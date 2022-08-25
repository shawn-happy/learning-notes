<h1 align=center>个人简历</h1>

## 基本信息

- 姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：邵帅
- 工作经验：5年
- 求职意向：大数据开发工程师/java开发工程师
- &nbsp;GitHub&nbsp;&nbsp;：[https://github.com/shawn-happy](https://github.com/shawn-happy)
- 电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：18721318962
- 邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱：gentlestshawn@gmail.com
- 微&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;信：ss19950
- 毕业院校：淮阴工学院
- 专&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业：信息与计算科学
- 证       书：英语CET4、计算机二级

## 专业技能

以下均为我熟练使用的技能

* 编程语言：Java，Python。熟练使用Java SE/Java EE，熟练掌握JVM基础知识，具备性能调优经验。
* 设计模式：熟练掌握常用的设计模式和设计原则，熟练掌握面向对象设计思想。
* 数据库：MySQL。熟练掌握MySQL基础知识，SQL语法、事务、锁、MVCC等，具备SQL调优经验。
* 基础设施：熟练使用Docker，Kubernetes等。
* Spring FrameWork框架：熟练掌握Spring FrameWork全家桶。
* ORM框架：熟练掌握Mybatis、Hibernate等ORM框架。
* 大数据：熟练掌握Hadoop、YARN、Spark、Flink、Hive等大数据组件。熟练掌握Kafka，ELK等流数据组件。
* 图数据库：熟练使用ArangoDB图数据库。
* 调度层：熟练使用Argo Workflow调度层框架。
* 监控：熟练使用Prometheus，Grafana等监控指标收集，可视化框架。
* DevOps技能：熟练使用Ansible、Jenkins、Gitlab-CI，Github-Workflow等DevOps技能。
* 模版引擎框架：熟练掌握Velocity、Jinja等模版引擎框架。
* Testing框架：熟练掌握JUnit、Mockito、PowerMockito、TestContainer、WireMock、Pytest等测试框架。
* Python模块和框架：熟练掌握Numpy、Pandas等数据分析模块。熟练掌握Django Web框架。Ray分布式计算框架。

## 工作经历
### 第四范式 |  2020.6 - 至今
- 负责HyperCycle ML/RL(AI中台)的开发。
- 负责HyperCycle Decision(策略中台)的开发。
- 负责HyperCycle Data(数据中台)的开发。

### 江苏国泰新点有限公司 |  2017.7 - 2020.6
- 负责政务大数据交换平台的开发。
- 负责Kettle(开源项目)的插件开发。
- 负责流式数据ETL的开发。

## 项目经历
### HyperCycle ML 2020.07-至今

* **项目描述**：基于库伯学习圈理论将AI应用过程简化为行为，反馈，学习，应用四个步骤，并且内置了很多AI算法，比如GBDT、GBRT等机器学习算子，大幅降低AI使用门槛，为企业规模化落地AI提供效率引擎，助力企业轻松实现智能化转型。
* **职责描述**：
  * 基于Spark和Flink实现批量/实时数据ETL和拼表任务。
  * 负责基础中间件开发：比如OpenMLDB SDK，调度层Argo Workflow SDK，Kubernetes API SDK，Flink Job，Mixer(流式数据收集器)，分布式状态机等。
  * 负责平台的监控功能，比如实时场景的服务监控，数据流监控。

### HyperCycle RL 2021.01-2021.07

* **项目描述**：强化学习通过自动化重复观察、决策、反馈、学习，让智能体在特定环境中，根据当前的状态，做出行动，从而获得最大回报的一个最优策略。
* **职责描述**：
  * 负责算子调度层的开发，利用Argo Workflow实现算子的调度运行。
  * 负责Ray(Python开源的分布式计算框架)与算法的对接。
  * 负责仿真器调度器的开发，利用Docker实现仿真器的调度。
  * 负责编写Ansible，Gitlab-CI，DockerFile等DevOps工作。

### HyperCycle Decision 2022.05-至今

* **项目描述**：智能决策平台是一个聚焦解决金融领域高频，复杂，多变业务场景下决策问题的综合性策略中台。平台致力于将精简规则和领先AI技术相结合，构建业务人员易上手的闭环策略管理体系，帮助企业实现业务流程中的智能决策，助力提升决策水平，有序的帮助企业进行智能升级。
* **职责描述**：
  * 使用ArangoDB实现社会网络。
  * 使用Argo Workflow实现批量场景的决策流发布以及状态管理。
  * 对接HyperCycle ML。

### HyperCycle Data 2022.06-至今

* **项目描述**：将不同系统的数据相互打通，实现数据自由离线或实时流动， 并提供丰富的异构数据源之间高速稳定的数据移动能力，具备数据集成，数据开发，数据质量，数据治理等模块功能。
* **职责描述**：
  * 基于Argo Workflow实现调度层
  * 基于Spark实现部分Spark算子的开发
  * 使用Kubernetes Informer机制，监控Kubernetes Pod的运行状态和日志收集。

### DataExchange Platform 2017.06-2020.06

* **项目描述**：Kettle是一款pentaho开源的ETL工具，纯Java编写，需要下载客户端工具去编辑ETL的DAG作业。DataExchange Platform会使用Spring Boot集成Kettle，并提供公司常用的转换，作业组件，提供作业模板，支持流式数据和批量数据推送，提供高可用机制。
* **职责描述**：
  * 负责Kettle源码的编译，去掉公司不常用的组件，并开发公司需要的组件，比如交换日志组件，远程文件上传下载组件。
  * 负责Kettle SDK封装。
  * 负责实现Kettle job模板的开发，主要支持全量推送，触发器增量推送，时间戳增量推送等。
  * 负责CDC的实现，主要通过Alibaba Canal监控MySQL BinLog日志，日志内容写入Kafka，并使用FLink实时同步到MongoDB/Hbase里。

### 开源项目：2021.07 - 2021.08

#### OpenMLDB

* **项目描述**：OpenMLDB是一个开源机器学习数据库，面向机器学习应用提供正确，高效的数据供给。OpenMLDB超过了10倍的机器学习数据开发效率的提升，OpenMLDB也提供了统一的计算与存储引擎，减少了开发运维的复杂性和总体成本。

* **项目地址**：https://github.com/4paradigm/OpenMLDB

* **职责描述**：

  * 负责DDL语句的解析

  * 负责JDBC部分实现

  * 负责Java/Python SDK部分实现


## 自我评价
- 工作积极认真，细心负责，有较强的分析，动手能力和学习能力。
- 勤奋好学，踏实肯干，有很强的社会责任感。
- 坚毅不拔，吃苦耐劳，喜欢和善于迎接新挑战。
- 适应能力强，心胸豁达，有很强的团队协作精神和与人沟通能力。
- 有开源社区开发维护经验。
