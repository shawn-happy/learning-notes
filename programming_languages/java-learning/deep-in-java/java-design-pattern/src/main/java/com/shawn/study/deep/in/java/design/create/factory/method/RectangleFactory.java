package com.shawn.study.deep.in.java.design.create.factory.method;

import com.shawn.study.deep.in.java.design.create.factory.simple.Rectangle;
import com.shawn.study.deep.in.java.design.create.factory.simple.Shape;

public class RectangleFactory implements ShapeFactory {

	@Override
	public Shape createShape() {
		return new Rectangle();
	}
}
