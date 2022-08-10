package com.shawn.study.deep.in.java.design.structural.proxy;

/**
 * @author shawn
 * @description:
 * @since 2020/7/19
 */
public class TestMain {

  public static void main(String[] args) throws Exception {
    UserService service = new UserServiceProxy();
    service.getUserInfoById(1L);

    DynamicUserServiceProxy dynamicUserServiceProxy = new DynamicUserServiceProxy();
    UserService userService =
        (UserService) dynamicUserServiceProxy.createProxy(new UserServiceImpl());
    userService.getUserInfoById(2);
  }
}
