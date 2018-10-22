package com.lichkin.framework.springboot.applications;

import org.springframework.boot.CommandLineRunner;

import com.lichkin.framework.springboot.services.LKBaseTaskDBService;

/**
 * 任务运行启动类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKTaskDBRunner extends LKBaseTaskDBService implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		executeTask();
	}

}