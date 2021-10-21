# 适配器的定义

`Adapter Design Pattern`：将不兼容的接口转换为可兼容的接口，让原本由于接口不兼容而不能一起工作的类可以一起工作。

实现方式：

* 类适配器（继承）
* 对象适配器（组合）

# 适配器的应用场景

1. 封装有缺陷的接口设计
2. 统一多个类的接口设计
3. 替换依赖的外部系统
4. 兼容老版本的接口
5. 适配不同格式的数据



# show me the code

```java
// 类适配器：基于继承
public interface ITarget{
    void f1();
    void f2();
    void fc();
}

public class Adaptee{
    public void fa(){}
 	public void fb(){}
    public void fc(){}
}

public class Adapter extends Adaptee implements ITarget{
    public void f1(){
        super.fa();
    }
    public void f2(){
        // 重新实现f2
    }
    // 不需要重新实现fc
}

// 对象适配器：基于组合
public interface ITarget{
    void f1();
    void f2();
    void fc();
}

public class Adaptee{
    public void fa(){}
 	public void fb(){}
    public void fc(){}
}

public class Adapter implements ITarget{
    Adaptee adaptee;
    
    public Adapter(Adaptee adaptee){
        this.adaptee = adaptee;
    }
    public void f1(){
        adaptee.fa();
    }
    public void f2(){
        // 重新实现f2
    }
    // 实现fc
    public void fc(){
        adaptee.fc();
    }
}
```

