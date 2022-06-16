package com.shawn.study.deep.in.spring.core.resource;

import java.io.IOException;
import java.io.Reader;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

public class EncodedFileSystemResourceLoaderDemo {

  public static void main(String[] args) throws IOException {
    String currentJavaFilePath =
        "/"
            + System.getProperty("user.dir")
            + "/deep-in-spring-core/resource/src/main/java/com/shawn/study/deep/in/spring/core/resource/EncodedFileSystemResourceLoaderDemo.java";
    // 新建一个 FileSystemResourceLoader 对象
    FileSystemResourceLoader resourceLoader = new FileSystemResourceLoader();
    // FileSystemResource => WritableResource => Resource
    Resource resource = resourceLoader.getResource(currentJavaFilePath);
    EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
    // 字符输入流
    try (Reader reader = encodedResource.getReader()) {
      System.out.println(IOUtils.toString(reader));
    }
  }
}
