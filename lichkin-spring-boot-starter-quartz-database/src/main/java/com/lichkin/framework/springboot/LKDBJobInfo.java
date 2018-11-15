package com.lichkin.framework.springboot;

import com.lichkin.framework.springboot.services.LKBaseDBJobService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 定时任务信息
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@RequiredArgsConstructor
public class LKDBJobInfo {

	/** 定时任务类 */
	private final Class<? extends LKBaseDBJobService> clazz;

	/** 表达式 */
	private final String cronExpression;


	/**
	 * 构造方法
	 * @param clazz 定时任务类
	 */
	public LKDBJobInfo(Class<? extends LKBaseDBJobService> clazz) {
		super();
		this.clazz = clazz;
		cronExpression = "once";
	}

}
