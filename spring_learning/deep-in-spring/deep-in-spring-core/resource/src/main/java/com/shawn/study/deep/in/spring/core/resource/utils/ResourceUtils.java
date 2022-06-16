package com.shawn.study.deep.in.spring.core.resource.utils;

import java.io.IOException;
import java.io.Reader;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

public class ResourceUtils {

  public static String getContent(Resource resource) {
    try {
      return getContent(resource, "UTF-8");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getContent(Resource resource, String encoding) throws IOException {
    EncodedResource encodedResource = new EncodedResource(resource, encoding);
    // 字符输入流
    try (Reader reader = encodedResource.getReader()) {
      return IOUtils.toString(reader);
    }
  }
}
