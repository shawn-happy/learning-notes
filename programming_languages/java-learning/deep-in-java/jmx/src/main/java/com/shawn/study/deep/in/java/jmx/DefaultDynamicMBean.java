package com.shawn.study.deep.in.java.jmx;

import java.util.HashMap;
import java.util.Map;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

public class DefaultDynamicMBean implements DynamicMBean {

  private Map<String, Object> attributeMap = new HashMap<>();

  @Override
  public Object getAttribute(String attribute)
      throws AttributeNotFoundException, MBeanException, ReflectionException {
    if (!attributeMap.containsKey(attribute)) {
      throw new AttributeNotFoundException(
          String.format("this attribute [%s] not found!", attribute));
    }
    return attributeMap.get(attribute);
  }

  @Override
  public void setAttribute(Attribute attribute)
      throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException,
          ReflectionException {
    attributeMap.put(attribute.getName(), attribute.getValue());
  }

  @Override
  public AttributeList getAttributes(String[] attributes) {
    AttributeList attributeList = new AttributeList();
    for (String attribute : attributes) {
      try {
        Object value = getAttribute(attribute);
        attributeList.add(new Attribute(attribute, value));
      } catch (AttributeNotFoundException | MBeanException | ReflectionException e) {
        throw new RuntimeException(e);
      }
    }
    return attributeList;
  }

  @Override
  public AttributeList setAttributes(AttributeList attributes) {
    for (Object object : attributes) {
      if (object instanceof Attribute) {
        Attribute attribute = (Attribute) object;
        try {
          setAttribute(attribute);
        } catch (AttributeNotFoundException
            | InvalidAttributeValueException
            | MBeanException
            | ReflectionException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return attributes;
  }

  @Override
  public Object invoke(String actionName, Object[] params, String[] signature)
      throws MBeanException, ReflectionException {
    return null;
  }

  @Override
  public MBeanInfo getMBeanInfo() {
    return null;
  }
}
