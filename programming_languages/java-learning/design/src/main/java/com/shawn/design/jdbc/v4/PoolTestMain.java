package com.shawn.design.jdbc.v4;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shawn
 * @since 2020/8/21
 */
public class PoolTestMain {

  private static ExecutorService executorService = Executors.newFixedThreadPool(10);

  private static AtomicInteger atomicInteger = new AtomicInteger();

  public static void main(String[] args) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    for (int i = 0; i < 500; i++) {
      executorService.submit(() -> new PoolTestMain().queryAllCoffees(jdbcTemplate));
    }
    executorService.shutdown();
  }

  public synchronized void queryAllCoffees(JdbcTemplate jdbcTemplate) {
    final String sql = "select * from t_coffee";
    List<Coffee> coffees = jdbcTemplate.queryList(sql, Coffee.class);
    System.out.println("第" + atomicInteger.incrementAndGet() + "次");
    coffees.forEach(System.out::println);
  }
}
