package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;

/**
 * API数据请求控制器类定义
 * @param <I> 入参类型
 * @param <O> 控制器类出参类型
 * @param <SO> 服务类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKSameInApiController<I extends LKRequestInterface, O, SO> extends LKStandardApiController<I, O, I, SO> {

	@Override
	protected I beforeInvokeService() {
		return in;
	}

}
