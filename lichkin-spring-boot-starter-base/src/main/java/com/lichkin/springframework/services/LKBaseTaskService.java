package com.lichkin.springframework.services;

import org.joda.time.DateTime;

import com.lichkin.framework.defines.enums.impl.LKDateTimeTypeEnum;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKRandomUtils;

import lombok.Getter;

/**
 * 通用基本任务服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
public abstract class LKBaseTaskService extends LKService {

	/** 方法名称 */
	protected static final String METHOD_NAME = "executeTask";

	/** 当前任务开始时间 */
	private DateTime startTime;

	/** 当前任务ID */
	private String taskId = "";

	/** 当前业务ID */
	private String busId = "";


	/**
	 * 执行任务
	 */
	public void executeTask() {
		startTime = DateTime.now();
		taskId = LKDateTimeUtils.toString(startTime) + LKRandomUtils.create(15);

		logger.info(toUnifyLog(String.format("starting at %s.", LKDateTimeUtils.toString(startTime))));

		doTask();

		final DateTime endTime = DateTime.now();// 当前任务结束时间
		busId = "";
		logger.info(toUnifyLog(String.format("ended at %s. consuming %s millisecond.", LKDateTimeUtils.toString(endTime), String.valueOf((endTime.getMillis() - startTime.getMillis())))));
	}


	/**
	 * 执行任务，子类在此方法中实现业务逻辑，外部类需要调用executeTask()来执行任务。
	 */
	protected abstract void doTask();


	/**
	 * 转换为统一日志内容，子类需使用此方法来输出日志。
	 * @param content 日志内容
	 * @return 统一日志内容
	 */
	protected String toUnifyLog(final String content) {
		return String.format("taskId: [%s], busId: [%s] -> %s", taskId, busId, content);
	}


	/**
	 * 生成新的业务ID
	 */
	protected void createNewBusId() {
		busId = LKDateTimeUtils.now(LKDateTimeTypeEnum.TIMESTAMP_MIN) + LKRandomUtils.create(15);
	}

}
