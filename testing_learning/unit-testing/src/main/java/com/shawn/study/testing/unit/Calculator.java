package com.shawn.study.testing.unit;

import com.shawn.study.testing.unit.service.CalculatorService;

public class Calculator {

  private CalculatorService service;

  public Calculator(CalculatorService service) {
    this.service = service;
  }

  public int perform(int i, int j) {
    return service.add(i, j) * i;
  }
}
