package com.shawn.study.deep.in.java.design.create.factory.method;

import com.shawn.study.deep.in.java.design.create.factory.simple.Shape;

/** 符合开闭原则 */
public interface ShapeFactory {

  Shape createShape();
}
