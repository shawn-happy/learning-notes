package com.shawn.study.java.web.dao;

import com.shawn.study.java.web.db.JdbcTemplate;
import com.shawn.study.java.web.db.RowMapper;
import com.shawn.study.java.web.entity.UserEntity;
import java.util.List;
import javax.annotation.Resource;

public class UserDaoImpl implements UserDao {

  @Resource(name = "bean/jdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  private final RowMapper<UserEntity> userRowMapper =
      (rs, rowNum) -> {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(rs.getInt("id"));
        userEntity.setUsername(rs.getString("username"));
        userEntity.setPassword(rs.getString("password"));
        userEntity.setAge(rs.getInt("age"));
        userEntity.setAddress(rs.getString("address"));
        return userEntity;
      };

  private final RowMapper<Integer> countRowMapper = ((rs, rowNum) -> rs.getInt(1));

  @Override
  public int insert(UserEntity userEntity) {
    final String sql = "INSERT INTO user (username, password, age, address) VALUES (?, ?, ?, ?)";
    final Object[] params = {
      userEntity.getUsername(),
      userEntity.getPassword(),
      userEntity.getAge(),
      userEntity.getAddress()
    };
    return jdbcTemplate.update(sql, params);
  }

  @Override
  public List<UserEntity> findAll() {
    final String sql = "SELECT * FROM user";
    return jdbcTemplate.queryForList(sql, null, userRowMapper);
  }

  @Override
  public UserEntity findById(int id) {
    final String sql = "SELECT * FROM user WHERE id = ?";
    final Object[] params = {id};
    return jdbcTemplate.queryForObject(sql, params, userRowMapper);
  }

  @Override
  public List<UserEntity> findByAddress(String address) {
    final String sql = "SELECT * FROM user WHERE address = ?";
    final Object[] params = {address};
    return jdbcTemplate.queryForList(sql, params, userRowMapper);
  }

  @Override
  public int updatePasswordById(int id, String password) {
    final String sql = "UPDATE user SET password = ? WHERE id = ?";
    final Object[] params = {password, id};
    return jdbcTemplate.update(sql, params);
  }

  @Override
  public int deleteById(int id) {
    final String sql = "DELETE FROM user WHERE id = ?";
    final Object[] params = {id};
    return jdbcTemplate.update(sql, params);
  }

  @Override
  public int count() {
    final String sql = "SELECT COUNT(1) FROM user";
    return jdbcTemplate.queryForObject(sql, null, countRowMapper);
  }
}
