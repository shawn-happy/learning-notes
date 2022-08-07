package com.shawn.study.deep.in.java.configuration.converter;

public class StringConverter extends GenericConverter<String> {

  public StringConverter() {
    super(String.class);
  }

  @Override
  protected String doConvert(String value) throws Throwable {
    return value;
  }
}
