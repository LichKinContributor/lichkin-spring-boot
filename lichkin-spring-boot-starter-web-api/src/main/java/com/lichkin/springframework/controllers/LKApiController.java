package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * API数据请求控制器类定义
 * @param <I> 入参类型
 * @param <O> 出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiController<I extends LKRequestInterface, O> extends LKSameInApiController<I, O, O> {

	@Override
	protected O returnOut() throws LKException {
		return sout;
	}

}
