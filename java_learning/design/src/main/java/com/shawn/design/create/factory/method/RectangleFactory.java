package com.shawn.design.create.factory.method;

import com.shawn.design.create.factory.simple.Rectangle;
import com.shawn.design.create.factory.simple.Shape;

public class RectangleFactory implements ShapeFactory {

	@Override
	public Shape createShape() {
		return new Rectangle();
	}
}
