package com.shawn.study.deep.in.java.design.structural.proxy;

import java.util.Map;

/**
 * @author shawn
 * @description 静态代理
 * @since 2020/7/19
 */
public class UserServiceProxy implements UserService{

  private UserService service = new UserServiceImpl();

  @Override
  public Map<String, Object> getUserInfoById(long id) throws Exception {
    long start = System.currentTimeMillis();
    Map<String, Object> userMap = service.getUserInfoById(id);
    long end = System.currentTimeMillis();
    System.out.printf("id: %s, time: %sms\n", id ,(end - start));
    return userMap;
  }
}
