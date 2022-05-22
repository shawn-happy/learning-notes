package com.shawn.study.deep.in.spring.core.ioc.dependency;

import static com.shawn.study.deep.in.spring.core.AssertUser.assertUser;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import com.shawn.study.deep.in.spring.core.ioc.repository.UserRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class DependencyInjectionTests {

  private static DependencyInjectionDemo injection;

  @BeforeClass
  public static void init() {
    injection = new DependencyInjectionDemo();
  }

  @Test
  public void testInjectionWithCode() {
    UserRepository userRepository = injection.injectWithCode();
    assertNotNull(userRepository);
    User[] users = userRepository.getUsers();
    assertNotNull(users);
    for (User user : users) {
      assertUser(user);
    }
    BeanFactory beanFactory = userRepository.getBeanFactory();
    assertNull(beanFactory);

    Environment environment = userRepository.getEnvironment();
    assertNull(environment);

    ObjectFactory<ApplicationContext> applicationContextObjectFactory =
        userRepository.getObjectFactory();
    assertNull(applicationContextObjectFactory);

    ObjectFactory<User> userObjectFactory = userRepository.getUserObjectFactory();
    assertNull(userObjectFactory);
  }

  @Test
  public void testInjectionWithAutoWiring() {
    UserRepository userRepository = injection.injectWithAutoWiring();
    assertNotNull(userRepository);
    User[] users = userRepository.getUsers();
    assertNotNull(users);
    for (User user : users) {
      assertUser(user);
    }
    BeanFactory beanFactory = userRepository.getBeanFactory();
    assertNotNull(beanFactory);

    Environment environment = userRepository.getEnvironment();
    assertNotNull(environment);

    ObjectFactory<ApplicationContext> applicationContextObjectFactory =
        userRepository.getObjectFactory();
    assertNotNull(applicationContextObjectFactory);

    ObjectFactory<User> userObjectFactory = userRepository.getUserObjectFactory();
    assertNotNull(userObjectFactory);
  }
}
