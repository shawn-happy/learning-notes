package com.shawn.study.deep.in.java.design.structural.composite;

import java.io.File;

/**
 * @author shawn
 * @description:
 * @since 2020/7/29
 */
public class FileNode extends AbstractFileSystemNode {

  public FileNode(String path) {
    super(path);
  }

  @Override
  public int countNumOfFiles() {
    return 1;
  }

  @Override
  public long countSizeOfFiles() {
    File file = new File(path);
    if (!file.exists()) {
      return 0;
    }
    return file.length();
  }
}
