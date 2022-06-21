package com.shawn.study.deep.in.spring.core.type.conversion;

import java.beans.PropertyEditor;

public class PropertyEditorDemo {

  public static void main(String[] args) {

    // 模拟 Spring Framework 操作
    // 有一段文本 name = 小马哥;
    String text = "name = 小马哥";

    PropertyEditor propertyEditor = new StringToPropertiesPropertyEditor();
    // 传递 String 类型的内容
    propertyEditor.setAsText(text);

    System.out.println(propertyEditor.getValue());

    System.out.println(propertyEditor.getAsText());
  }
}
