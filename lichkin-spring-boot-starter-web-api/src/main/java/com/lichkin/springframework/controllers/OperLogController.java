package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.springframework.services.OperLogService;

/**
 * 日志控制器类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface OperLogController<I extends LKRequestBean> {

	/**
	 * 获取日志服务类
	 * @return 日志服务类
	 */
	public OperLogService getLogService();

}
