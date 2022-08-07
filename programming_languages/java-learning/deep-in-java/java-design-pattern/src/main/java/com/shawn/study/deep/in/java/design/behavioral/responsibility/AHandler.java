package com.shawn.study.deep.in.java.design.behavioral.responsibility;

/**
 * @author shawn
 * @since 2020/8/8
 */
public class AHandler extends Handler{

  @Override
  public void handle() {
    if(handler != null){
      System.out.println("AHandler.handle");
      handler.handle();
    }
  }
}
