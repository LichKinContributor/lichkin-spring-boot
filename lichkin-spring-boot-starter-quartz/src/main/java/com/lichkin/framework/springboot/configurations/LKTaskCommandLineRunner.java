package com.lichkin.framework.springboot.configurations;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.lichkin.framework.defines.LKFramework;
import com.lichkin.framework.springboot.beans.LKJob;
import com.lichkin.framework.springboot.services.LKTaskInitializer;

/**
 * 任务启动类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Configuration
@Order(value = 1000)
public class LKTaskCommandLineRunner extends LKFramework implements CommandLineRunner {

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private LKTaskInitializer taskInitializer;


	@Override
	public void run(String... args) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("►►►►►►►►►►►►►►►►►►LKTaskRunner4SysConfigQuartz◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄");
		}

		LKQuartzManager qm = LKQuartzManager.getInstance();

		qm.initScheduler(scheduler);

		List<LKJob> list = new ArrayList<>();

		taskInitializer.init(list);

		if (CollectionUtils.isNotEmpty(list)) {
			for (LKJob bean : list) {
				qm.scheduleJob(bean.isSimple(), bean.getGroupName(), bean.getJobName(), bean.getClassName(), bean.getMethodName(), bean.getCronExpression(), bean.getParams());
			}
		}

		qm.start();
	}

}
