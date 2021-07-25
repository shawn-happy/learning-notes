package com.shawn.study.java.configuration.converter;

public class CharacterConverter extends AbstractConverter<Character> {

  @Override
  protected Character doConvert(String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    return Character.valueOf(value.charAt(0));
  }
}
