package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.defines.entities.I_Base;
import com.lichkin.framework.web.annotations.LKController4Api;

/**
 * API数据请求控制器类定义
 * @param <I> 控制器类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKSameInApiBusInsertController<I extends LKRequestInterface, E extends I_Base> extends LKApiBusInsertController<I, I, E> {

	@Override
	protected I beforeInvokeService() {
		return in;
	}

}
