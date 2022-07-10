# 桥接模式的理解

将抽象和实现解耦，让它们可以独立变化。

抽象：并非指的是抽象类或者接口，而是被抽象出来的一套类库。它只包含骨架代码，真正的业务逻辑需要委派给定义中的实现来完成。

而定义中的实现，也并非接口的实现类，而是一套独立的类库。抽象和实现独立开发，通过对象之间的组合关系，组装在一起







一个类存在两个或者多个以上独立变化的维度，我们可以通过组合的方式，让这两个或者多个维度可以独立进行扩展。非常类似组合优于继承的原则，通过组合关系来替代继承关系，避免继承层次的指数级爆炸。



jdbc的驱动就是桥接模式的经典应用。

```java
Class.forName("com.mysql.jdbc.Driver");
String url = "jdbc:mysql://localhost:3306/test";
String username = "root";
String password = "11111";
Connection con = DriverManager.getConnection(url, username, password);
```

如果是oracle，我们只要切换成oracle需要的驱动包就行了，`Oracle.jdbc.Driver.OracleDriver`



