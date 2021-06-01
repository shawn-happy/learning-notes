package com.shawn.design.create.factory.method;

import com.shawn.design.create.factory.simple.Shape;
import com.shawn.design.create.factory.simple.Square;

public class SquareFactory implements ShapeFactory {

	@Override
	public Shape createShape() {
		return new Square();
	}
}
