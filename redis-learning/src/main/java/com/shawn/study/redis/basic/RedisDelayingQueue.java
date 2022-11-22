package com.shawn.study.redis.basic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import redis.clients.jedis.Jedis;

/** redis 延迟队列 */
public class RedisDelayingQueue<T> {

  private final Jedis jedis;
  private final String key;

  private static final ObjectMapper OM = new ObjectMapper().findAndRegisterModules();
  private final TypeReference<TaskItem<T>> taskType = new TypeReference<TaskItem<T>>() {};

  public RedisDelayingQueue(Jedis jedis, String key) {
    this.jedis = jedis;
    this.key = key;
  }

  public void delay(T msg) throws Exception {
    TaskItem<T> item = new TaskItem<>();
    item.setId(UUID.randomUUID().toString());
    item.setMsg(msg);
    String json = OM.writeValueAsString(item);
    jedis.zadd(this.key, System.currentTimeMillis() + 5000 * 1.0, json);
  }

  public void loop() throws Exception {
    while (!Thread.interrupted()) {
      Set<String> values = jedis.zrangeByScore(this.key, 0.0, System.currentTimeMillis() * 1.0, 0, 1);
      if (null == values || values.isEmpty()) {
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          break;
        }
        continue;
      }
      String s = values.iterator().next();
      if (jedis.zrem(this.key, s) > 0) {
        TaskItem<T> taskItem = OM.readValue(s, taskType);
        System.out.println(taskItem.toString());
      }
    }
  }

  public static class TaskItem<T> {
    private String id;
    private T msg;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public T getMsg() {
      return msg;
    }

    public void setMsg(T msg) {
      this.msg = msg;
    }

    @Override
    public String toString() {
      return "TaskItem{" + "id='" + id + '\'' + ", msg=" + msg + '}';
    }
  }
}
