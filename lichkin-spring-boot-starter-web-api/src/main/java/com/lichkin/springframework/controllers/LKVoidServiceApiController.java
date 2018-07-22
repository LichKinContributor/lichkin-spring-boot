package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.springframework.services.LKVoidApiService;

/**
 * API数据请求控制器类定义
 * @param <I> 控制器类入参类型
 * @param <O> 控制器类出参类型
 * @param <SI> 服务类入参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKVoidServiceApiController<I extends LKRequestInterface, O, SI> extends LKWithoutServiceApiController<I, O> {

	/**
	 * 获取服务类
	 * @return 服务类
	 */
	protected abstract LKVoidApiService<SI> getService();


	/**
	 * 返回前处理方法
	 * @deprecated 框架内部实现
	 */
	@Deprecated
	@Override
	protected void beforeReturn() throws LKException {
		SI sin = beforeInvokeService();
		getService().handle(sin);
	}


	/**
	 * 调用service方法前服务类入参
	 * @return 服务类入参
	 */
	protected abstract SI beforeInvokeService();

}
