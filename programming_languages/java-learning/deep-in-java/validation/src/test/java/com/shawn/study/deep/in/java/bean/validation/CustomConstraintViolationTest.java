package com.shawn.study.deep.in.java.bean.validation;

import static org.junit.Assert.assertEquals;

import com.shawn.study.deep.in.java.bean.validation.domain.Car;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#validator-gettingstarted
 */
public class CustomConstraintViolationTest {

  private static Validator validator;

  @BeforeClass
  public static void init() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void test() {
    // invalid license plate
    Car car = new Car("Morris", "dd-ab-123", 4);
    Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
    assertEquals(1, constraintViolations.size());
    assertEquals("Case mode must be UPPER.", constraintViolations.iterator().next().getMessage());

    // valid license plate
    car = new Car("Morris", "DD-AB-123", 4);

    constraintViolations = validator.validate(car);

    assertEquals(0, constraintViolations.size());
  }
}
