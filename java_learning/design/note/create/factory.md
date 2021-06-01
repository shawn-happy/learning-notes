# Factory Design Pattern

## 分类

**简单工厂，工厂方法，抽象工厂。**

在jdbc中，jdk定义了一套标准的java连接数据库的方式，jdk只是提供了Jdk，各大数据库要想与java交互，就必须实现jdk提供的相关接口。数据库的种类很多，如Mysql,oracle,sqlserver等。下面是一段mysql，jdbc的连接代码。

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//基本jdbc处理
public class Testdb
{

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");

        String dburl = "jdbc:mysql://localhost:3306/testDB";
        Connection conn = DriverManager.getConnection(dburl, "root", "11111");

        try {
            Statement stat = conn.createStatement();
            stat.executeUpdate("insert into people(name,age)values('张三','10')");
            // conn.commit();
            ResultSet rst = stat.executeQuery("select * from people");
            while (rst.next()) {                
                System.out.println(rst.getString(1) + "  " + rst.getString(2) + "  " + rst.getString(3));
            }
//            stat.execute("delete from people");
            rst.close();
            stat.close();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            conn.close();
        }

        
    }

}
```

上述代码，简单的写出了jdbc的连接方式。可以发现，java.sql包下的很多都是接口，也jdk并没有具体实现类，具体实现类都是数据库驱动包里的。其实这是一条设计原则，**针对接口编程，而不是针对实现编程**。然而我们在写代码的时候，通常都需要new这个关键字来创建一个对象实例，这就违背了上述原则。

```java
IUserService service = new UserServiceRDBImpl();
IUserService service = new UserServiceRedisImpl();
//=====
```

这里有一些要实例化的具体类，究竟实例化哪个类，要在运行时有一些条件来决定。这样的代码，一旦有其他实现，就得在上述代码中再加一些条件，检查，修改等，不利于扩展，难以维护，很容易犯错。在技术上，new没有错，毕竟是java的基础部分。针对接口编程，可以隔离掉以后系统可能发生的一大堆改变。

那我们可能是这么写代码的：

```java
Connection conn = null;
if(mysql){
    conn = new MysqlConnection();
}else if(oracle){
    conn = new OracleConnection();
}
//...
conn.findlist();
conn.find();
conn.delete();
conn.update();
conn.insert();
```

目前来说，RDB数据库很多，你的程序要支持那么多数据库，不可能写很多判断条件，判断到底使用哪个数据库连接。压力来自于需要支持很多数据库。那么我们如何改善呢？工厂模式就很好的解决了这个问题。

## 概念

**定义了一个创建对象的接口，但由子类决定要实例化的类是哪一个。工厂方法让类把实例化推迟到子类。**

## 针对前言做改进

**1.封装创建对象的代码**

![1545921652762](C:\Users\shao\AppData\Roaming\Typora\typora-user-images\1545921652762.png)

```java
public class ConnectionFactory{
    
    public Connection createBean(String type){
        Connection conn = null;
        if("mysql".equals(type)){
            conn = new MysqlConnection();
        }else if("oracle".equals(type)){
            conn = new OracleConnection();
        }
        // ...
        return conn;
    }
    
}
```

问：

1. 这样做的好处？

   貌似只是把方法抽取到另一个类里，问题依旧存在。但是我们从实际的业务代码中，把这一段删除，那以后需要改变实现的时候，只需要修改这个类即可。

2. 为什么不用静态？

   利用静态方法定义一个简单的工厂，这是很常见的技巧，常被称为静态工厂。但是使用了静态，就不能通过继承来改变创建实例的行为。

2.重写我们的业务类

```java
public class Test{
    //我们在业务代码里需要一个工厂引用，方便在方法里创建对象实例
    ConnectionFactory factory;
    
    public Test(ConnectionFactory factory){
		this.factory = factory;
    }
    
    public void fun1(String type){
        Connection conn = factory.createBean(type);
        conn.findlist();
		conn.find();
		conn.delete();
		conn.update();
		conn.insert();
    }
}
```

上述使用了简单工厂方法来暖暖身，下面来使用两个重量级的工厂模式。

## 扩展ConnectionFactory

我们之前写的ConnectionFactory，基本适用于所有关系型数据库，那么非关系型数据库呢，他们jdbc的连接方式跟关系型数据库的不一样，那么还有大数据组件，如hbase,hive2等，他们都需要扩展ConnectionFactory。但是呢，我们之前写的connectionFactory扩展性比较差，没有弹性，那么我们应该如何修改呢？

```java
public abstract class ConnectionAbstract{
    public Connection getConnection(String type){
        Connection conn = null;
        conn = createBean(type);
        conn.find();
        //....
    }
    
    public abstract Connection createBean(type);
}
```

我们这么做呢，可以让子类来决定，到底需要实例化哪个Connection

```java
public class RDBConnectionFactory extands ConnectionAbstract{
    public Connection createBean(type){
        Connection conn = null;
        if("mysql".equals(type)){
            conn = new MysqlConnection();
        }else if("oracle".equals(type)){
            conn = new OracleConnection();
        }
        // ...
        return conn;
    }
}

public class NOSQLConnectionFactory extands ConnectionAbstract{
    public Connection createBean(type){
        Connection conn = null;
        if("mongo".equals(type)){
            conn = new MongoConnection();
        }else if("redis".equals(type)){
            conn = new RedisConnection();
        }
        // ...
        return conn;
    }
}

public class BigDataConnectionFactory extands ConnectionAbstract{
    public Connection createBean(type){
        Connection conn = null;
        if("hbase".equals(type)){
            conn = new MongoConnection();
        }else if("hive2".equals(type)){
            conn = new RedisConnection();
        }
        // ...
        return conn;
    }
}
```

getConnection()假设对Connection对象做了很多是，比如获取到了url,username,password等。但是Connection对象是抽象的，getConnection并不知道能获取到哪个连接。这就是解耦了。

createBean是由子类决定的。但实际上还是由业务决定的，业务代码会传一个type过来。

**声明一个工厂方法**

![1545924305680](C:\Users\shao\AppData\Roaming\Typora\typora-user-images\1545924305680.png)

## 一个很依赖的Connection

```java
public class Connection{
    public Connection createBean(String sqlType,String type){
        Connection conn = null;
        if("rdb".equals(sqlType)){
            if("mysql".equals(type)){
                conn = new MysqlConnection();
            }else if(oracle){
                //...
            }
        }else if("nosql".equals(sqlType)){
            if("redis".equals(type)){
                conn = new RedisConnection();
            }else if(mongo){
                //...
            }
        }
        return conn;
    }
}
```

## 依赖倒置原则

**要依赖抽象，不要依赖具体类**

不能让高层组件依赖底层组件，而且，不管高层或底层组件，两者都应该依赖抽象。

下面的指导方针，能帮你避免在OO设计中违反依赖倒置原则：

* 变量不可以持有具体类的引用。
* 不要让类派生具体类。
* 不要覆盖基类中已经实现的方法。

```java
/**
 建造一个工厂来生产原料。
 开始先为工厂定义一个接口，这个接口负责创建所有的原料。
*/
public interface PizzaIngredientFactory{
    public Dough createDough();
    public Sauce createSauce();
    public Cheese createCheese();
    public Veggies[] createVeggies();
    public Pepperoni createPepperoni();
    public Clams createClam();
}

/*
1.为每个区域建造一个工厂。你需要创建一个继承自PizzaIngredientFactory的子类来实现每一个创建方法。
2.实现一组原料类供工厂使用，例如ReggianoCheese,RedPeppers,ThickCrustDough。这些类可以在合适的区域间共享。
3.然后你仍然需要将这一切组织起来，将新的原料工厂整合进旧的PizzaStore代码中。
*/

public class NYPizzaIngredientFactory implements PizzaIngredientFactory{
    public Dough createDough(){
        return new ThinCrustDough();
    }
    
    public Sauce createSauce(){
        return new MarinaraSauce();
    }
    public Cheese createCheese(){
        return new ReggianoCheese();
    }
    public Veggies[] createVeggies(){
        Veggies[] veggies = {new Garlic(),new Onion(),new Mushroom(),new RedPepper()};
        return veggies;
    }
    public Pepperoni createPepperoni(){
        return new SlicedPepperoni();
    }
    public Clams createClam(){
        return new FreshClams();
    }
}

/*
工厂已经一切就绪，准备生产高质量原料了现在我们只需要重做pizza，好让它们只使用工厂生产出来的原料。我们先从抽象的Pizza类开始：

*/


public abstract class Pizza{
    String name;
    Dough dough;
    Sauce sauce;
    Veggies[] Veggies;
    Cheese cheese;
    Pepperoni pepperoni;
    Clams clam;
    
    abstract void prepare();
    
    void bake(){
        System.out.println("Bake for 25 minutes at 350");
    }
    
   	void cut(){
        System.out.println("cutting the pizza into diagonal slices");
    }
    
    void box(){
        System.out.println("place pizza in official PizzaStore box");
    }
    
    void setName(String name){
        this.name = name;
    }
    
    String getName(){
        return this.name;
    }
    
}

public class CheesePizza extends Pizza{
    PizzaIngredientFactory factory;
    
    public CheesePizza(PizzaIngredientFactory factory){
        this.factory = factory;
    }
    
    void prepare(){
    	dough = factory.createDough();
        sauce = factory.createSauce();
        cheess = facory.createCheese();
    }
}


public class ClamPizza extends Pizza{
    PizzaIngredientFactory factory;
    
    public CheesePizza(PizzaIngredientFactory factory){
        this.factory = factory;
    }
    
    void prepare(){
    	dough = factory.createDough();
        sauce = factory.createSauce();
        cheess = facory.createCheese();
        clam = factory.createClam();
    }
}

public class NYPizzaStore extends PizzaStore{
    protected Pizza createPizza(String item){
        Pizza pizza = null;
        PizzaIngredientFactory factory = new NYPizzaIngredientFactory();
        if("cheese".equals(item)){
            pizza = new CheesePizza(factory);
            pizza.setName("cheese");
        }
        return pizza;
    }
}
```

## 我们做了什么？

我们引入了新类型的工厂，也就是所谓的抽象工厂，来创建比萨原料家族。

通过抽象工厂所提供的接口，可以创建产品的家族，利用这个接口书写代码，我们的代码将从实际工厂解耦，一边在不同上下文中实现各式各样的工厂，制造出各种不同的产品。例如：不同的区域，不同的操作系统，不同的外观以及操作。

因为代码从实际的产品中解耦了。我们可以替换不同的工厂来取得不同的行为。

## 抽象工厂模式

提供一个接口，用于创建相关或依赖对象的家族，而不需要明确指定具体类。

抽象工厂允许客户使用抽象的接口来创建一组相关的产品，而不需要知道或关心实际产出的具体产品是什么。这样一来，客户就从具体的产品中被解耦。

## 总结

* 所有的工厂都是用来封装对象的创建
* 简单工厂，虽然不是真正的设计模式，但仍不失为一个简单的方法，可以将客户程序从具体类解耦。
* 工厂方法使用继承：把对象的创建委托给子类。子类实现工厂方法来创建对象。
* 抽象工厂使用对象组合：对象的创建被实现在工厂接口所暴露出来的方法中。
* 所有工厂模式都通过减少应用程序和具体类之间的依赖促进解耦。
* 工厂方法允许类将实例化延迟到子类进行。
* 抽象工厂创建相关的对象家族，而不需要依赖它们的具体类。
* 依赖倒置原则，指导我们避免依赖具体类型，而要尽量依赖抽象。
* 工厂是很有威力的技巧，帮助我们针对抽象编程，而不要针对具体类编程。

![å·¥åæ¨¡å¼ç UML å¾](http://www.runoob.com/wp-content/uploads/2014/08/factory_pattern_uml_diagram.jpg)



![æ½è±¡å·¥åæ¨¡å¼ç UML å¾](http://www.runoob.com/wp-content/uploads/2014/08/abstractfactory_pattern_uml_diagram.jpg)





封装变化：创建逻辑有可能变化，封装成工厂类之后，创建逻辑的变更对调用者透明。

代码复用：创建代码抽离到独立的工厂类之后可以复用。

隔离复杂性：封装复杂的创建逻辑，调用者无需了解如何创建对象。

控制复杂度：将创建代码抽离出来， 让原本的函数或类职责单一，代码更加简洁。

