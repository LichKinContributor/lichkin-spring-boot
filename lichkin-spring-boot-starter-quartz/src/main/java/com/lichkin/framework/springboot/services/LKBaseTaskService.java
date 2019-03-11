package com.lichkin.framework.springboot.services;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKRandomUtils;
import com.lichkin.springframework.services.LKService;

import lombok.Getter;

/**
 * 基础任务服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
abstract class LKBaseTaskService extends LKService {

	/** 执行任务方法 */
	protected static final String METHOD_NAME = "executeTask";

	/** 任务开始时间 */
	private DateTime startTime;

	/** 任务ID */
	private String taskId = "";

	/** 业务ID */
	private String busId = "";


	/**
	 * 执行任务方法（实际入口）
	 */
	public void executeTask() {
		startTime = DateTime.now();

		taskId = new StringBuilder().append(LKDateTimeUtils.now()).append(LKRandomUtils.create(15)).toString();

		if (logger.isDebugEnabled()) {
			logger.debug(toUnifyLog(new StringBuilder().append("starting at ").append(LKDateTimeUtils.now()).append(".").toString()));
		}

		doTask();

		DateTime endTime = DateTime.now();
		busId = "";
		if (logger.isDebugEnabled()) {
			logger.debug(toUnifyLog(new StringBuilder().append("ended at ").append(LKDateTimeUtils.now()).append(". consuming ").append(endTime.getMillis() - startTime.getMillis()).append(" millisecond.").toString()));
		}
	}


	/**
	 * 子类实现执行任务方法
	 */
	protected abstract void doTask();


	/**
	 * 统一输出日志
	 * @param content 日志内容
	 * @return 实际日志内容
	 */
	protected String toUnifyLog(String content) {
		return new StringBuilder().append("taskId: [").append(taskId).append("]").append(StringUtils.isBlank(busId) ? "" : new StringBuilder().append(", busId: [").append(busId).append("]").toString()).append(" -> ").append(content).toString();
	}


	/**
	 * 创建业务ID
	 */
	protected void createNewBusId() {
		busId = new StringBuilder().append(LKDateTimeUtils.now()).append(LKRandomUtils.create(15)).toString();
	}

}
