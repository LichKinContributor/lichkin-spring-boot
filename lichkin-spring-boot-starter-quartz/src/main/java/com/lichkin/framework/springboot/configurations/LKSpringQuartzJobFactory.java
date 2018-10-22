package com.lichkin.framework.springboot.configurations;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务工厂类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Component
public class LKSpringQuartzJobFactory extends AdaptableJobFactory {

	@Autowired
	private AutowireCapableBeanFactory factory;


	@Override
	protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
		final Object jobInstance = super.createJobInstance(bundle);
		factory.autowireBean(jobInstance);
		return jobInstance;
	}

}
