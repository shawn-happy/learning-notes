package com.shawn.study.deep.in.java.bean.validation.domain;

import com.shawn.study.deep.in.java.bean.validation.custom.CaseMode;
import com.shawn.study.deep.in.java.bean.validation.custom.CheckCase;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Car {

  @NotNull private String manufacturer;

  @NotNull
  @Size(min = 2, max = 14)
  @CheckCase(CaseMode.UPPER)
  private String licensePlate;

  @Min(2)
  private int seatCount;

  public Car(String manufacturer, String licencePlate, int seatCount) {
    this.manufacturer = manufacturer;
    this.licensePlate = licencePlate;
    this.seatCount = seatCount;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public String getLicensePlate() {
    return licensePlate;
  }

  public int getSeatCount() {
    return seatCount;
  }
}
