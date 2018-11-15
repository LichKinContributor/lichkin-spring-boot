package com.lichkin.framework.springboot.services;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.Getter;

/**
 * 通用基本任务服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
public abstract class LKBaseJobService extends LKBaseTaskService implements Job {

	/** 定时任务执行环境上下文 */
	public JobExecutionContext context;


	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		this.context = context;
		executeTask();
	}

}
