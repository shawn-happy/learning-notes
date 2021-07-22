package com.shawn.study.java.jpa;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdRangeConstraintValidator implements ConstraintValidator<IdRange, Integer> {

  private int min;
  private int max;

  @Override
  public void initialize(IdRange idRange) {
    min = idRange.min();
    max = idRange.max();
  }

  @Override
  public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
    constraintValidatorContext.buildConstraintViolationWithTemplate(
        String.format(constraintValidatorContext.getDefaultConstraintMessageTemplate(), min, max));
    return (id != null) && (min <= id && id <= max);
  }
}
