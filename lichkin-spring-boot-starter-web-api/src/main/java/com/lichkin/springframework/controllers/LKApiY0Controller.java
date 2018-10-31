package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @param <CO> 控制器类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiY0Controller<CI extends LKRequestBean, CO> extends LKApiController<CI, CO> {

	@Override
	CO handleInvoke(@Valid CI cin, String locale, String compId, String loginId) throws LKException {
		return doInvoke(cin);
	}


	/**
	 * 执行请求处理方法
	 * @deprecated 如框架约定的实现不能满足业务需要时才重写该方法
	 * @param cin 控制器类入参
	 * @return 控制器类出参
	 * @throws LKException 转换时可能抛出的异常
	 */
	@Deprecated
	protected abstract CO doInvoke(CI cin) throws LKException;

}
