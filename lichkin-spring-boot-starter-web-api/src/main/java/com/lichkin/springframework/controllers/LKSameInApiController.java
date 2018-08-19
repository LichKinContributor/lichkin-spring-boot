package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;

/**
 * API数据请求控制器类定义
 * @param <CI> 入参类型
 * @param <CO> 控制器类出参类型
 * @param <SO> 服务类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKSameInApiController<CI extends LKRequestInterface, CO, SO> extends LKStandardApiController<CI, CO, CI, SO> {

	@Override
	protected CI cin2sin(CI in) {
		return in;
	}

}
