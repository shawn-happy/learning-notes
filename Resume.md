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
- 博&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;客：https://juejin.cn/user/1855631358965384

## 个人优势

- 具备6年的java开发经验和大数据开发经验。
- 具备分布式项目开发经验，具备性能调优相关经验。
- 具备开源社区开发维护经验，具备开源项目二次开发经验。
- 熟练掌握常用的数据结构和算法。熟练掌握常用的设计模式和设计原则，熟练掌握面向对象设计思想。

* 熟练掌握MySQL、Redis。
* 熟练掌握Hadoop、Spark、Flink等大数据组件。熟练掌握Kafka。
* 熟练掌握Spring FrameWork全家桶、SpringBoot、Spring Data JPA、Mybatis等框架
* 熟练使用Docker、Kubernetes。
* 熟练使用Argo Workflow调度框架、Ray分布式计算框架。
* 熟练使用Prometheus、Grafana、Grafana Loki等监控指标收集，可视化框架。
* 熟练使用Ansible、Gitlab-CI、Helm Chart、Gradle、Maven等DevOps技能。

## 项目经历

### 上海民航华东凯亚系统集成有限公司 ｜ 2023.4 - 至今

#### 机场数据中台

* **技术栈：**SpringBoot、Pulsar、Drios、Seatunnel、Kubernetes、Hadoop
* **项目描述：**数据中台是机场突破数据赋能、迈向智慧机场的重要支撑。作为大数据技术的融合体，数据中台可以帮助企业实现数据纳管、数据治理、数据开发、数据共享，沉淀数据资产，实现快速赋能，解决数字化转型过程中数据层面的一系列问题。
* **我的职责：**
  * 负责重构数据中台，数据中台引入大数据的方案与架构设计。
  * 负责开发Argo workflow SDK。
  * 负责引入Doris，数据源管理模块支持多种数据源。
  * 负责引入Seatunnel，完善数据中台数据集成，数据开发，数据质量等任务的算子开发。
  * 负责自动化元数据采集模块。

### 慧博云通 |  2020.6 - 至今
#### AI平台 2020.06-至今

* **技术栈：**SpringBoot、Kafka、Zookeeper、Hadoop、Spark、Flink、Kubernetes、ArgoWorkflow、OpenMLDB、Filebeat
* **项目描述**：基于库伯学习圈理论将AI应用过程简化为行为，反馈，学习，应用四个步骤，并且内置了很多AI算法，比如GBDT、GBRT等机器学习算子，大幅降低AI使用门槛，为企业规模化落地AI提供效率引擎，助力企业轻松实现智能化转型。强化学习通过自动化重复观察、决策、反馈、学习，让智能体在特定环境中，根据当前的状态，做出行动，从而获得最大回报的一个最优策略。
* **我的职责**：
  * 负责基于Spark和Flink实现批量/实时数据ETL算子，拼表算子，数据质量检测算子的开发。
  * 负责基于Ray(开源的分布式计算框架)实现强化学习算法的调度框架。
  * 负责基础中间件服务开发：比如OpenMLDB SDK，Mixer(流式数据收集器)，Stateful-Coordination(选主框架)，Kafka-Connector，HDFS-Scanner，Loofah(基于Argo Workflow实现算子的管理和调度)，Magneto(利用K8s Informer机制实现对K8s Resources的监听)等。

#### 智能决策平台 2022.05-至今

* **技术栈：**SpringBoot、Kafka、Zookeeper、Hadoop、Spark、Kubernetes、ArgoWorkflow、OpenMLDB、Clickhouse
* **项目描述**：智能决策平台是一个聚焦解决金融领域高频，复杂，多变业务场景下决策问题的综合性策略中台。平台致力于将精简规则和领先AI技术相结合，构建业务人员易上手的闭环策略管理体系，帮助企业实现业务流程中的智能决策，助力提升决策水平，有序的帮助企业进行智能升级。
* **我的职责**：
  * 负责基于Shiro实现权限管理框架
  * 负责基于Spark，Clickhouse，Kafka实现Data-Warehouse
  * 负责基于Spark实现批量决策流的发布

### 江苏国泰新点有限公司 |  2017.7 - 2020.6
#### 数据交换平台 2017.06-2020.06

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
