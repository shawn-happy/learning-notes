如果对象在进行可达性分析的时候，发现没有与GC Roots相连的引用链，那么它将会被第一次标记，随后进行一次筛选，筛选的条件是此对象是否有必要执行finalize()方法。如果没有覆盖finalize()方法，或者finalize()方法已经被虚拟机调用过一次，那么虚拟机将这两种情况视为“没有必要执行”。

如果是有必要执行，那么该对象将会被放置在一个名为`F-QUEUE`的队列之中，并在稍后由一条由虚拟机自动建立的，低优先级调用的Finalizer线程去执行他们的finalize()方法。这是即将要被回收的对象有机会复活的唯一机会。

DEMO:

```java
public class FinalizeObjectSurvivorDemo {

  private static FinalizeObjectSurvivorDemo HOOK = null;

  public void isAlive() {
    System.out.println("yes, i'm still alive...");
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    System.out.println("finalize method executed!");
    HOOK = this;
  }

  public static void main(String[] args) throws Exception {
    HOOK = new FinalizeObjectSurvivorDemo();
    HOOK = null;
    // 对象第一次复活
    System.gc();
    // Finalizer Thread是一个低调度优先级的线程，需要暂停1S，确保finalize()方法的执行
    TimeUnit.SECONDS.sleep(1);
    if (HOOK != null) {
      HOOK.isAlive();
    } else {
      System.out.println("no, i'm dead...");
    }

    HOOK = null;
    // 对象被回收了， 因为同一个对象的finalize()只会被虚拟机执行一次
    System.gc();
    // Finalizer Thread是一个低调度优先级的线程，需要暂停1S，确保finalize()方法的执行
    TimeUnit.SECONDS.sleep(1);
    if (HOOK != null) {
      HOOK.isAlive();
    } else {
      System.out.println("no, i'm dead...");
    }
  }

}
```