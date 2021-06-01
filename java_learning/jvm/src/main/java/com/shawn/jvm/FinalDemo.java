package com.shawn.jvm;

public class FinalDemo {

	public static void main(String[] args) {
		System.out.println(Parent.STR);
	}

}

class Parent{
	public static final String STR = GenerateString.str();
	static {
		System.out.println("parent static code");
	}
}

class GenerateString{
	public static String str(){
		return "hello world";
	}
}