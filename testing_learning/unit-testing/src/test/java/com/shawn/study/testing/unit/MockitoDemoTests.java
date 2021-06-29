package com.shawn.study.testing.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shawn.study.testing.unit.service.CalculatorService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class MockitoDemoTests {

  private Calculator calculator;
  private CalculatorService service = mock(CalculatorService.class);

  @Before
  public void setup() {
    calculator = new Calculator(service);
  }

  @Test
  public void mockito_demo_first() {
    when(service.add(2, 3)).thenReturn(5);
    assertEquals(10, calculator.perform(2, 3));
  }

  @Test
  public void mockito_spy_demo(){
    List<Integer> mock = mock(ArrayList.class);
    when(mock.add(1)).thenReturn(true);
    when(mock.add(2)).thenReturn(true);
    assertEquals(2, mock.size());
  }
}
