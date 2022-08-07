package com.shawn.study.deep.in.java.design.create.factory.simple;

import java.util.HashMap;
import java.util.Map;

public class ShapeFactory {

	private static Map<String, Shape> shapeMap = new HashMap<>();

	static{
		shapeMap.put("CIRCLE", new Circle());
		shapeMap.put("RECTANGLE",new Rectangle());
		shapeMap.put("SQUARE",new Square());
	}

	/**
	 * use getShape method to get object of type shape
	 */
	public Shape getShape(String shapeType){
		if(shapeType == null){
			return null;
		}
//		if(shapeType.equalsIgnoreCase("CIRCLE")){
//			return new Circle();
//
//		} else if(shapeType.equalsIgnoreCase("RECTANGLE")){
//			return new Rectangle();
//
//		} else if(shapeType.equalsIgnoreCase("SQUARE")){
//			return new Square();
//		}

		return shapeMap.get(shapeType);
	}

}
