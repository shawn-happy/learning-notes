package com.shawn.study.deep.in.java.jdbc.jpa.repository;

import com.shawn.study.deep.in.java.jdbc.jpa.entity.UserEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UserRepository extends DefaultJpaRepository<UserEntity, Integer> {

  public UserRepository(String persistenceUnitName) {
    super(UserEntity.class, persistenceUnitName);
  }

  /**
   * jpa type query
   *
   * @param name
   * @return
   */
  public List<UserEntity> findByNameWithTypeQuery(String name) {
    EntityManager entityManager = getEntityManager();
    TypedQuery<UserEntity> typedQuery =
        entityManager.createQuery(
            "SELECT u FROM UserEntity u where u.name = :name", UserEntity.class);
    typedQuery.setParameter("name", name);
    return typedQuery.getResultList();
  }

  /**
   * named query
   *
   * @param phone
   * @return
   */
  public List<UserEntity> findByPhone(String phone) {
    EntityManager entityManager = getEntityManager();
    TypedQuery<UserEntity> nameQuery =
        entityManager.createNamedQuery("findByPhoneNumber", UserEntity.class);
    nameQuery.setParameter("phoneNumber", phone);
    return nameQuery.getResultList();
  }

  /**
   * named query
   *
   * @param email
   * @return
   */
  public List<UserEntity> findByEmail(String email) {
    EntityManager entityManager = getEntityManager();
    TypedQuery<UserEntity> nameQuery =
        entityManager.createNamedQuery("findByEmail", UserEntity.class);
    nameQuery.setParameter("email", email);
    return nameQuery.getResultList();
  }

  /**
   * native sql query
   *
   * @param age
   * @return
   */
  public List<UserEntity> findByAgeGT(int age) {
    EntityManager entityManager = getEntityManager();
    Query nativeQuery =
        entityManager.createNativeQuery("select * from user where age > ?1", UserEntity.class);
    nativeQuery.setParameter(1, age);
    return (List<UserEntity>) nativeQuery.getResultList();
  }

  /**
   * Collection-Valued Positional Parameters
   *
   * @param ids
   * @return
   */
  public List<UserEntity> findByIdIn(List<Integer> ids) {
    EntityManager entityManager = getEntityManager();
    TypedQuery<UserEntity> typedQuery =
        entityManager.createQuery(
            "select u from UserEntity u where u.id in (?1)", UserEntity.class);
    typedQuery.setParameter(1, ids);
    return typedQuery.getResultList();
  }

  /**
   * multiple parameters
   *
   * @param name
   * @param password
   * @return
   */
  public UserEntity findByNameAndPassword(String name, String password) {
    EntityManager entityManager = getEntityManager();
    TypedQuery<UserEntity> typedQuery =
        entityManager.createQuery(
            "select u from UserEntity u where u.name =:name and u.password = :password",
            UserEntity.class);
    typedQuery.setParameter("name", name).setParameter("password", password);
    return typedQuery.getSingleResult();
  }

  /**
   * Criteria Query API
   *
   * @param name
   * @return
   */
  public UserEntity findByNameWithCriteriaQuery(String name) {
    CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
    Root<UserEntity> userRoot = criteriaQuery.from(UserEntity.class);
    return getEntityManager()
        .createQuery(
            criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("name"), name)))
        .getSingleResult();
  }
}
