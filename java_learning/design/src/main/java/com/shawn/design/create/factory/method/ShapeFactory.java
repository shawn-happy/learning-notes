package com.shawn.design.create.factory.method;

import com.shawn.design.create.factory.simple.Shape;

/**
 * 符合开闭原则
 */
public interface ShapeFactory {

	Shape createShape();

}
