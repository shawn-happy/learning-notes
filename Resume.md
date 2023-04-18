<h1 align=center>个人简历</h1>

<p align="center">
邵帅｜6年｜大数据高级开发工程师/java高级开发工程师
</p>

<p align="center">手机号-18721318962｜微信号-ss19950｜邮箱-gentlestshawn@gmail.com</p>

## 基本信息

- 学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;历：本科
- 毕业院校：淮阴工学院
- 专&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业：信息与计算科学
- 证       书：英语CET4、计算机二级
- &nbsp;GitHub&nbsp;&nbsp;：[https://github.com/shawn-happy](https://github.com/shawn-happy)

## 个人优势

- 具备6年的java开发经验和大数据开发经验。
- 具备分布式项目开发经验，具备性能调优相关经验。
- 具备开源社区开发维护经验，具备开源项目二次开发经验。
- 了解一些机器学习，强化学习算法。

## 专业技能

* 计算机基础：熟练掌握常用的数据结构和算法。熟练掌握常用的设计模式和设计原则，熟练掌握面向对象设计思想。
* 编程语言：Java、Python。熟练使用Java SE/Java EE，熟练掌握JVM基础知识以及Java多线程编程。
* 数据库：熟练掌握MySQL、Redis。
* 大数据：熟练掌握Hadoop、Spark、Flink等大数据组件。熟练掌握Kafka、ElasticSearch。
* Spring框架：熟练掌握Spring FrameWork全家桶、SpringBoot、Spring Data JPA。
* 基础设施：熟练使用Docker、Kubernetes。
* 调度：熟练使用Argo Workflow调度框架、Ray分布式计算框架。
* 监控：熟练使用Prometheus、Grafana、Grafana Loki等监控指标收集，可视化框架。
* DevOps技能：熟练使用Ansible、Gitlab-CI、Helm Chart、Gradle、Maven等DevOps技能。

## 工作经历
### 慧博云通 |  2020.6 - 至今
- 负责AI平台、智能决策平台的开发。

### 江苏国泰新点有限公司 |  2017.7 - 2020.6
- 负责大数据交换平台和Kettle插件的开发。

## 项目经历

### AI平台 2020.07-至今

* **人数：**7人
* **技术栈：**SpringBoot、Kafka、Zookeeper、Hadoop、Spark、Flink、Kubernetes、ArgoWorkflow、OpenMLDB、Filebeat

* **项目描述**：基于库伯学习圈理论将AI应用过程简化为行为，反馈，学习，应用四个步骤，并且内置了很多AI算法，比如GBDT、GBRT等机器学习算子，大幅降低AI使用门槛，为企业规模化落地AI提供效率引擎，助力企业轻松实现智能化转型。强化学习通过自动化重复观察、决策、反馈、学习，让智能体在特定环境中，根据当前的状态，做出行动，从而获得最大回报的一个最优策略。
* **我的职责**：
  * 负责基于Spark和Flink实现批量/实时数据ETL算子，拼表算子，数据质量检测算子的开发。
  * 负责基于Ray(开源的分布式计算框架)实现强化学习算法的调度框架。
  * 负责基础中间件服务开发：比如OpenMLDB SDK，Mixer(流式数据收集器)，Stateful-Coordination(选主框架)，Kafka-Connector，HDFS-Scanner，Loofah(基于Argo Workflow实现算子的管理和调度)，Magneto(利用K8s Informer机制实现对K8s Resources的监听)等。
  * 负责基于数据质量检测算子实现数据质量报告，方案报告，指标报告的计算。

### 智能决策平台 2022.05-至今

* **人数：**6人
* **技术栈：**SpringBoot、Kafka、Zookeeper、Hadoop、Spark、Kubernetes、ArgoWorkflow、OpenMLDB、Clickhouse

* **项目描述**：智能决策平台是一个聚焦解决金融领域高频，复杂，多变业务场景下决策问题的综合性策略中台。平台致力于将精简规则和领先AI技术相结合，构建业务人员易上手的闭环策略管理体系，帮助企业实现业务流程中的智能决策，助力提升决策水平，有序的帮助企业进行智能升级。
* **我的职责**：
  * 负责基于Shiro实现权限管理框架
  * 负责基于Spark，Clickhouse，Kafka实现Data-Warehouse
  * 负责基于Spark实现批量决策流的发布

### 数据交换平台 2017.06-2020.06

* **人数：**3人
* **技术栈：**SpringMVC、Kettle、JavaEE、Tomcat、Canal、Kafka、HDFS

* **项目描述**：Kettle是一款pentaho开源的ETL工具，纯Java编写。DataExchange Platform会使用Spring Boot集成Kettle，并提供公司常用的转换，作业组件，提供作业模板，支持流式数据和批量数据推送，提供高可用机制。
* **我的职责**：
  * 负责Kettle的二次开发以及SDK封装，比如交换日志，远程文件上传下载等插件以及基于Quartz实现Kettle Job的调度，实现高可用。
  * 负责实现Kettle job模板的开发，主要支持全量推送，触发器增量推送，时间戳增量推送等。
  * 负责基于Alibaba Canal监控MySQL BinLog日志，实时写入Kafka，再通过kafka注册到HDFS。

### OpenMLDB（开源项目）

* **项目描述**：OpenMLDB是一个开源机器学习数据库，面向机器学习应用提供正确，高效的数据供给。OpenMLDB超过了10倍的机器学习数据开发效率的提升，OpenMLDB也提供了统一的计算与存储引擎，减少了开发运维的复杂性和总体成本。

* **项目地址**：https://github.com/4paradigm/OpenMLDB

* **我的职责**：
* 负责DDL语句的解析
  
* 参与JDBC部分实现
  
* 参与Java/Python SDK部分实现
