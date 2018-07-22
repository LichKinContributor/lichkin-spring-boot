package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.springframework.services.LKApiService;

/**
 * API数据请求控制器类定义
 * @param <I> 控制器类入参类型
 * @param <O> 控制器类出参类型
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKStandardApiController<I extends LKRequestInterface, O, SI, SO> extends LKWithoutServiceApiController<I, O> {

	/** 服务类出参 */
	protected SO sout;


	/**
	 * 获取服务类
	 * @return 服务类
	 */
	protected abstract LKApiService<SI, SO> getService();


	/**
	 * 返回前处理方法
	 * @deprecated 框架内部实现
	 */
	@Deprecated
	@Override
	protected void beforeReturn() throws LKException {
		SI sin = beforeInvokeService();
		sout = getService().handle(sin);
	}


	/**
	 * 调用service方法前
	 * @return 服务类入参
	 */
	protected abstract SI beforeInvokeService();

}
