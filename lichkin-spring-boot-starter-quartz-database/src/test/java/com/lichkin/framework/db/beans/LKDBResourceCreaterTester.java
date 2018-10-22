package com.lichkin.framework.db.beans;

import java.io.IOException;

import org.junit.Test;

public class LKDBResourceCreaterTester {

	@Test
	public void test() throws IOException {
		LKDBResourceCreater.createRFiles("Quartz", false);
	}

}
