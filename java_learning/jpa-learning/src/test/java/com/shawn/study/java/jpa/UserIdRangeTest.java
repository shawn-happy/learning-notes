package com.shawn.study.java.jpa;

import com.shawn.study.java.jpa.entity.UserEntity;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.Test;

public class UserIdRangeTest {

  @Test
  public void test_id_range() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    UserEntity userEntity = new UserEntity();
    userEntity.setId(5);
    Set<ConstraintViolation<UserEntity>> validate = validator.validate(userEntity);
    validate.forEach(message -> System.out.println(message.getMessage()));
  }
}
