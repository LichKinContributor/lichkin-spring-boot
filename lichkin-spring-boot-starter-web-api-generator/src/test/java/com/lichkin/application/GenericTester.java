package com.lichkin.application;

import org.junit.Test;

import com.lichkin.application.services.TestBusInsertService;

public class GenericTester {

	@Test
	public void test() {
		TestBusInsertService service = new TestBusInsertService();
		System.out.println(service.classSI);
		System.out.println(service.classSO);
		System.out.println(service.classE);
	}

}
