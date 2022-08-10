package com.shawn.study.deep.in.java.design.create.factory.method;

import com.shawn.study.deep.in.java.design.create.factory.simple.Circle;
import com.shawn.study.deep.in.java.design.create.factory.simple.Shape;

public class CircleFactory implements ShapeFactory {

  @Override
  public Shape createShape() {
    return new Circle();
  }
}
