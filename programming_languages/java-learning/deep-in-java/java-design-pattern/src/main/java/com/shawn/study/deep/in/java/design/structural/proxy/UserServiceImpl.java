package com.shawn.study.deep.in.java.design.structural.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author shawn
 * @description:
 * @since 2020/7/19
 */
public class UserServiceImpl implements UserService {

  private static List<Map<String, Object>> userMapList = new ArrayList<>();

  static {
    for (int i = 0; i < 10; i++) {
      Map<String, Object> userMap = new HashMap<>();
      userMap.put("id", i + 1);
      userMap.put("user", "shawn_" + (i + 1));
      userMap.put("age", 20 + i);
      userMapList.add(userMap);
    }
  }

  @Override
  public Map<String, Object> getUserInfoById(long id) throws Exception {
    for (Map<String, Object> userMap : userMapList) {
      Long value = Long.valueOf(userMap.get("id").toString());
      if (value.equals(id)) {
        TimeUnit.SECONDS.sleep(1);
        return userMap;
      }
    }
    return null;
  }
}
