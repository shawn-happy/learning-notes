package com.shawn.study.deep.in.java.concurrency.sdk;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureDemo1 {

  public static void main(String[] args) throws Exception {
    // serial();
    // and();
    // or();
    // exception();
    all();
    any();
  }

  // 描述串行关系
  // 期待输出
  // hello world
  // Hello CompletableFuture
  private static void serial() throws Exception {
    CompletableFuture<Void> t1 =
        CompletableFuture.supplyAsync(() -> "Hello")
            .thenApply(s -> s + " World")
            .thenApply(String::toLowerCase)
            .thenAccept(System.out::println);

    CompletableFuture<Void> t2 =
        CompletableFuture.supplyAsync(() -> "Hello")
            .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " CompuletableFuture"))
            .thenAccept(System.out::println);

    t1.thenRun(() -> t2.join());
    t1.join();
  }

  /*
   期待输出：
   hello world
   Hello CompuletableFuture
   thenCombine end
   thenAcceptBoth end
   runAfterBoth end
  */
  private static void and() throws Exception {
    CompletableFuture<Void> t1 =
        CompletableFuture.supplyAsync(() -> "Hello")
            .thenApply(s -> s + " World")
            .thenApply(String::toLowerCase)
            .thenAccept(System.out::println);

    CompletableFuture<Void> t2 =
        CompletableFuture.supplyAsync(() -> "Hello")
            .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " CompuletableFuture"))
            .thenAccept(System.out::println);

    CompletableFuture<String> t3 = t1.thenCombine(t2, (__, s) -> "end");
    t3.thenAccept(s -> System.out.println("thenCombine " + s));
    t3.join();

    CompletableFuture<Void> t4 =
        t1.thenAcceptBoth(t2, (__, s) -> System.out.println("thenAcceptBoth end"));
    t4.join();

    CompletableFuture<Void> t5 = t1.runAfterBoth(t2, () -> System.out.println("runAfterBoth end"));
    t5.join();
  }

  private static void or() {
    CompletableFuture<Void> t1 =
        CompletableFuture.runAsync(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("t1");
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });

    CompletableFuture<Void> t2 =
        CompletableFuture.runAsync(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(200);
                System.out.println("t2");
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });

    CompletableFuture<String> t3 = t1.applyToEither(t2, s -> "applyToEither end");
    t3.thenAccept(System.out::println);
    t3.join();

    CompletableFuture<Void> t4 = t1.acceptEither(t2, s -> System.out.println("acceptEither end"));
    t4.join();

    CompletableFuture<Void> t5 =
        t1.runAfterEither(t2, () -> System.out.println("runAfterEither end"));
    t5.join();
  }

  private static void exception() {
    int i = 0;
    CompletableFuture<String> t1 =
        CompletableFuture.supplyAsync(
            () -> {
              System.out.println("t1");
              if (i > 0) {
                return "t1";
              }
              throw new RuntimeException("error");
            });

    //    CompletableFuture<Void> t2 =
    //        t1.exceptionally(e -> e.getMessage())
    //            .thenAccept(System.out::println)
    //            .thenRun(() -> System.out.println("exceptionally end"));
    //    t2.join();

    //    CompletableFuture<String> t3 =
    //        t1.whenComplete(
    //                (s, e) -> {
    //                  if (e != null) {
    //                    System.out.println(e.getMessage());
    //                  } else {
    //                    System.out.println(s);
    //                  }
    //                });
    //    t3.join();

    CompletableFuture<Void> t4 =
        t1.handle(
                (s, e) -> {
                  if (e != null) {
                    return e.getMessage();
                  }
                  return s;
                })
            .thenAccept(System.out::println);
    t4.join();
  }

  private static void all() {
    long startTime = System.currentTimeMillis();
    CompletableFuture<Void> t1 =
        CompletableFuture.runAsync(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("t1");
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });

    CompletableFuture<Void> t2 =
        CompletableFuture.runAsync(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(15);
                System.out.println("t2");
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });
    CompletableFuture<Void> t3 =
        CompletableFuture.allOf(t1, t2)
            .thenRun(
                () ->
                    System.out.println(
                        "all end cost " + (System.currentTimeMillis() - startTime) + " ms."));
    t3.join();
  }

  private static void any() {
    long startTime = System.currentTimeMillis();
    CompletableFuture<Void> t1 =
        CompletableFuture.runAsync(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("t1");
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });

    CompletableFuture<Void> t2 =
        CompletableFuture.runAsync(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(15);
                System.out.println("t2");
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });

    CompletableFuture<Void> t3 =
        CompletableFuture.anyOf(t1, t2)
            .thenRun(
                () ->
                    System.out.println(
                        "all end cost " + (System.currentTimeMillis() - startTime) + " ms."));
    t3.join();
  }
}
