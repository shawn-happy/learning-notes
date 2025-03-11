package io.github.shawn.deep.in.spring.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.beancontext.BeanContext;
import java.beans.beancontext.BeanContextServicesSupport;
import java.beans.beancontext.BeanContextSupport;

public class UserBeanContext {
  public static void main(String[] args){
    BeanContextServicesSupport beanContextServicesSupport = new BeanContextServicesSupport();

    BeanContext beanContext = new BeanContextSupport();
    User user = new User();
    user.setId(1);
    user.setUsername("Shawn");

    beanContextServicesSupport.add(user);

    beanContext.addPropertyChangeListener("username", new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("old: " + evt.getOldValue());
        System.out.println("new: " + evt.getNewValue());
      }
    });
    beanContext.add(user);
    user.setUsername("shao");
    User userBean = (User)beanContext.iterator().next();
    System.out.println(userBean.getUsername());
  }
}
