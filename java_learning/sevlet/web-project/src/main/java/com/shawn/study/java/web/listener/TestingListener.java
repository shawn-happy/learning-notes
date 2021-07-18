package com.shawn.study.java.web.listener;

import com.shawn.study.java.web.context.JndiApplicationContext;
import com.shawn.study.java.web.dao.UserDao;
import com.shawn.study.java.web.entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * unit testing
 *
 * @author Shawn
 * @since 1.0.0
 */
public class TestingListener implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(TestingListener.class.getName());

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    JndiApplicationContext instance = JndiApplicationContext.getInstance();
    if (instance != null) {
      UserDao userDao = instance.getComponent("bean/userDao");
      int count = count(userDao);
      if (count == 0) {
        insertUser(userDao);
      }
      List<UserEntity> userEntities = findAll(userDao);
      count = count(userDao);
      int id = userEntities.get(count - 1).getId();
      findById(userDao, id);
      updatePassword(userDao, id, "112233");
      delete(userDao, id);
      count(userDao);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}

  private void insertUser(UserDao userDao) {
    List<UserEntity> userEntities = new ArrayList<>();
    userEntities.add(new UserEntity("Shawn", "123456", 25, "SHANGHAI"));
    userEntities.add(new UserEntity("Jack", "654321", 24, "BEIJING"));
    userEntities.add(new UserEntity("Bob", "qwerty", 26, "HANGZHOU"));
    int i = 0;
    for (UserEntity userEntity : userEntities) {
      i += userDao.insert(userEntity);
    }
    LOGGER.log(Level.INFO, "Insert the number of successes is " + i);
  }

  private List<UserEntity> findAll(UserDao userDao) {
    List<UserEntity> userEntities = userDao.findAll();
    userEntities.forEach(userEntity -> LOGGER.log(Level.INFO, userEntity.toString()));
    return userEntities;
  }

  private UserEntity findById(UserDao userDao, int id) {
    UserEntity userEntity = userDao.findById(id);
    if (userEntity != null) {
      LOGGER.log(Level.INFO, userEntity.toString());
    }
    return userEntity;
  }

  private void updatePassword(UserDao userDao, int id, String password) {
    UserEntity userEntity = findById(userDao, id);
    if (userEntity != null) {
      int i = userDao.updatePasswordById(id, password);
      LOGGER.log(Level.INFO, "update the number of successes is " + i);
    }
  }

  private void delete(UserDao userDao, int id) {
    int i = userDao.deleteById(id);
    LOGGER.log(Level.INFO, "delete the number of successes is " + i);
  }

  private int count(UserDao userDao) {
    int count = userDao.count();
    LOGGER.log(Level.INFO, "total number of users is " + count);
    return count;
  }
}
