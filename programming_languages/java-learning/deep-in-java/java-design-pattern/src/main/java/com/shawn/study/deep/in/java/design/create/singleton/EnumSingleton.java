package com.shawn.study.deep.in.java.design.create.singleton;

/**
 * enum singleton
 *
 * @author com.shawn
 */
public enum EnumSingleton {

  /** 单例模式 */
  INSTANCE;

  public EnumSingleton getInstance() {
    return INSTANCE;
  }
}
