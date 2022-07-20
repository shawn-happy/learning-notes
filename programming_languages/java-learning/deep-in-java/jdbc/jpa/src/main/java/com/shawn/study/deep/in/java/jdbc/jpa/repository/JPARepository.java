package com.shawn.study.deep.in.java.jdbc.jpa.repository;

import java.util.List;

public interface JPARepository<T, ID> {

  T findById(ID id);

  List<T> findAll();

  int save(T entity);

  int deleteById(ID id);

  int update(T newEntity, ID id);
}
