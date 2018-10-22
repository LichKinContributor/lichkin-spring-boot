package com.lichkin.framework.springboot.configurations;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时任务配置类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Configuration
public class LKSpringBootConfiguration4Quartz {

	@Autowired
	private LKSpringQuartzJobFactory factory;


	/**
	 * 创建定时任务工厂类
	 * @return 定时任务工厂类
	 */
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		final SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setJobFactory(factory);
		return schedulerFactoryBean;
	}


	/**
	 * 创建定时任务计划类
	 * @return 定时任务计划类
	 */
	@Bean
	public Scheduler scheduler() {
		return schedulerFactoryBean().getScheduler();
	}

}
