package com.shawn.design.structural.composite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shawn
 * @description:
 * @since 2020/7/29
 */
public class FileSystemNode {

  private String path;

  private boolean isFile;

  private List<FileSystemNode> subNodes = new ArrayList<>();

  public FileSystemNode(String path, boolean isFile) {
    this.path = path;
    this.isFile = isFile;
  }

  /**
   * 统计文件总数
   *
   * @return
   */
  public int countNumOfFiles() {
    if (isFile) {
      return 1;
    }
    int num = 0;
    for (FileSystemNode node : subNodes) {
      num += node.countNumOfFiles();
    }
    return num;
  }

  /**
   * 统计文件大小
   *
   * @return
   */
  public long countSizeOfFiles() {
    if (isFile) {
      File f = new File(path);
      if (!f.exists()) {
        return 0;
      }
      return f.length();
    }
    long size = 0;
    for (FileSystemNode node : subNodes) {
      size += node.countSizeOfFiles();
    }
    return size;
  }

  public String getPath() {
    return path;
  }

  public void addSubNode(FileSystemNode node) {
    subNodes.add(node);
  }

  public void removeSubNode(FileSystemNode node) {
    int size = subNodes.size();
    int i = 0;
    while (i < size) {
      if (subNodes.get(i).getPath().equalsIgnoreCase(node.getPath())) {
        break;
      }
    }
    if (i < size) {
      subNodes.remove(i);
    }
  }
}
