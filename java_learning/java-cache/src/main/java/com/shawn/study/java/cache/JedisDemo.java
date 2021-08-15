package com.shawn.study.java.cache;

import java.util.Set;
import redis.clients.jedis.Jedis;

public class JedisDemo {

  public static void main(String[] args) {
    Jedis jedis = new Jedis("172.27.67.41", 6379);
    jedis.auth("paradigm4");

    jedis.set("k1", "v1");
    Set<String> keys = jedis.keys("*");
    System.out.println(keys);
  }
}
