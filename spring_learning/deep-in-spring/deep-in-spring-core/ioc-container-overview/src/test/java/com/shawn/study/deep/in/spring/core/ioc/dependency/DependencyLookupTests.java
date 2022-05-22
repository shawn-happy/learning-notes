package com.shawn.study.deep.in.spring.core.ioc.dependency;

import static com.shawn.study.deep.in.spring.core.AssertUser.assertUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.shawn.study.deep.in.spring.core.ioc.domain.SuperUser;
import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;

public class DependencyLookupTests {

  private static DependencyLookUpDemo lookup;

  @BeforeClass
  public static void init() {
    lookup = new DependencyLookUpDemo();
  }

  @Test
  public void testLookupByBeanName() {
    User user = lookup.lookupByBeanName();
    assertUser(user);
  }

  @Test
  public void testLookupByBeanType() {
    User user = lookup.lookupByBeanType();
    assertUser(user);
  }

  @Test
  public void testLookupByBeanNameAndType() {
    User user = lookup.lookupByBeanNameAndType();
    assertUser(user);
  }

  @Test
  public void testLookupCollectionType() {
    Map<String, User> userMap = lookup.lookupCollectionType();
    assertNotNull(userMap);
    assertEquals(2, userMap.size());
    assertTrue(userMap.containsKey("user"));
    assertTrue(userMap.containsKey("superUser"));
    User user = userMap.get("user");
    User superUser = userMap.get("superUser");
    assertUser(user);
    assertUser(superUser);
  }

  @Test
  public void testLookupByAnnotation() {
    Map<String, Object> objectMap = lookup.lookupBeansByAnnotation();
    assertNotNull(objectMap);
    assertEquals(1, objectMap.size());
    assertTrue(objectMap.containsKey("superUser"));
    SuperUser superUser = (SuperUser) objectMap.get("superUser");
    assertUser(superUser);
  }

  @Test
  public void testLookupInLazy() {
    User user = lookup.lookupInLazy();
    assertUser(user);
  }
}
