package com.lichkin.framework.springboot.utils;

import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.lichkin.framework.defines.LKConfigStatics;
import com.lichkin.framework.springboot.configurations.LKQuartzManager;
import com.lichkin.framework.springboot.services.LKBaseJobService;

/**
 * 定时任务工具类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKJobUtils {

	/** 组名称 */
	private static final String GROUP_NAME;
	static {
		String groupName = LKConfigStatics.SYSTEM_TAG;
		if (LKConfigStatics.SYSTEM_DEBUG) {
			groupName = "DEBUG-" + groupName;
		}
		GROUP_NAME = groupName;
	}

	/** 方法名称 */
	private static final String METHOD_NAME = "executeTask";


	/**
	 * 构建任务详情
	 * @param clazz 任务类类型
	 * @param params 任务参数
	 * @return 任务详情
	 */
	public static JobDetail buildJobDetail(Class<? extends LKBaseJobService> clazz, Map<String, Object> params) {
		return LKQuartzManager.getInstance().buildJobDetail(GROUP_NAME, clazz.getSimpleName(), clazz.getName(), METHOD_NAME, params);
	}


	/**
	 * 构建任务详情
	 * @param clazz 任务类类型
	 * @return 任务详情
	 */
	public static JobDetail buildJobDetail(Class<? extends LKBaseJobService> clazz) {
		return buildJobDetail(clazz, null);
	}


	/**
	 * 构建触发器
	 * @param clazz 任务类类型
	 * @param cronExpression 表达式
	 * @return 触发器
	 */
	public static Trigger buildTrigger(Class<? extends LKBaseJobService> clazz, String cronExpression) {
		return LKQuartzManager.getInstance().buildTrigger(LKConfigStatics.SYSTEM_DEBUG, GROUP_NAME, clazz.getSimpleName(), cronExpression);
	}


	/**
	 * 计划任务
	 * @param clazz 任务类类型
	 * @param cronExpression 表达式
	 * @param params 任务参数
	 * @return 成功返回true，否则返回false。
	 */
	public static boolean scheduleJob(Class<? extends LKBaseJobService> clazz, String cronExpression, Map<String, Object> params) {
		return LKQuartzManager.getInstance().scheduleJob(LKConfigStatics.SYSTEM_DEBUG, GROUP_NAME, clazz.getSimpleName(), clazz.getName(), METHOD_NAME, cronExpression, params);
	}


	/**
	 * 计划任务
	 * @param clazz 任务类类型
	 * @param cronExpression 表达式
	 * @return 成功返回true，否则返回false。
	 */
	public static boolean scheduleJob(Class<? extends LKBaseJobService> clazz, String cronExpression) {
		return scheduleJob(clazz, cronExpression, null);
	}


	/**
	 * 重置触发器
	 * @param clazz 任务类类型
	 * @param cronExpression 表达式
	 * @return 成功返回true，否则返回false。
	 */
	public static boolean rescheduleJob(Class<? extends LKBaseJobService> clazz, String cronExpression) {
		return LKQuartzManager.getInstance().rescheduleJob(LKConfigStatics.SYSTEM_DEBUG, GROUP_NAME, clazz.getSimpleName(), cronExpression);
	}

}
