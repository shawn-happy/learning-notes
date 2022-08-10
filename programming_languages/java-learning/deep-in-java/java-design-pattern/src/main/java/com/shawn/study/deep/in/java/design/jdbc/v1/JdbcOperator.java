package com.shawn.study.deep.in.java.design.jdbc.v1;

import com.shawn.study.deep.in.java.design.jdbc.entity.Coffee;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * v1 jdbc operator class
 *
 * @author shawn
 * @since 2020/8/14
 */
public class JdbcOperator {

  private static final String url =
      "jdbc:mysql://localhost:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
  private static final String username = "root";
  private static final String password = "Gepoint";

  public static void printMsg(String msg) {
    System.out.println("-------------------");
    System.out.println(msg);
  }

  public static void queryAllCoffees() {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      con = DriverManager.getConnection(url, username, password);
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
      try {
        if (rs != null) {
          rs.close();
        }
        if (ps != null) {
          ps.close();
        }
        if (con != null) {
          con.close();
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static Coffee queryCoffeeById(long id) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Coffee coffee = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      con = DriverManager.getConnection(url, username, password);
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
      try {
        if (rs != null) {
          rs.close();
        }
        if (ps != null) {
          ps.close();
        }
        if (con != null) {
          con.close();
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return coffee;
  }

  public static void delete(long id) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      con = DriverManager.getConnection(url, username, password);
      final String sql = "delete from t_coffee where id = ?";
      ps = con.prepareStatement(sql);
      ps.setLong(1, id);
      ps.execute();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
        if (con != null) {
          con.close();
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void save(Coffee coffee) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      con = DriverManager.getConnection(url, username, password);
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
      try {
        if (ps != null) {
          ps.close();
        }
        if (con != null) {
          con.close();
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void update(Coffee coffee) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      con = DriverManager.getConnection(url, username, password);
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
      try {
        if (ps != null) {
          ps.close();
        }
        if (con != null) {
          con.close();
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
