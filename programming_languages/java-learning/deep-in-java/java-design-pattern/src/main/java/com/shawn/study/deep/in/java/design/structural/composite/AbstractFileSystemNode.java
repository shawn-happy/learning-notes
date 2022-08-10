package com.shawn.study.deep.in.java.design.structural.composite;

/**
 * @author shawn
 * @description:
 * @since 2020/7/29
 */
public abstract class AbstractFileSystemNode {

  protected String path;

  public AbstractFileSystemNode(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public abstract int countNumOfFiles();

  public abstract long countSizeOfFiles();
}
