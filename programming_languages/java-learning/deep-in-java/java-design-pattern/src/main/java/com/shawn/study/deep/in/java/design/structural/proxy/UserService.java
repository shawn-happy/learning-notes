package com.shawn.study.deep.in.java.design.structural.proxy;

import java.util.Map;

public interface UserService {
  Map<String, Object> getUserInfoById(long id) throws Exception;
}
