package com.shawn.study.deep.in.java.bean.validation;

import static org.junit.Assert.assertEquals;

import com.shawn.study.deep.in.java.bean.validation.domain.User;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserValidatorTests {

  private static Validator validator;

  @BeforeClass
  public static void init() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void idIsNull() {
    User user = new User(null, "shawn-happy", "shawn-happy");
    Set<ConstraintViolation<User>> validate = validator.validate(user);
    assertEquals(1, validate.size());
    assertEquals("must not be null", validate.iterator().next().getMessage());
  }

  @Test
  public void nameIsTooShort() {
    User user = new User("shawn", "s", "shawn-happy");
    Set<ConstraintViolation<User>> validate = validator.validate(user);
    assertEquals(1, validate.size());
    assertEquals("size must be between 6 and 12", validate.iterator().next().getMessage());
  }

  @Test
  public void passwordIsTooLong() {
    User user = new User("shawn", "shawn-happy", "shawn-happy-shawn-happy-shawn-happy");
    Set<ConstraintViolation<User>> validate = validator.validate(user);
    assertEquals(1, validate.size());
    assertEquals("size must be between 6 and 18", validate.iterator().next().getMessage());
  }

  @Test
  public void userIsValid() {
    User user = new User("shawn", "shawn-happy", "shawn-happy");
    Set<ConstraintViolation<User>> validate = validator.validate(user);
    assertEquals(0, validate.size());
  }
}
