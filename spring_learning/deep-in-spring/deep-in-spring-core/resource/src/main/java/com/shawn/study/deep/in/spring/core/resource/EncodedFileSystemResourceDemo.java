package com.shawn.study.deep.in.spring.core.resource;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;

public class EncodedFileSystemResourceDemo {

  public static void main(String[] args) throws IOException {
    String currentJavaFilePath =
        System.getProperty("user.dir")
            + "/deep-in-spring-core/resource/src/main/java/com/shawn/study/deep/in/spring/core/resource/EncodedFileSystemResourceDemo.java";
    File currentJavaFile = new File(currentJavaFilePath);
    // FileSystemResource => WritableResource => Resource
    FileSystemResource fileSystemResource = new FileSystemResource(currentJavaFilePath);
    EncodedResource encodedResource = new EncodedResource(fileSystemResource, "UTF-8");
    // 字符输入流
    // 字符输入流
    try (Reader reader = encodedResource.getReader()) {
      System.out.println(IOUtils.toString(reader));
    }
  }
}
