package com.shawn.study.redis.basic;

import java.util.concurrent.TimeUnit;
import redis.clients.jedis.Jedis;

public class TestMain {

  /**
   * @param args host port password
   */
  public static void main(String[] args) throws Exception {
    Jedis jedis = new Jedis(args[0], Integer.parseInt(args[1]));
    if (args.length == 3) {
      jedis.auth(args[2]);
    }
    testDistributeLock(jedis);
    testDelayQueue(jedis);
    testHyperLogLog(jedis);
    jedis.close();
  }

  private static void testDistributeLock(Jedis jedis) {
    RedisDistributeLock lock = new RedisDistributeLock(jedis);
    System.out.println(lock.lock("shawn:lock"));
    System.out.println(lock.lock("shawn:lock"));
    System.out.println(lock.unlock("shawn:lock"));
    System.out.println(lock.unlock("shawn:lock"));
  }

  private static void testDelayQueue(Jedis jedis) throws Exception {
    String key = "queue-demo";
    RedisDelayingQueue<String> queue = new RedisDelayingQueue<>(jedis, key);
    try {
      Thread producer =
          new Thread(
              () -> {
                try {
                  for (int i = 0; i < 100; i++) {
                    queue.delay("msg" + i);
                  }
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              });
      Thread consumer =
          new Thread(
              () -> {
                try {
                  queue.loop();
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              });

      producer.start();
      consumer.start();

      producer.join();
      TimeUnit.SECONDS.sleep(6);
      consumer.interrupt();
      consumer.join();
    } finally {
      jedis.del(key);
    }
  }

  private static void testHyperLogLog(Jedis jedis){
    String key = "pf-demo";
    for (int i = 0; i < 100000; i++) {
      jedis.pfadd(key, "user" + i);
    }
    long pfcount = jedis.pfcount(key);
    System.out.printf("%d %d \n", 100000, pfcount);
    jedis.del(key);
  }
}
