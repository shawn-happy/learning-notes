package com.shawn.study.deep.in.java.jdbc.jpa.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class DefaultJpaRepository<T, ID> implements JPARepository<T, ID> {

  private final Class<T> type;
  private final String primaryKeyName;
  private final Integer batchSize;
  private final String persistenceUnitName;
  private final EntityManagerFactory entityManagerFactory;

  protected DefaultJpaRepository(Class<T> type, String persistenceUnitName) {
    this.type = type;
    this.primaryKeyName = getPkColumn(type);
    this.persistenceUnitName = persistenceUnitName;
    this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    Map<String, Object> properties = entityManagerFactory.getProperties();
    batchSize =
        Integer.parseInt(
            String.valueOf(
                Objects.isNull(properties.get("hibernate.jdbc.batch_size"))
                    ? 5
                    : properties.get("hibernate.jdbc.batch_size")));
  }

  protected EntityManager getEntityManager() {
    return entityManagerFactory.createEntityManager();
  }

  @Override
  public T findById(ID id) {
    return getEntityManager().find(type, id);
  }

  @Override
  public List<T> findAll() {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> query = criteriaBuilder.createQuery(type);
    Root<T> root = query.from(type);
    CriteriaQuery<T> select = query.select(root);
    TypedQuery<T> typedQuery = entityManager.createQuery(select);
    return typedQuery.getResultList();
  }

  @Override
  public List<T> findAllByIds(Collection<ID> ids) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> query = criteriaBuilder.createQuery(type);
    Root<T> root = query.from(type);
    In<Object> in = criteriaBuilder.in(root.get(primaryKeyName));
    for (ID id : ids) {
      in.value(id);
    }
    TypedQuery<T> typedQuery =
        entityManager.createQuery(query.select(root).where(root.get(primaryKeyName).in(ids)));
    return typedQuery.getResultList();
  }

  @Override
  public boolean existsById(ID id) {
    return !Objects.isNull(findById(id));
  }

  @Override
  public long count() {
    EntityManager entityManager = getEntityManager();
    Query query = entityManager.createQuery("select count(t) from " + type.getSimpleName() + " t");
    return Objects.isNull(query.getSingleResult())
        ? 0
        : Long.parseLong(String.valueOf(query.getSingleResult()));
  }

  @Override
  public void save(T entity) {
    EntityManager entityManager = getEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    try {
      entityManager.persist(entity);
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
    }
  }

  @Override
  public int saveAll(Collection<T> entities) {
    if (entities == null || entities.isEmpty()) {
      return 0;
    }
    EntityManager entityManager = getEntityManager();
    List<T> saveEntities = new ArrayList<>(entities);
    int count = 0;
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    try {
      for (int i = 0; i < saveEntities.size(); i++) {
        if (i % batchSize == 0) {
          entityManager.flush();
          entityManager.clear();
        }
        entityManager.persist(saveEntities.get(i));
        count++;
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
    }

    return count;
  }

  @Override
  public int deleteById(ID id) {
    T entity = findById(id);
    if (entity == null) {
      return 0;
    }
    return delete(entity);
  }

  @Override
  public int delete(T entity) {
    EntityManager entityManager = getEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    try {
      entityManager.remove(entityManager.merge(entity));
      entityManager.flush();
      entityManager.clear();
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      return 0;
    }
    return 1;
  }

  @Override
  public int deleteAll(Collection<T> entities) {
    if (entities == null || entities.isEmpty()) {
      return 0;
    }
    int count = 0;
    for (T entity : entities) {
      delete(entity);
      count++;
    }
    return count;
  }

  @Override
  public int update(T newEntity, ID id) {
    EntityManager entityManager = getEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    try {
      entityManager.merge(newEntity);
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      return 0;
    }
    return 1;
  }

  public String getPkColumn(Class<?> entityClass) {
    try {
      Field[] fields = entityClass.getDeclaredFields();
      for (Field field : fields) {
        if (field.getAnnotation(Id.class) != null) {
          return field.getName();
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    throw new RuntimeException();
  }
}
