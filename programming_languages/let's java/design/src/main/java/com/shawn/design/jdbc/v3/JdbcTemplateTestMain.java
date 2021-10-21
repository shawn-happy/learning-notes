package com.shawn.design.jdbc.v3;

import com.shawn.design.jdbc.entity.Coffee;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author shawn
 * @since 2020/8/16
 */
public class JdbcTemplateTestMain {

  public static void main(String[] args) {
    printMsg("test count");
    count();
    printMsg("test select all");
    queryAllCoffees();
    printMsg("test select by id = 10");
    queryCoffeeById(10);
    printMsg("test delete by id = 10");
    delete(10);
    printMsg("test select by id = 10");
    queryCoffeeById(10);
    Date date = new Date();
    Coffee coffee = new Coffee(10, date, date, "IceCoffee", 20);
    printMsg("test insert");
    save(coffee);
    printMsg("test select by id = 2");
    Coffee coffee1 = queryCoffeeById(2);
    coffee1.setUpdateTime(new Date());
    coffee1.setPrice(25);
    printMsg("test update");
    update(coffee1);
    printMsg("test select by id = 2");
    queryCoffeeById(2);
  }

  public static void printMsg(String msg) {
    System.out.println("-------------------");
    System.out.println(msg);
  }

  public static void count() {
    final String sql = "select count(1) from t_coffee";
    List<Long> list = JdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong(1));
    list.forEach(System.out::println);
  }

  public static void queryAllCoffees() {
    final String sql = "select * from t_coffee";
    List<Coffee> list =
        JdbcTemplate.query(
            sql,
            (rs, rowNum) ->
                new Coffee(
                    rs.getLong(1),
                    rs.getTimestamp(2),
                    rs.getTimestamp(3),
                    rs.getString(4),
                    rs.getLong(5)));
    list.forEach(System.out::println);
  }

  public static Coffee queryCoffeeById(long id) {
    final String sql = "select * from t_coffee where id = ?";
    List<Coffee> list =
        JdbcTemplate.query(
            sql,
            (rs, rowNum) ->
                new Coffee(
                    rs.getLong(1),
                    rs.getTimestamp(2),
                    rs.getTimestamp(3),
                    rs.getString(4),
                    rs.getLong(5)),
            id);
    Coffee coffee = list.isEmpty() ? new Coffee() : list.get(0);
    System.out.println(coffee);
    return coffee;
  }

  public static void delete(long id) {
    final String sql = "delete from t_coffee where id = ?";
    JdbcTemplate.update(sql, id);
  }

  public static void save(Coffee coffee) {
    final String sql =
        "INSERT INTO t_coffee (id, create_time, update_time, name, price) VALUES (?,?,?,?,?);";
    JdbcTemplate.update(
        sql,
        coffee.getId(),
        coffee.getCreateTime(),
        coffee.getUpdateTime(),
        coffee.getName(),
        coffee.getPrice());
  }

  public static void update(Coffee coffee) {
    final String sql =
        "UPDATE t_coffee SET create_time = ?, update_time = ?, name = ?, price = ? WHERE id = ?";
    JdbcTemplate.update(
        sql,
        new Timestamp(coffee.getCreateTime().getTime()),
        new Timestamp(coffee.getUpdateTime().getTime()),
        coffee.getName(),
        coffee.getPrice(),
        coffee.getId());
  }
}
