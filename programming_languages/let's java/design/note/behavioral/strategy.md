# 策略模式的原理

`Strategy design pattern`

> Define a family of algorithms, encapsulate each one, and make them interchangeable. Strategy lets the algorithm vary independently from clients that use it.
>
> 定义一族算法类，将每个算法分别封装起来，让它们可以相互替换。策略模式可以是算法的变化独立于使用它们的客户端

# 代码模板

```java
public interface Strategy {
  void method();
}

public class ConcreteStrategyA implements Strategy{

  @Override
  public void method() {
    System.out.println("ConcreteStrategyA.method");
  }
}

public class ConcreteStrategyB implements Strategy {

  @Override
  public void method() {
    System.out.println("ConcreteStrategyB.method");
  }
}

public class StrategyFactory {

  private static final Map<String, Strategy> strategies = new HashMap<>();

  static {
    strategies.put("A", new ConcreteStrategyA());
    strategies.put("B", new ConcreteStrategyB());
  }

  public static Strategy getStrategy(String type) {
    if (null == type || type.isEmpty()) {
      throw new IllegalArgumentException("type should not be empty.");
    }
    return strategies.get(type);
  }
}
```