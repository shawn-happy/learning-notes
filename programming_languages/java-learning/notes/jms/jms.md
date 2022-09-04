# JMS

## JMS元素

* 提供方（Provider）：实现JMS接口的MOM
* 客户端（Client）：生产或者消费消息的应用或进程
* 生产者（Producer）：创建和发送消息的JMS客户端
* 消费者（Consumer）：接收消息的JMS客户端
* 消息（Message）：JMS客户端之间的传输数据对象
* 队列（Queue）：包含待读取消息的准备区域
* 主题（Topic）：发布消息的分布机制

### JMS消息

* 消息头（Header）：所有消息支持相同的头字段集合，头字段包含客户端和提供方识别和路由消息的数据。
  * JMSReplyTo:消息回复地址，说明消息期待回复，可选
  * JMSRedelivered:消息重投递标识
  * JMSType:消息客户端发送消息时的类型标识
  * JMSExpiration:消息过期
  * JMSPriority：消息优先级
* 消息属性（Properties）：除标准的头字段以外，提供一种内建机制来添加可选的消息头字段。
* 应用特殊属性
* 标准属性
* 提供方特殊属性
* 消息主体（Body）
  * StreamMessage
  * MapMessage
  * TextMessage
  * ObjectMessage
  * BytesMessage

* JMS消息确认（Acknowledgment）：所有JMS消息支持acknowledge方法的使用，当客户端已规定JMS消息将明确地收到。如果客户端使用了消息自动确认的话，调用acknowledge方法将被忽略。
* JMS消息模型
  * 点对点模型(Point-To-Point Model)
  * 发布订阅模型（Publish/Subscribe Model）

