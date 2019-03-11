package com.lichkin.framework.springboot.services;

import java.util.List;

import com.lichkin.framework.springboot.beans.LKJob;

/**
 * 定时任务初始化器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface LKTaskInitializer {

	/**
	 * 初始化
	 * @param list 任务列表
	 */
	public void init(List<LKJob> list);

}
