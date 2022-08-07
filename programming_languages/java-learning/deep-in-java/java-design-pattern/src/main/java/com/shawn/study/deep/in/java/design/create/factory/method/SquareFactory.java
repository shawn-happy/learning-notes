package com.shawn.study.deep.in.java.design.create.factory.method;

import com.shawn.study.deep.in.java.design.create.factory.simple.Shape;
import com.shawn.study.deep.in.java.design.create.factory.simple.Square;

public class SquareFactory implements ShapeFactory {

	@Override
	public Shape createShape() {
		return new Square();
	}
}
