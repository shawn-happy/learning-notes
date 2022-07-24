package com.shawn.study.deep.in.java.web.orm.jpa;

import com.shawn.study.deep.in.java.web.entity.LoginUser;
import com.shawn.study.deep.in.java.web.orm.repository.UserRepository;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class UserRepositoryImplWithJpa implements UserRepository {

  private static final Logger LOGGER = Logger.getLogger(UserRepositoryImplWithJpa.class.getName());

  @Resource(name = "bean/EntityManager")
  private EntityManager entityManager;

  @Override
  public void insert(LoginUser userEntity) {
    entityManager.persist(userEntity);
  }

  @Override
  public LoginUser findById(int id) {
    return entityManager.find(LoginUser.class, id);
  }

  @Override
  public LoginUser findByNameAndPassword(String name, String password) {
    TypedQuery<LoginUser> typedQuery =
        entityManager.createNamedQuery("findByNameAndPassword", LoginUser.class);
    typedQuery.setParameter("username", name).setParameter("password", password);
    return typedQuery.getSingleResult();
  }
}
