package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * API数据请求控制器类定义（同入参同出参）
 * @param <I> 控制器类/服务类入参类型
 * @param <O> 控制器类/服务类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiSYSYController<I extends LKRequestBean, O> extends LKApiYYController<I, O, I, O> {

	@Override
	protected I beforeInvokeService(I cin) throws LKException {
		return cin;
	}


	@Override
	protected O afterInvokeService(I cin, I sin, O sout) throws LKException {
		return sout;
	}

}
