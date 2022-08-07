package com.shawn.study.deep.in.java.design.structural.composite;

import java.io.File;

/**
 * @author shawn
 * @description:
 * @since 2020/7/29
 */
public class TestMain {

  public static void main(String[] args) {
    String rootPath = System.getProperty("user.dir");
    DirectoryNode node = new DirectoryNode(rootPath);
    AbstractFileSystemNode compositeNode =
        new FileNode(
            rootPath
                + File.separator
                + "design"
                + File.separator
                + "note"
                + File.separator
                + "structural"
                + File.separator
                + "composite.md");
    node.addSubNode(compositeNode);
    System.out.printf(
        "before remove ---> num of files: %s, size of files: %s\n",
        node.countNumOfFiles(), node.countSizeOfFiles());
    node.removeSubNode(compositeNode);
    System.out.printf(
        "after remove ---> num of files: %s, size of files: %s\n",
        node.countNumOfFiles(), node.countSizeOfFiles());
  }
}
