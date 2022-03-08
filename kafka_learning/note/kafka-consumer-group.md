### Consumer Group

Consumer Group 是 Kafka 提供的可扩展且具有容错性的消费者机制。

三个特性

* Consumer Group 下可以有一个或多个 Consumer 实例。这里的实例可以是一个单独的进程，也可以是同一进程下的线程。在实际场景中，使用进程更为常见一些。
* Group ID 是一个字符串，在一个 Kafka 集群中，它标识唯一的一个 Consumer Group。
* Consumer Group 下所有实例订阅的主题的单个分区，只能分配给组内的某个 Consumer 实例消费。这个分区当然也可以被其他的 Group 消费。

Kafka 仅仅使用 Consumer Group 这一种机制，却同时实现了传统消息引擎系统的两大模型：如果所有实例都属于同一个 Group，那么它实现的就是消息队列模型；如果所有实例分别属于不同的 Group，那么它实现的就是发布 / 订阅模型。

理想情况下，Consumer 实例的数量应该等于该 Group 订阅主题的分区总数。

### __consumer_offset

老版本的 Consumer Group 把位移保存在 ZooKeeper 中。Apache ZooKeeper 是一个分布式的协调服务框架，Kafka 重度依赖它实现各种各样的协调管理。将位移保存在 ZooKeeper 外部系统的做法，最显而易见的好处就是减少了 Kafka Broker 端的状态保存开销。现在比较流行的提法是将服务器节点做成无状态的，这样可以自由地扩缩容，实现超强的伸缩性。Kafka 最开始也是基于这样的考虑，才将 Consumer Group 位移保存在独立于 Kafka 集群之外的框架中。但是ZooKeeper 这类元框架其实并不适合进行频繁的写更新，而 Consumer Group 的位移更新却是一个非常频繁的操作。这种大吞吐量的写操作会极大地拖慢 ZooKeeper 集群的性能，因此采用了将位移保存在 Kafka 内部主题的方法。这个内部主题就是的 __consumer_offsets。

位移主题就是普通的 Kafka 主题。你可以手动地创建它、修改它，甚至是删除它，但是消息格式却是 Kafka 自己定义的，所以不能随意地向这个主题里写入数据，否则可能会让Broker崩溃。

Consumer Group通过一组KV对管理offset，形式：`Map<TopicPartition, OffsetAndMetadata>`。

* Key 中应该保存 3 部分内容：<Group ID，主题名，分区号 >，
* Value主要存储位移值，保存 Consumer Group 信息的消息，删除 Group 过期位移甚至是删除 Group 的消息（tombstone 消息，即墓碑消息，也称 delete mark，一旦某个 Consumer Group 下的所有 Consumer 实例都停止了，而且它们的位移数据都已被删除时，Kafka 会向位移主题的对应分区写入 tombstone 消息，表明要彻底删除这个 Group 的信息）。

当 Kafka 集群中的第一个 Consumer 程序启动时，Kafka 会自动创建位移主题，默认分区数（offsets.topic.num.partitions）50，副本数（offsets.topic.replication.factor）3

kafka使用Compact策略来删除位移主题中过期的消息，避免该主题无限期膨胀。

### Rebalance

Rebalance 本质上是一种协议，规定了一个 Consumer Group 下的所有 Consumer 如何达成一致，来分配订阅 Topic 的每个分区。比如某个 Group 下有 20 个 Consumer 实例，它订阅了一个具有 100 个分区的 Topic。正常情况下，Kafka 平均会为每个 Consumer 分配 5 个分区。这个分配的过程就叫 Rebalance。

Rebalance 的触发条件有 3 个。

* 组成员数发生变更。比如有新的 Consumer 实例加入组或者离开组，抑或是有 Consumer 实例崩溃被“踢出”组。
* 订阅主题数发生变更。Consumer Group 可以使用正则表达式的方式订阅主题，比如 consumer.subscribe(Pattern.compile("t.*c")) 就表明该 Group 订阅所有以字母 t 开头、字母 c 结尾的主题。在 Consumer Group 的运行过程中，你新创建了一个满足这样条件的主题，那么该 Group 就会发生 Rebalance。
* 订阅主题的分区数发生变更。Kafka 当前只能允许增加一个主题的分区数。当分区数增加时，就会触发订阅该主题的所有 Group 开启 Rebalance。

分配策略：

* Range分配策略是面向每个主题的，首先会对同一个主题里面的分区按照序号进行排序，并把消费者线程按照字母顺序进行排序。然后用分区数除以消费者线程数量来判断每个消费者线程消费几个分区。如果除不尽，那么前面几个消费者线程将会多消费一个分区。 
* RoundRobin策略的原理是将消费组内所有消费者以及消费者所订阅的所有topic的partition按照字典序排序，然后通过轮询算法逐个将分区以此分配给每个消费者。 使用RoundRobin分配策略时会出现两种情况： 
  1. 如果同一消费组内，所有的消费者订阅的消息都是相同的，那么 RoundRobin 策略的分区分配会是均匀的。 
  2. 如果同一消费者组内，所订阅的消息是不相同的，那么在执行分区分配的时候，就不是完全的轮询分配，有可能会导致分区分配的不均匀。如果某个消费者没有订阅消费组内的某个 topic，那么在分配分区的时候，此消费者将不会分配到这个 topic 的任何分区。 
* Sticky分配策略，这种分配策略是在kafka的0.11.X版本才开始引入的，是目前最复杂也是最优秀的分配策略。 Sticky分配策略的原理比较复杂，它的设计主要实现了两个目的： 
  1. 分区的分配要尽可能的均匀； 
  2. 分区的分配尽可能的与上次分配的保持相同。 如果这两个目的发生了冲突，优先实现第一个目的。

Rebalance诟病的地方：

* 在 Rebalance 过程中，所有 Consumer 实例都会停止消费，等待 Rebalance 完成。
* 其次，目前 Rebalance 的设计是所有 Consumer 实例共同参与，全部重新分配所有分区。其实更高效的做法是尽量减少分配方案的变动。例如实例 A 之前负责消费分区 1、2、3，那么 Rebalance 之后，如果可能的话，最好还是让实例 A 继续消费分区 1、2、3，而不是被重新分配其他的分区。这样的话，实例 A 连接这些分区所在 Broker 的 TCP 连接就可以继续用，不用重新创建连接其他 Broker 的 Socket 资源。
* 最后，Rebalance 实在是太慢了。曾经，有个国外用户的 Group 内有几百个 Consumer 实例，成功 Rebalance 一次要几个小时！这完全是不能忍受的。最悲剧的是，目前社区对此无能为力，也许最好的解决方案就是避免 Rebalance 的发生吧。

不必要的Rebalance：

* 未能及时发送心跳，导致 Consumer 被“踢出”Group 而引发的
* Consumer 消费时间过长导致的

用于减少Rebalance的4个参数：

* session.timeout.ms
* heartbeat.interval.ms
* max.poll.interval.ms
* GC 参数

推荐配置

* 设置 session.timeout.ms = 6s。
* 设置 heartbeat.interval.ms = 2s。
* 要保证 Consumer 实例在被判定为“dead”之前，能够发送至少 3 轮的心跳请求，即 session.timeout.ms >= 3 * heartbeat.interval.ms。