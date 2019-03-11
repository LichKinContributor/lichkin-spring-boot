package com.lichkin.framework.springboot.beans;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 任务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class LKJob {

	/** 是否为简单触发器 */
	private final boolean simple;

	/** 组名称 */
	private final String groupName;

	/** 任务名称 */
	private final String jobName;

	/** 类名称 */
	private final String className;

	/** 方法名称 */
	private String methodName = "executeTask";

	/** 表达式 */
	private final String cronExpression;

	/** 任务参数 */
	private final Map<String, Object> params;

}
