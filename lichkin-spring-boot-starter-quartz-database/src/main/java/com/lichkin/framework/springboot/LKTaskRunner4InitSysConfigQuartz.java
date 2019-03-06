package com.lichkin.framework.springboot;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.db.beans.SysConfigQuartzR;
import com.lichkin.framework.defines.LKConfigStatics;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.springboot.applications.LKTaskDBRunnerWithTransactional;
import com.lichkin.springframework.entities.impl.SysConfigQuartzEntity;

/**
 * 初始化定时任务数据
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKTaskRunner4InitSysConfigQuartz extends LKTaskDBRunnerWithTransactional {

	@Override
	protected void doTask() {
		logger.info("==============================初始化定时任务表==============================");
		final List<LKDBJobInfo> listJob = getJobs();
		if (CollectionUtils.isEmpty(listJob)) {
			throw new LKRuntimeException(LKErrorCodesEnum.CONFIG_ERROR);
		}

		for (final LKDBJobInfo job : listJob) {
			initDebugJob(job);
			initJob(job);
		}
	}


	/**
	 * 初始化任务
	 * @param job 定时任务
	 */
	private void initDebugJob(LKDBJobInfo job) {
		final Class<?> clazz = job.getClazz();
		final String groupName = "DEBUG-" + LKConfigStatics.SYSTEM_TAG;
		final String jobName = clazz.getSimpleName();
		final String className = clazz.getName();
		final String methodName = METHOD_NAME;
		final String cronExpression = job.getCronExpression();
		logger.info("===============初始化定时任务【%s -> %s -> [%s]】===============", groupName, jobName, cronExpression);

		QuerySQL sql = new QuerySQL(SysConfigQuartzEntity.class);
		sql.eq(SysConfigQuartzR.groupName, groupName);
		sql.eq(SysConfigQuartzR.jobName, jobName);
		sql.eq(SysConfigQuartzR.className, className);
		sql.eq(SysConfigQuartzR.methodName, methodName);

		final SysConfigQuartzEntity exist = dao.getOne(sql, SysConfigQuartzEntity.class);

		if (exist == null) {
			// 不存在数据则创建数据
			final SysConfigQuartzEntity entity = new SysConfigQuartzEntity();
			entity.setGroupName(groupName);
			entity.setJobName(jobName);
			entity.setClassName(className);
			entity.setMethodName(methodName);
			entity.setCronExpression(cronExpression);
			dao.persistOne(entity);
			return;
		}

		// 数据内容不同则修改数据内容
		if (!cronExpression.equals(exist.getCronExpression())) {
			exist.setCronExpression(cronExpression);
			dao.mergeOne(exist);
		}
	}


	/**
	 * 初始化任务
	 * @param job 定时任务
	 */
	private void initJob(LKDBJobInfo job) {
		final Class<?> clazz = job.getClazz();
		final String groupName = LKConfigStatics.SYSTEM_TAG;
		final String jobName = clazz.getSimpleName();
		final String className = clazz.getName();
		final String methodName = METHOD_NAME;
		final String cronExpression = job.getCronExpression();
		logger.info("===============初始化定时任务【%s -> %s -> [%s]】===============", groupName, jobName, cronExpression);

		QuerySQL sql = new QuerySQL(SysConfigQuartzEntity.class);
		sql.eq(SysConfigQuartzR.groupName, groupName);
		sql.eq(SysConfigQuartzR.jobName, jobName);
		sql.eq(SysConfigQuartzR.className, className);
		sql.eq(SysConfigQuartzR.methodName, methodName);

		final SysConfigQuartzEntity exist = dao.getOne(sql, SysConfigQuartzEntity.class);

		if (exist == null) {
			// 不存在数据则创建数据
			final SysConfigQuartzEntity entity = new SysConfigQuartzEntity();
			entity.setGroupName(groupName);
			entity.setJobName(jobName);
			entity.setClassName(className);
			entity.setMethodName(methodName);
			entity.setCronExpression(cronExpression);
			dao.persistOne(entity);
			return;
		}

		if (!LKConfigStatics.SYSTEM_DEBUG) {// 非DEBUG状态下才做实际数据修改
			// 数据内容不同则修改数据内容
			if (!cronExpression.equals(exist.getCronExpression())) {
				exist.setCronExpression(cronExpression);
				dao.mergeOne(exist);
			}
		}
	}


	/**
	 * 获取任务列表
	 * @return 任务列表
	 */
	protected abstract List<LKDBJobInfo> getJobs();

}
