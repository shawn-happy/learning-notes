package com.shawn.study.deep.in.spring.core.type.conversion;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

public class CustomizedPropertyEditorRegistrar implements PropertyEditorRegistrar {

  @Override
  public void registerCustomEditors(PropertyEditorRegistry registry) {
    // 1. 通用类型转换
    // 2. Java Bean 属性类型转换
    registry.registerCustomEditor(User.class, "context", new StringToPropertiesPropertyEditor());
  }
}
