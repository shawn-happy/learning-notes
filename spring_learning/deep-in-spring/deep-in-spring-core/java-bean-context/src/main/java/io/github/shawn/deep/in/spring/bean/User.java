package io.github.shawn.deep.in.spring.bean;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;

public class User implements Serializable {
  private int id;
  private String username;
  private String password;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public static void main(String[] args) throws Exception{
    BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
    BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
    MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();

    for (MethodDescriptor methodDescriptor: methodDescriptors){

    }

    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    User user = new User();
    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
      if("class".equals(propertyDescriptor.getName())){
        continue;
      }
      Method readMethod = propertyDescriptor.getReadMethod();
      Method writeMethod = propertyDescriptor.getWriteMethod();
      String name = writeMethod.getName();
      if(name.equals("setUsername")){
        writeMethod.invoke(user, "shawn");
      }

    }

    System.out.println(user.getUsername());
  }
}
