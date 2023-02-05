package com.shawn.study.deep.in.spring.aop.service;

public interface EchoService {

  String echo(String msg) throws NullPointerException;

  String display(String msg);
}
