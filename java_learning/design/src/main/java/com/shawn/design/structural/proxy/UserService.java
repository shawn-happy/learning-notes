package com.shawn.design.structural.proxy;

import java.util.Map;

public interface UserService {
  Map<String, Object> getUserInfoById(long id) throws Exception;
}
