package com.shawn.study.java.web.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class JdbcTemplate {

  private static final Logger LOGGER = Logger.getLogger(JdbcTemplate.class.getName());

  private Connection connection;

  @Resource(name = "jdbc/datasource")
  private DataSource dataSource;

  @PostConstruct
  public void init() {
    if (dataSource != null) {
      try {
        connection = dataSource.getConnection();
      } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, e.getMessage());
        throw new RuntimeException(e);
      }
    }
  }

  public JdbcTemplate() {}

  @PreDestroy
  public void close() {
    close(connection);
  }

  public <T> T execute(final String sql) {
    return execute(
        statement -> {
          statement.execute(sql);
          return null;
        });
  }

  public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> callback) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = psc.createPreparedStatement(connection);
      return callback.doInPreparedStatement(preparedStatement);
    } catch (SQLException e) {
      throw new RuntimeException("preparedStatement callback function failed to execute", e);
    } finally {
      close(preparedStatement);
    }
  }

  public <T> T execute(StatementCallback<T> callback) {
    Statement statement = null;
    try {
      statement = connection.createStatement();
      return callback.doInStatement(statement);
    } catch (SQLException e) {
      throw new RuntimeException("statement callback function failed to execute", e);
    } finally {
      close(statement);
    }
  }

  public <R> List<R> executeQuery(final String sql, RowMapper<R> rowMapper) {
    return executeQuery(sql, null, rowMapper);
  }

  public <R> List<R> executeQuery(final String sql, Object[] params, RowMapper<R> rowMapper) {
    return execute(
        preparedStatementCreate(sql, params),
        statement -> {
          ResultSet resultSet = null;
          try {
            resultSet = statement.executeQuery();
            return new RowMapperResultSetConvertor<>(rowMapper).convert(resultSet);
          } catch (SQLException e) {
            throw new RuntimeException("Unable to map column to java property", e);
          } finally {
            close(resultSet);
          }
        });
  }

  public <R> List<R> queryForList(final String sql, Object[] params, RowMapper<R> rowMapper) {
    return executeQuery(sql, params, rowMapper);
  }

  public <R> R queryForObject(final String sql, Object[] params, RowMapper<R> rowMapper) {
    List<R> rs = queryForList(sql, params, rowMapper);
    if (Objects.isNull(rs) || rs.size() == 0) {
      throw new RuntimeException(
          String.format("Incorrect result size: expected [%d], actual [%d]", 1, 0));
    } else if (rs.size() > 1) {
      throw new RuntimeException(
          String.format("Incorrect result size: expected [%d], actual [%d]", 1, rs.size()));
    }
    return rs.iterator().next();
  }

  public int update(final String sql, Object[] params) {
    return executeForDML(preparedStatementCreate(sql, params), PreparedStatement::executeUpdate);
  }

  public int executeForDML(
      PreparedStatementCreator psc, PreparedStatementCallback<Integer> callback) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = psc.createPreparedStatement(connection);
      return callback.doInPreparedStatement(preparedStatement);
    } catch (SQLException e) {
      throw new RuntimeException("preparedStatement callback function failed to execute", e);
    } finally {
      close(preparedStatement);
    }
  }

  private PreparedStatementCreator preparedStatementCreate(String sql, Object[] params) {
    return con -> {
      PreparedStatement preparedStatement = con.prepareStatement(sql);
      if (!Objects.isNull(params) && params.length != 0) {
        for (int i = 0; i < params.length; i++) {
          StatementCreatorUtils.setSqlParamValue(preparedStatement, (i + 1), params[i]);
        }
      }
      return preparedStatement;
    };
  }

  private void close(AutoCloseable closeable) {
    try {
      if (closeable != null) {
        closeable.close();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
