package com.lichkin.framework.springboot.services;

import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.springframework.entities.impl.SysConfigQuartzEntity;

import lombok.Getter;

/**
 * 通用基本任务服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
public abstract class LKBaseDBJobService extends LKBaseTaskDBService implements Job {

	/** 定时任务执行环境上下文 */
	public JobExecutionContext context;


	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		this.context = context;
		executeTask();
	}


	@Autowired
	private SysConfigQuartzService quartzService;


	@Deprecated
	@Override
	protected void doTask() {
		// 查询任务信息
		SysConfigQuartzEntity quartzEntity = quartzService.getOneByClassName(getClass().getSimpleName());

		if (quartzEntity == null) {
			throw new LKFrameworkException("no quartz found.");
		}

		try {
			doTask(LKDateTimeUtils.toDateTime(quartzEntity.getLastExecuteTime()), LKDateTimeUtils.toDateTime(quartzEntity.getLastFinishedTime()));
		} catch (Exception e) {
			logger.error(e);
			return;
		}

		// 记录任务最后一次执行时间
		quartzService.finished(quartzEntity);
	}


	/**
	 * 执行任务具体实现方法
	 * @param lastExecuteTime 最后一次执行时间
	 * @param lastFinishedTime 最后一次完成时间
	 */
	protected abstract void doTask(DateTime lastExecuteTime, DateTime lastFinishedTime);

}
