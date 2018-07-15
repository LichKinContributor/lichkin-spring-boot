package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;

/**
 * API数据请求控制器类定义
 * @param <I> 入参类型
 * @param <O> 出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiController<I extends LKRequestInterface, O> extends LKNormalApiController<I, O, O> {

	@Override
	protected O sout2out(O sout) {
		return sout;
	}

}
