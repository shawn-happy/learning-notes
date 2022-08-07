package com.shawn.study.deep.in.java.design.ioc;

import com.shawn.study.deep.in.java.design.oop.ArrayList;
import com.shawn.study.deep.in.java.design.oop.List;

/**
 * @author Shawn
 * @description
 * @since 2020/7/6
 */
public class TestApplication {

	private static final List<TestCase> testCases = new ArrayList<>();

	public static void register(TestCase testCase){
		testCases.add(testCase);
	}

	public static void main(String[] args) {
		TestCase kafka = new KafkaTestCase();
		TestCase mysql = new MysqlTestCase();
		register(kafka);
		register(mysql);
		for (int i = 0; i < testCases.size(); i++) {
			TestCase testCase = testCases.get(i);
			testCase.run();
		}
	}
}
