package com.shawn.study.deep.in.java.design.structural.decorator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author shawn
 * @description:
 * @since 2020/7/28
 */
public class InputTest {

  public static void main(String[] args) {
    int c = 0;
    String path =
        System.getProperty("user.dir")
            + File.separator
            + "design"
            + File.separator
            + "note"
            + File.separator
            + "structural"
            + File.separator
            + "decorator.txt";
    InputStream in = null;
    try {
      in = new LowerCaseInputStream(new BufferedInputStream(new FileInputStream(path)));
      while ((c = in.read()) != -1) {
        System.out.print((char) c);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
