package com.lichkin.framework.springboot.services;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKRandomUtils;
import com.lichkin.springframework.services.LKService;

import lombok.Getter;

@Getter
public abstract class LKBaseTaskService extends LKService {

	protected static final String METHOD_NAME = "executeTask";

	private DateTime startTime;

	private String taskId = "";

	private String busId = "";


	public void executeTask() {
		startTime = DateTime.now();

		taskId = new StringBuilder().append(LKDateTimeUtils.now()).append(LKRandomUtils.create(15)).toString();

		logger.info(toUnifyLog(new StringBuilder().append("starting at ").append(LKDateTimeUtils.now()).append(".").toString()));

		doTask();

		DateTime endTime = DateTime.now();
		busId = "";
		logger.info(toUnifyLog(new StringBuilder().append("ended at ").append(LKDateTimeUtils.now()).append(". consuming ").append(endTime.getMillis() - startTime.getMillis()).append(" millisecond.").toString()));
	}


	protected abstract void doTask();


	protected String toUnifyLog(String content) {
		return new StringBuilder().append("taskId: [").append(taskId).append("]").append(StringUtils.isBlank(busId) ? "" : new StringBuilder().append(", busId: [").append(busId).append("]").toString()).append(" -> ").append(content).toString();
	}


	protected void createNewBusId() {
		busId = new StringBuilder().append(LKDateTimeUtils.now()).append(LKRandomUtils.create(15)).toString();
	}

}
