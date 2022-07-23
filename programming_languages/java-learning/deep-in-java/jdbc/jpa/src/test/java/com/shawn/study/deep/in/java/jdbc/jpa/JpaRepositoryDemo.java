package com.shawn.study.deep.in.java.jdbc.jpa;

import com.shawn.study.deep.in.java.jdbc.jpa.entity.Gender;
import com.shawn.study.deep.in.java.jdbc.jpa.entity.UserEntity;
import com.shawn.study.deep.in.java.jdbc.jpa.repository.JPARepository;
import com.shawn.study.deep.in.java.jdbc.jpa.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class JpaRepositoryDemo {

  @Test
  public void test() {
    JPARepository<UserEntity, Integer> userRepository = new UserRepository("emf");
    userRepository.save(newEntity());
    List<UserEntity> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      list.add(newEntity());
    }
    userRepository.saveAll(list);

    UserEntity userEntity = userRepository.findById(1);
    Assert.assertNotNull(userEntity);
    Assert.assertEquals("Shawn", userEntity.getName());

    List<UserEntity> users = userRepository.findAll();
    Assert.assertNotNull(users);
    Assert.assertFalse(users.isEmpty());

    List<UserEntity> userByIds = userRepository.findAllByIds(Arrays.asList(1, 2, 3, 4, 5));
    Assert.assertNotNull(userByIds);
    Assert.assertFalse(userByIds.isEmpty());
    Assert.assertEquals(5, userByIds.size());

    userRepository.deleteById(6);
    UserEntity userAfterDelete = userRepository.findById(6);
    Assert.assertNull(userAfterDelete);

    userRepository.deleteAll(userByIds);
    userByIds = userRepository.findAllByIds(Arrays.asList(1, 2, 3, 4, 5));
    Assert.assertTrue(userByIds.isEmpty());

    long count = userRepository.count();
    System.out.println(count);
    boolean b = userRepository.existsById(1);
    Assert.assertFalse(b);

    UserEntity user7 = userRepository.findById(7);
    user7.setName("Shawn7");
    userRepository.update(user7, 7);
    user7 = userRepository.findById(7);
    Assert.assertEquals("Shawn7", user7.getName());
  }

  private UserEntity newEntity() {
    UserEntity userEntity = new UserEntity();
    userEntity.setName("Shawn");
    userEntity.setPassword("123456");
    userEntity.setEmail("123456@126.com");
    userEntity.setPhoneNumber("12345678901");
    userEntity.setBirth(new Date());
    userEntity.setAge(28);
    userEntity.setGender(Gender.MALE);
    List<String> hobbies = new ArrayList<>();
    for (int i = 0; i < 100000; i++) {
      hobbies.add("hobby_" + i);
    }
    userEntity.setHobbies(hobbies);
    return userEntity;
  }
}
