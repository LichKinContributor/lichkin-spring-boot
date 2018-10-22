package com.lichkin.framework.springboot.configurations;

import java.util.Date;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 定时任务管理类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LKQuartzManager {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKQuartzManager.class);

	/** 单例 */
	private static final LKQuartzManager instance = new LKQuartzManager();


	/**
	 * 获取单例
	 * @return 单例
	 */
	public static LKQuartzManager getInstance() {
		return instance;
	}


	/** Scheduler */
	private Scheduler scheduler;


	/**
	 * 初始化计划
	 * @param scheduler 计划
	 */
	void initScheduler(Scheduler scheduler) {
		instance.scheduler = scheduler;
	}


	/**
	 * 构建任务详情
	 * @param groupName 组名称
	 * @param jobName 任务名称
	 * @param className 类名称
	 * @param methodName 方法名称
	 * @param params 任务参数
	 * @return 任务详情
	 */
	@SuppressWarnings("unchecked")
	public JobDetail buildJobDetail(String groupName, String jobName, String className, String methodName, Map<String, Object> params) {
		Class<? extends Job> clazz = null;
		try {
			clazz = (Class<? extends Job>) Class.forName(className);
		} catch (final ClassNotFoundException e) {
			throw new LKRuntimeException(LKErrorCodesEnum.INTERNAL_SERVER_ERROR, e);
		}
		final JobBuilder jobBuilder = JobBuilder.newJob(clazz);
		jobBuilder.withIdentity(jobName, groupName);
		jobBuilder.usingJobData("methodName", methodName);
		JobDetail jobDetail = jobBuilder.build();
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				jobDetail.getJobDataMap().put(entry.getKey(), entry.getValue());
			}
		}
		return jobDetail;
	}


	/**
	 * 构建任务详情
	 * @param groupName 组名称
	 * @param jobName 任务名称
	 * @param className 类名称
	 * @param methodName 方法名称
	 * @return 任务详情
	 */
	public JobDetail buildJobDetail(String groupName, String jobName, String className, String methodName) {
		return buildJobDetail(groupName, jobName, className, methodName, null);
	}


	/**
	 * 构建触发器
	 * @param simple 是否为简单触发器
	 * @param groupName 组名称
	 * @param jobName 任务名称
	 * @param cronExpression 表达式
	 * @return 触发器
	 */
	@SuppressWarnings("unchecked")
	public Trigger buildTrigger(boolean simple, String groupName, String jobName, String cronExpression) {
		TriggerBuilder<Trigger> triggerBuilder = null;
		try {
			final Trigger trigger = scheduler.getTrigger(new TriggerKey(jobName, groupName));
			if (trigger == null) {
				triggerBuilder = TriggerBuilder.newTrigger();
			} else {
				triggerBuilder = (TriggerBuilder<Trigger>) trigger.getTriggerBuilder();
			}
		} catch (final SchedulerException e) {
			triggerBuilder = TriggerBuilder.newTrigger();
		}
		triggerBuilder.withIdentity(jobName, groupName);
		if (simple) {
			triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).withRepeatCount(0));
			triggerBuilder.startAt(new Date());
		} else {
			triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
		}
		return triggerBuilder.build();
	}


	/**
	 * 计划任务
	 * @param simple 是否为简单触发器
	 * @param groupName 组名称
	 * @param jobName 任务名称
	 * @param className 类名称
	 * @param methodName 方法名称
	 * @param cronExpression 表达式
	 * @param params 任务参数
	 * @return 成功返回true，否则返回false。
	 */
	public boolean scheduleJob(boolean simple, String groupName, String jobName, String className, String methodName, String cronExpression, Map<String, Object> params) {
		try {
			scheduler.scheduleJob(buildJobDetail(groupName, jobName, className, methodName, params), buildTrigger(simple, groupName, jobName, cronExpression));
			return true;
		} catch (final SchedulerException e) {
			LOGGER.error(e);
			return false;
		}
	}


	/**
	 * 计划任务
	 * @param simple 是否为简单触发器
	 * @param groupName 组名称
	 * @param jobName 任务名称
	 * @param className 类名称
	 * @param methodName 方法名称
	 * @param cronExpression 表达式
	 * @return 成功返回true，否则返回false。
	 */
	public boolean scheduleJob(boolean simple, String groupName, String jobName, String className, String methodName, String cronExpression) {
		return scheduleJob(simple, groupName, jobName, className, methodName, cronExpression, null);
	}


	/**
	 * 重置触发器
	 * @param simple 是否为简单触发器
	 * @param groupName 组名称
	 * @param jobName 任务名称
	 * @param cronExpression 表达式
	 * @return 成功返回true，否则返回false。
	 */
	public boolean rescheduleJob(boolean simple, String groupName, String jobName, String cronExpression) {
		try {
			final Trigger trigger = buildTrigger(simple, groupName, jobName, cronExpression);
			scheduler.rescheduleJob(scheduler.getTrigger(new TriggerKey(jobName, groupName)).getKey(), trigger);
			return true;
		} catch (final Exception e) {
			LOGGER.error(e);
			return false;
		}
	}


	/**
	 * 启动计划
	 * @return 成功返回true，否则返回false。
	 */
	public boolean start() {
		try {
			scheduler.start();
			return true;
		} catch (final SchedulerException e) {
			LOGGER.error(e);
			return false;
		}
	}

}
