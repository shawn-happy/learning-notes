package com.shawn.study.java.jpa;

import com.shawn.study.java.jpa.entity.UserEntity;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class BeanValidationDemo {

  public static void main(String[] args) {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    UserEntity userEntity = new UserEntity();
    userEntity.setPassword("123456");
    Set<ConstraintViolation<UserEntity>> validate = validator.validate(userEntity);
    validate.forEach(message -> System.out.println(message.getMessage()));
  }
}
