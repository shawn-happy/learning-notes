package com.shawn.study.deep.in.spring.core.ioc.bean.lifecycle;

import com.shawn.study.deep.in.spring.core.ioc.domain.User;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;

public class UserHolder implements SmartInitializingSingleton, InitializingBean, DisposableBean {

  private User user;

  private String description;

  public UserHolder() {}

  public UserHolder(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "UserHolder{" + "user=" + user + ", description='" + description + '\'' + '}';
  }

  @PostConstruct
  public void initPostConstruct() {
    // postProcessBeforeInitialization v3 -> initPostConstruct v4
    System.out.println("postProcessBeforeInitialization v3 -> initPostConstruct v4");
    description = "The user holder V4";
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // initPostConstruct v4 -> afterPropertiesSet v5
    System.out.println("initPostConstruct v4 -> afterPropertiesSet v5");
    description = "The user holder V5";
  }

  public void init() {
    // afterPropertiesSet v5 -> init method v6
    System.out.println("afterPropertiesSet v5 -> init method v6");
    description = "The user holder V6";
  }

  @Override
  public void afterSingletonsInstantiated() {
    System.out.println("postProcessAfterInitialization v7 -> afterSingletonsInstantiated v8");
    description = "The user holder V8";
  }

  @PreDestroy
  public void destroyByPreDestroy() {
    System.out.println("postProcessBeforeDestruction v9 -> destroyByPreDestroy v10");
    description = "The user holder V10";
  }

  @Override
  public void destroy() throws Exception {
    System.out.println("destroyByPreDestroy v10 -> destroy By DisposableBean v11");
    description = "The user holder V11";
  }

  public void destroyMethod() {
    System.out.println("destroy By DisposableBean v11 -> destroy By destroy method v12");
    description = "The user holder V12";
  }
}
