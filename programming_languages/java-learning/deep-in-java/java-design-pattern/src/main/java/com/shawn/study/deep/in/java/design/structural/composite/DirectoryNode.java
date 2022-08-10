package com.shawn.study.deep.in.java.design.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shawn
 * @description:
 * @since 2020/7/29
 */
public class DirectoryNode extends AbstractFileSystemNode {

  private List<AbstractFileSystemNode> subNodes = new ArrayList<>();

  public DirectoryNode(String path) {
    super(path);
  }

  @Override
  public int countNumOfFiles() {
    int num = 0;
    for (AbstractFileSystemNode node : subNodes) {
      num += node.countNumOfFiles();
    }
    return num;
  }

  @Override
  public long countSizeOfFiles() {
    long size = 0;
    for (AbstractFileSystemNode node : subNodes) {
      size += node.countSizeOfFiles();
    }
    return size;
  }

  public void addSubNode(AbstractFileSystemNode node) {
    subNodes.add(node);
  }

  public void removeSubNode(AbstractFileSystemNode node) {
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
