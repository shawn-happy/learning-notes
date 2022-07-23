package com.shawn.study.deep.in.java.jdbc.jpa.repository;

import java.util.Collection;
import java.util.List;

public interface JPARepository<T, ID> {

  T findById(ID id);

  List<T> findAll();

  List<T> findAllByIds(Collection<ID> ids);

  boolean existsById(ID id);

  long count();

  void save(T entity);

  int saveAll(Collection<T> entities);

  int deleteById(ID id);

  int delete(T entity);

  int deleteAll(Collection<T> entities);

  int update(T newEntity, ID id);
}
