package com.shawn.study.deep.in.java.jdbc.jpa.domian;

import java.util.List;

public class Pageable<T> {

  private int pageNum;
  private int pageSize;
  private long totalSize;
  private long totalPage;
  private List<T> data;

  private Pageable() {}

  public static <T> Pageable<T> of(int pageNum, int pageSize, List<T> data) {
    return of(pageNum, pageSize, data, 0, 0);
  }

  public static <T> Pageable<T> of(
      int pageNum, int pageSize, List<T> data, long totalPage, long totalSize) {
    Pageable<T> pageable = new Pageable<>();
    pageable.pageNum = pageNum;
    pageable.pageSize = pageSize;
    pageable.data = data;
    pageable.totalSize = totalSize;
    pageable.totalPage = totalPage;
    return pageable;
  }

  public int getPageNum() {
    return pageNum;
  }

  public int getPageSize() {
    return pageSize;
  }

  public long getTotalSize() {
    return totalSize;
  }

  public long getTotalPage() {
    return totalPage;
  }

  public List<T> getData() {
    return data;
  }
}
