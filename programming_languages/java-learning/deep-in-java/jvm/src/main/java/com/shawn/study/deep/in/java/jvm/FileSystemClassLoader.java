package com.shawn.study.deep.in.java.jvm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSystemClassLoader extends ClassLoader {

  private final String rootDir;

  public FileSystemClassLoader(String rootDir) {
    this.rootDir = rootDir;
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    String path = rootDir + File.separatorChar + name.replace('.', File.separatorChar) + ".class";
    byte[] byteCode = null;
    try (InputStream is = new FileInputStream(path)) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[4096];
      int readSize = 0;
      while ((readSize = is.read(buffer)) != -1) {
        baos.write(buffer, 0, readSize);
      }
      byteCode = baos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if (byteCode == null) {
      throw new ClassNotFoundException(String.format("class name %s", name));
    } else {
      return defineClass(name, byteCode, 0, byteCode.length);
    }
  }

  public static void main(String[] args) throws Exception {
    ClassLoader cl =
        new FileSystemClassLoader(
            "/Users/shaoshuai/dev/workspace/learning-notes/programming_languages/java-learning/deep-in-java/jvm/target/classes");
    Class<?> cls = cl.loadClass("com.shawn.study.deep.in.java.jvm.BitAlgo");
    System.out.println(cls.getSimpleName());
  }
}
