package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiVoidService;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @param <CO> 控制器类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiYVController<CI extends LKRequestBean, CO> extends LKApiY0Controller<CI, CO> {

	@Override
	@Deprecated
	protected CO doInvoke(CI cin, ApiKeyValues<CI> params) throws LKException {
		beforeInvokeService(cin, params);
		getService(cin, params).handle(cin, params);
		return afterInvokeService(cin, params);
	}


	/**
	 * 获取服务类对象
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @return 服务类对象
	 */
	protected abstract LKApiVoidService<CI> getService(CI cin, ApiKeyValues<CI> params);


	/**
	 * 调用服务类前参数处理
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @throws LKException 转换时可能抛出的异常
	 */
	protected void beforeInvokeService(CI cin, ApiKeyValues<CI> params) throws LKException {
	}


	/**
	 * 调用服务类后参数处理
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @return 控制器类出参
	 * @throws LKException 转换时可能抛出的异常
	 */
	protected abstract CO afterInvokeService(CI cin, ApiKeyValues<CI> params) throws LKException;

}
