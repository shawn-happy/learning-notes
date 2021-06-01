package com.shawn.design.create.factory.method;

import com.shawn.design.create.factory.simple.Circle;
import com.shawn.design.create.factory.simple.Shape;

public class CircleFactory implements ShapeFactory {

	@Override
	public Shape createShape() {
		return new Circle();
	}
}
