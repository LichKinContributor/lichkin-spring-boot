package com.lichkin.framework.springboot;

import com.lichkin.framework.springboot.services.LKBaseJobService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 定时任务信息
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@RequiredArgsConstructor
public class LKJobInfo {

	/** 定时任务类 */
	private final Class<? extends LKBaseJobService> clazz;

	/** 表达式 */
	private final String cronExpression;


	/**
	 * 构造方法
	 * @param clazz 定时任务类
	 */
	public LKJobInfo(Class<? extends LKBaseJobService> clazz) {
		super();
		this.clazz = clazz;
		cronExpression = "once";
	}

}
