package com.shawn.study.deep.in.java.design.create.prototype;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shawn
 * @description:
 * @since 2020/7/19
 */
public class ColorStore {
  private static Map<String, Color> colorMap = new HashMap<String, Color>();

  static {
    colorMap.put("blue", new BlueColor());
    colorMap.put("black", new BlackColor());
  }

  public static Color getColor(String colorName) {
    return (Color) colorMap.get(colorName).clone();
  }
}
