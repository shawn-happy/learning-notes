package com.shawn.study.deep.in.java.collection.implementations;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class SpecialPurposeImplementationsDemo {

  @Test
  public void demoWeakHashMap() throws InterruptedException {

    // 强引用
    // value 变量是局部变量，存放在栈
    // "abc" 是常量，在 Java 8 之前是放在 Perm 区域，Java 8+ 是存放在 META 区域
    // 在 demoWeakHashMap() 方法执行结束后，value 变量会被立即回收，"abc" 常量常驻
    String value = "abc";

    ReferenceQueue<User> queue = new ReferenceQueue();

    // 弱引用
    WeakReference<User> userReference = new WeakReference<>(new User("张三"), queue);

    // WeakReference 继承为 Reference，Reference 入队 ReferenceQueue
    // 获取引用对象
    for (int i = 0; i < 100; i++) {
      TimeUnit.SECONDS.sleep(1);
      System.out.println(userReference.get());
    }
  }

  private static void demoIdentityHashCodeAndHashCode(Object a, Object b) {

    System.out.printf("System.identityHashCode(%s) == %d \n", a, System.identityHashCode(a));
    System.out.printf("%s.hashCode() == %d \n", a, a.hashCode());

    System.out.printf(
        "System.identityHashCode(%s) == System.identityHashCode(%s) == %s \n",
        a, b, System.identityHashCode(a) == System.identityHashCode(b));

    System.out.printf(
        "%s.hashCode() == %s.hashCode() == %s \n", a, b, a.hashCode() == b.hashCode());
  }

  @Test
  public void testIdentityHashMap() {
    // 如果类覆盖了 Object 的 equals(Object) 方法，那么 hashCode() 方法需不需要覆盖？
    // 说明：不强制覆盖，建议实现，注意不要将 hashCode() 作为 equals 方法的实现，可参考
    // Objects.hash(Object...) 以及 Arrays.hashCode(Object[])，hashCode() 是一个计算较重的实现
    // equals 通常是做对象属性的比较

    // 如果类覆盖了 Object 的 hashCode() ，那么 equals(Object) 方法 方法需不需要覆盖？
    // 说明：必须实现，hashCode() 是用于 Hash 计算，比如普通的 HashMap，由于不同对象的 hashCode() 方法可能返回相同的数据
    // 原因一：int 数据范围 2^31-1，原因二：hashCode() 方法计算问题
    // 当不同对象 hashCode() 相同是，再做对象 equals(Object) 方法比较
    demoHashMap();

    // 场景，需要对对象本身做鉴别
    demoIdentityHashMap();

    // System.identityHashCode() 与 覆盖 hashCode() 方法的差异

    //        Object a = new Object();
    //        demoIdentityHashCodeAndHashCode(a, a);
    //
    //        Object b = new Object();
    //        demoIdentityHashCodeAndHashCode(a, b);

    String string1 = "1";
    String string2 = "1";
    //        demoIdentityHashCodeAndHashCode(string1, string2);

    // 不同对象
    string2 = new String("1");
    demoIdentityHashCodeAndHashCode(string1, string2);
  }

  private static void demoMap(Map<String, Integer> map) {
    System.out.println("A" == new String("A")); // false
    System.out.println("A".equals(new String("A"))); // true
    System.out.println("A".hashCode() == new String("A").hashCode()); // true

    map.put("A", 1);
    map.put(new String("A"), 1);
    System.out.println(map.size());
  }

  private static void demoIdentityHashMap() {
    demoMap(new IdentityHashMap<>());
  }

  private static void demoHashMap() {
    demoMap(new HashMap<>());
  }

  @Test
  public void testPriorityQueue() {
    PriorityQueue<Integer> integerQueue = new PriorityQueue<>();

    integerQueue.add(1);
    integerQueue.add(4);
    integerQueue.add(2);
    integerQueue.add(3);

    integerQueue.forEach(System.out::println);
  }

  private static class User {

    private String name;

    public User(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return "User{" + "name='" + name + '\'' + '}';
    }
  }
}
