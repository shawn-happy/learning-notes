## ElasticSearch
### elasticsearch简介
* elasticsearch是一个基于Lucene库的分布式，支持多租户的全文搜索引擎，也是数据分析引擎。
* elasticsearch是由java开发实现的。
* elasticsearch提供了REST接口和JSON文档，可以被任何编程语言调用
* elasticsearch可以进行近实时搜索和分析任何类型的数据，无论是结构化数据，半结构化数据，非结构化数据。elasticsearch都可以高效地进行存储和快速搜索。
* 高性能，高可用（数据，服务），可水平扩展，易用
* 支持不同节点类型

### elasticsearch的应用场景

* 在应用程序或网站上添加搜索功能

* 存储和分析日志、指标和安全事件数据

* 使用机器学习来实时自动建模数据的行为

* 使用Elasticsearch作为存储引擎来自动化业务工作流

* 使用Elasticsearch作为地理信息系统(GIS)管理、集成和分析空间信息

* 使用Elasticsearch作为生物信息学研究工具存储和处理遗传数据

### elasticsearch家族

#### elasticsearch的生态圈

* `logstash和beats`用于数据抓取
* `elasticsearch`用于数据的存储，分析，计算
* `kibana`用于数据可视化
* `x-pack`主要是商业用途，如安全，监控，告警，图查询，机器学习等。

#### logstash

##### 简介

* 开源的服务端数据处理管道，支持从不同来源采集数据，转换数据，并将数据发送到不同的存储库中。
* 最初用于日志的采集和处理。

##### 特性

* 实时解析和转换数据
* 可扩展
  * 支持200多个插件（日志，数据库，Arcsigh，Netflow）
* 可靠性，安全性
  * logstash会通过持久化队列来保证至少将运行中的事件送达一次
  * 数据传输加密
* 监控

#### kibana

##### 简介

* Kiwi fruit + Banana
* 数据可视化工具，帮助用户解开对数据的任何疑问
* 基于Logstash的工具

### elasticsearch的基本概念

#### Document

##### Document MetaData

#### Index

#### Type

#### Cluster

#### Node

##### Master Node & Master-eligible Nodes

##### Data Node & Coordinating Node

##### Hot & Warm Node

##### Machine Learning Node

##### Tribe Node

#### Shard

#### Replica

#### 倒排索引



