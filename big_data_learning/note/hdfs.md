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

#### NameNode



#### DataNode



#### File System Namespace



#### Block Cache



#### Data Replication



### Data Replication Policies

### NameNode  HA

### Federation

### 

### Shell

### Java API

### Related Documentation

[Hadoop权威指南-第三章](https://book.douban.com/subject/26359169/)

[HDFS官网](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html)