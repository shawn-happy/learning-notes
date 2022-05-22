package com.shawn.study.deep.in.spring.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.shawn.study.deep.in.spring.core.ioc.domain.SuperUser;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;

public class AssertUser {

  public static void assertUser(User user) {
    assertNotNull(user);
    if (user instanceof SuperUser) {
      System.out.println("user is superUser");
      assertEquals("1234567890", ((SuperUser) user).getIdCard());
    }
    assertEquals("1", user.getId());
    assertEquals("shawn", user.getName());
    assertEquals("shanghai", user.getAddress());
    assertEquals(25, user.getAge());
  }
}
