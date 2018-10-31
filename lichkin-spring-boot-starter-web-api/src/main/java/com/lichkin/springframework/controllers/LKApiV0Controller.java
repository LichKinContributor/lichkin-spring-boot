package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiV0Controller<CI extends LKRequestBean> extends LKApiController<CI, Void> {

	@Override
	Void handleInvoke(@Valid CI cin, String locale, String compId, String loginId) throws LKException {
		doInvoke(cin);
		return null;
	}


	/**
	 * 执行请求处理方法
	 * @deprecated 如框架约定的实现不能满足业务需要时才重写该方法
	 * @param cin 控制器类入参
	 * @throws LKException 转换时可能抛出的异常
	 */
	@Deprecated
	protected abstract void doInvoke(CI cin) throws LKException;

}
