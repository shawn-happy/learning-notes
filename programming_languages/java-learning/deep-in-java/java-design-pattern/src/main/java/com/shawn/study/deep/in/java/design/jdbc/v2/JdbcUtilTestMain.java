package com.shawn.study.deep.in.java.design.jdbc.v2;

import com.shawn.study.deep.in.java.design.jdbc.entity.Coffee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

/**
 * v2 test class
 *
 * @author shawn
 * @since 2020/8/16
 */
public class JdbcUtilTestMain {

  public static void main(String[] args) {
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

  public static void queryAllCoffees() {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = JdbcUtil.getConnection();
      final String sql = "select * from t_coffee";
      ps = con.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        long id = rs.getLong(1);
        Timestamp createTime = rs.getTimestamp(2);
        Timestamp updateTime = rs.getTimestamp(3);
        String name = rs.getString(4);
        long price = rs.getLong(5);
        System.out.printf(
            "id: %d, createTime: %s, updateTime: %s, name: %s, price: %d\n",
            id, createTime, updateTime, name, price);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtil.close(con, ps, rs);
    }
  }

  public static Coffee queryCoffeeById(long id) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Coffee coffee = null;
    try {
      con = JdbcUtil.getConnection();
      final String sql = "select * from t_coffee where id = ?";
      ps = con.prepareStatement(sql);
      ps.setLong(1, id);
      rs = ps.executeQuery();
      while (rs.next()) {
        System.out.printf(
            "id: %d, createTime: %s, updateTime: %s, name: %s, price: %d\n",
            rs.getLong(1), rs.getTimestamp(2), rs.getTimestamp(3), rs.getString(4), rs.getLong(5));
        coffee =
            new Coffee(
                rs.getLong(1),
                rs.getTimestamp(2),
                rs.getTimestamp(3),
                rs.getString(4),
                rs.getLong(5));
        break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtil.close(con, ps, rs);
    }
    return coffee;
  }

  public static void delete(long id) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = JdbcUtil.getConnection();
      final String sql = "delete from t_coffee where id = ?";
      ps = con.prepareStatement(sql);
      ps.setLong(1, id);
      ps.execute();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtil.close(con, ps, null);
    }
  }

  public static void save(Coffee coffee) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = JdbcUtil.getConnection();
      final String sql =
          "INSERT INTO t_coffee (id, create_time, update_time, name, price) VALUES (?,?,?,?,?);";
      ps = con.prepareStatement(sql);
      ps.setLong(1, coffee.getId());
      ps.setTimestamp(2, new Timestamp(coffee.getCreateTime().getTime()));
      ps.setTimestamp(3, new Timestamp(coffee.getUpdateTime().getTime()));
      ps.setString(4, coffee.getName());
      ps.setLong(5, coffee.getPrice());
      ps.execute();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtil.close(con, ps, null);
    }
  }

  public static void update(Coffee coffee) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = JdbcUtil.getConnection();
      final String sql =
          "UPDATE t_coffee SET create_time = ?, update_time = ?, name = ?, price = ? WHERE id = ?";
      ps = con.prepareStatement(sql);
      ps.setTimestamp(1, new Timestamp(coffee.getCreateTime().getTime()));
      ps.setTimestamp(2, new Timestamp(coffee.getUpdateTime().getTime()));
      ps.setString(3, coffee.getName());
      ps.setLong(4, coffee.getPrice());
      ps.setLong(5, coffee.getId());
      ps.execute();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtil.close(con, ps, null);
    }
  }
}
