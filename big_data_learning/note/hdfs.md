## HDFS

### Introduction

* HDFS是hadoop自带的分布式文件系统，即Hadoop Distributed File System。
* HDFS具有高容错性，部署成本低等特性。
* HDFS提供了对应用数据的高吞吐量访问，适用于大量数据集的应用。
* HDFS放宽了对POXIS的要求，采用流式的数据访问方式。
* HDFS 最初是作为 Apache Nutch 网络搜索引擎项目的基础设施而构建的。
* HDFS现在是Apache Hadoop的主要项目。

### Advantage

* **高容错性**：硬件故障是常态而不是例外。 一个 HDFS 实例可能由数百或数千台服务器组成，每台服务器都会存储一部分数据。 事实上，有大量组件并且每个组件都有很大的故障概率，这意味着 HDFS 的某些组件总是无法正常工作。 因此，故障检测和快速、自动恢复是 HDFS 的核心架构目标。由于 HDFS 采用数据的多副本方案，所以部分硬件的损坏不会导致全部数据的丢失。
* **流式数据访问**：HDFS 更适合批处理而不是用户交互使用，适合一次写入，多次读取。HDFS 设计的重点是支持高吞吐量的数据访问， 而不是低延迟的数据访问。
* **大数据集**：HDFS 适合于大文件的存储，文档的大小应该是是 GB 到 TB 级别的。
* **简单一致性模型**：HDFS 应用程序需要对文件进行一次写入多次读取的访问模型。 文件一旦创建、写入和关闭，除了追加和截断外，无需更改。 支持将内容附加到文件的末尾，但不能在任意点更新。 这种假设简化了数据一致性问题并实现了高吞吐量数据访问。 MapReduce 应用程序或网络爬虫应用程序与此模型完美契合。
* **移动计算比移动数据更容易**：如果应用程序请求的计算在它所操作的数据附近执行，它会更有效率。 当数据集的大小很大时尤其如此。 这最大限度地减少了网络拥塞并增加了系统的整体吞吐量。 假设通常将计算迁移到数据所在的位置，而不是将数据移动到应用程序运行的位置。 HDFS 为应用程序提供了接口，使它们更接近数据所在的位置。
* **跨异构硬件和软件平台的可移植性**：HDFS是使用Java开发的，任何支持java的服务器上都可以部署HDFS，所以HDFS 具有良好的跨平台移植性，这使得其他大数据计算框架都将其作为数据持久化存储的首选方案。

### Architecture

![hdfsarchitecture](./images/hdfsarchitecture.png)

HDFS具有主/从架构，分为管理节点和工作节点。HDFS集群由单个NameNode（管理节点），和多个DataNode（工作节点）构成。

HDFS 公开了一个文件系统命名空间，并允许将用户数据存储在文件中。 在内部，一个文件被分成一个或多个块，这些块存储在一组 DataNode 中。

NameNode主要负责管理文件系统的namespace以及管理Client对文件系统的访问。NameNode 执行文件系统命名空间操作，如打开、关闭和重命名文件和目录。 它还确定块到 DataNode 的映射。 

DataNode主要负责管理连接到它们运行的节点的存储，负责处理来自文件系统客户端的读写请求，根据NameNode的指令执行块的创建、删除和复制。

### Concept

#### Block

HDFS为了支持高效的处理数据，引入了数据块的概念，默认大小128MB（1.X的版本默认是64MB）。HDFS上的文件也被划分为块大小的多个分块（Chunk），作为独立的存储单元。HDFS中小于一个块大小的文件，不会占据整个块的空间（例如，当一个1MB的文件存储在一个128MB的块中，文件只使用1MB的存储空间，而不是128MB）。

引入数据块带来好处：

1. 一个文件的大小可以大于网络中任意一个磁盘的容量，文件的所有块并不需要存储在同一个磁盘里，因此他们可以利用集群上的任意一个磁盘进行存储。
2. 使用抽象块而非整个文件作为存储单元，大大简化了存储子系统的设计。由于块的大小是固定的，因此计算单个磁盘能存储多少个块就相对容易，便于存储管理。此外，块不需要处理文件的元数据（权限等），消除了对元数据的顾虑。
3. 适用于数据备份，提高数据容错力，提高可用性。使用Data Replication。

为什么块的大小不能设置的过大也不能过小（为什么块的大小为128MB?）：

1. HDFS的块比磁盘的块大，其目的是为了最小化寻址开销。
2. 如果块过大，从磁盘传输数据时间会明显大于定位这个块开始位置所需的时间，导致程序在处理这块数据时很慢。
3. 如果块过小，会增加寻址时间，程序会一直寻找块开始的位置。

总结：

传输一个由多个块组成的大文件的时间取决于磁盘传输速率。

#### NameNode & DataNode

**NameNode**是HDFS的管理节点，主要负责的内容有：

1. 管理HDFS的命名空间。
2. 维护文件系统树以及整棵树内所有的文件和目录。
3. 记录每个文件中各个Block所在的DataNode信息。
4. 处理客户端对文件的请求。
5. 配置副本策略。

集群中单一Namenode的结构大大简化了系统的架构。Namenode是所有HDFS元数据的仲裁者和管理者，这样，用户数据永远不会流过Namenode。

**DataNode**是HDFS的工作节点，主要负责的内容有：

1. 存储并检索数据块，受Client或者NameNode调度，进行数据块的创建、删除和复制。
2. 定期向NameNode发送它们所存储的数据块的列表。

没有DataNode，文件系统不会崩溃，文件系统只是无法存储文件，也不会丢失数据。没有NameNode，文件系统会崩溃，文件系统上的所有文件将丢失（无法读出，因为无法定位元数据块的位置，也就无法根据DataNode的块来重构文件）。所以对NameNode实现容错是非常重要的，Hadoop提供了两种机制：

1. 备份组成文件系统元数据持久状态的文件。Hadoop可以通过配置使NameNode在多个FS上保存元数据的持久状态，这些写操作是实时同步的，并且是原子操作。
2. Secondary NameNode：并非NameNode的热备。当NameNode挂掉的时候，它并不能马上替换NameNode并提供服务。其主要作用是定期合并Fsimage和Edits，并推送给NameNode。

#### Secondary NameNode

NameNode将对文件系统的改动追加保存到本地文件系统上的一个日志文件（edits）。当一个NameNode启动时，它首先从一个映像文件（fsimage）中读取HDFS的状态，接着应用日志文件中的edits操作。然后它将新的HDFS状态写入（fsimage）中，并使用一个空的edits文件开始正常操作。因为NameNode只有在启动阶段才合并fsimage和edits，所以久而久之日志文件可能会变得非常庞大，特别是对大型的集群。日志文件太大的另一个副作用是下一次NameNode启动会花很长时间。

Secondary NameNode定期合并Fsimage和edits日志，将edits日志文件大小控制在一个限度下。因为内存需求和NameNode在一个数量级上，所以通常secondary NameNode和NameNode运行在不同的机器上。Secondary NameNode通过bin/start-dfs.sh在conf/masters中指定的节点上启动。

Secondary NameNode的检查点进程启动，是由两个配置参数控制的：

- fs.checkpoint.period，指定连续两次检查点的最大时间间隔， 默认值是1小时。
- fs.checkpoint.size定义了edits日志文件的最大值，一旦超过这个值会导致强制执行检查点（即使没到检查点的最大时间间隔）。默认值是64MB。

Secondary NameNode保存最新检查点的目录与NameNode的目录结构相同。 所以NameNode可以在需要的时候读取Secondary NameNode上的检查点镜像。

如果NameNode上除了最新的检查点以外，所有的其他的历史镜像和edits文件都丢失了， NameNode可以引入这个最新的检查点。以下操作可以实现这个功能：

- 在配置参数dfs.name.dir指定的位置建立一个空文件夹；
- 把检查点目录的位置赋值给配置参数fs.checkpoint.dir；
- 启动NameNode，并加上-importCheckpoint。

NameNode会从fs.checkpoint.dir目录读取检查点， 并把它保存在dfs.name.dir目录下。 如果dfs.name.dir目录下有合法的镜像文件，NameNode会启动失败。NameNode会检查fs.checkpoint.dir目录下镜像文件的一致性，但是不会去改动它。

#### File System Namespace

HDFS支持传统的层次型文件组织结构。用户或者应用程序可以创建目录，然后将文件保存在这些目录里。文件系统名字空间的层次结构和大多数现有的文件系统类似：用户可以创建、删除、移动或重命名文件。当前，HDFS不支持用户磁盘配额和访问权限控制，也不支持硬链接和软链接。但是HDFS架构并不妨碍实现这些特性。

NameNode负责维护文件系统的名字空间，任何对文件系统名字空间或属性的修改都将被NameNode记录下来。应用程序可以设置HDFS保存的文件的副本数目。文件副本的数目称为文件的副本系数，这个信息也是由NameNode保存的。

#### Block Cache

1. 通常DataNode从磁盘中读取块，但对于访问频繁的文件，其对应的块可能被显式地缓存在DataNode的内存中，以堆外块内存的形式存在。

2. Spark,MapReduce通过在缓存块的DataNode上运行任务，可以利用块缓存的优势提高读操作的性能。

3. 用户或者应用程序可以通过在Cache Pool中增加一个Cache Directive来告诉NameNode需要缓存哪些文件以及存多久。
4. Cache Pool用于管理缓存权限和资源使用。

### HDFS Federation

从整个HDFS系统架构上看，NameNode是其中最重要、最复杂也是最容易出现问题的地方，而且一旦NameNode出现故障，整个Hadoop集群就将处于不可服务的状态，同时随着数据规模和集群规模地持续增长，很多小量级时被隐藏的问题逐渐暴露出来，比如内存将成为系统横向扩展的瓶颈

![federation-background](./images/federation-background.gif)

HDFS 有两个主要层：

- 命名空间（Namespace）
  - 由目录、文件和块组成。
  - 它支持所有与命名空间相关的文件系统操作，如创建、删除、修改和列出文件和目录。
- 块存储服务（Block Storage），它有两个部分：
  - 块管理（在 Namenode 中执行）
    - 通过处理注册和定期心跳来提供 Datanode 集群成员资格。
    - 处理块报告并维护块的位置。
    - 支持创建、删除、修改、获取区块位置等区块相关操作。
    - 管理副本放置、复制不足的块的块复制，并删除复制过度的块。
  - 存储 - 由 Datanodes 通过在本地文件系统上存储块并允许读/写访问来提供。

Namespace管理的元数据除内存常驻外，也会周期Flush到持久化设备上FsImage文件；BlocksMap元数据只在内存中存在；当NameNode发生重启，首先从持久化设备中读取FsImage构建Namespace，之后根据DataNode的汇报信息重新构造BlocksMap。这两部分数据结构是占据了NameNode大部分JVM Heap空间。除了对文件系统本身元数据的管理之外，NameNode还需要维护整个集群的机架及DataNode的信息等信息。为了解决内存限制，单点故障等问题，Hadoop在2.X的发行版中引入Federation，允许HDFS添加多个NameNode，实现横向扩展。

![federation.gif](./images/federation.gif)

在Federation环境下，每个NameNode维护一个Namespace Volume，由Namespace的元数据和一个Block Pool组成，Block Pool包含该Namespace下文件的所有Block。

Namespace Volume相互独立，两两互不通信，每个Namespace都可以为自己的Block Pool生成Block ID。即使其中一个NameNode挂了也不会影响其他NameNode维护的Namespace的可用性。当这个Namespace/NameNode被删除，其对应的Block Pool也会被删除。

Datanodes 被所有 Namenodes 用作块的公共存储。每个 Datanode 都向集群中的所有 Namenode 注册。Datanodes 定期发送心跳和块报告。它们还处理来自 Namenodes 的命令。

用户可以通过ViewFileSystem和viewfs://URI进行配置和管理。

- 扩展性：NameNode内存使用和元数据量正相关。随着集群规模和业务的发展，即使经过小文件合并与数据压缩，仍然无法阻止内存会成为HDFS横向扩展的瓶颈。
- 可用性：NameNode可以横向扩展，Namespace之间相互独立，即使其中一个NameNode挂了，也不会影响其他NameNode维护的Namespace的可用性。
- 性能：文件系统吞吐量不受单个 Namenode 的限制。向集群添加更多 Namenode 可扩展文件系统读/写吞吐量。
- 隔离性：单个 Namenode 在多用户环境中不提供隔离。例如，实验性应用程序可能会使 Namenode 过载并减慢生产关键应用程序的速度。通过使用多个 Namenode，可以将不同类别的应用程序和用户隔离到不同的命名空间。

### Data Replication

#### Data Replication Policies

### NameNode  HA

### Shell

### Java API

### Related Documentation

[Hadoop权威指南-第三章](https://book.douban.com/subject/26359169/)

[HDFS官网](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html)

[美团对HDFS的应用](https://tech.meituan.com/2017/04/14/hdfs-federation.html)