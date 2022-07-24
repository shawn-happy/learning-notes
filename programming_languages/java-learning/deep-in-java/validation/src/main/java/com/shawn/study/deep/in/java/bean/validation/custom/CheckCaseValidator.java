package com.shawn.study.deep.in.java.bean.validation.custom;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckCaseValidator implements ConstraintValidator<CheckCase, String> {

  private CaseMode caseMode;

  @Override
  public void initialize(CheckCase constraintAnnotation) {
    this.caseMode = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
    if (object == null) {
      return true;
    }

    boolean isValid;
    if (caseMode == CaseMode.UPPER) {
      isValid = object.equals(object.toUpperCase());
    } else {
      isValid = object.equals(object.toLowerCase());
    }

    if (!isValid) {
      constraintContext.disableDefaultConstraintViolation();
      constraintContext
          .buildConstraintViolationWithTemplate(
              "{com.shawn.study.deep.in.java.bean.validation.custom.CheckCase.message}")
          .addConstraintViolation();
    }

    return isValid;
  }
}
