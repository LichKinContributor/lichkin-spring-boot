package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.springframework.services.LKVoidApiService;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @param <CO> 控制器类出参类型
 * @param <SI> 服务类入参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKVoidServiceApiController<CI extends LKRequestInterface, CO, SI> extends LKWithoutServiceApiController<CI, CO> {

	/**
	 * 获取服务类
	 * @return 服务类
	 */
	protected abstract LKVoidApiService<SI> getService();


	@Override
	protected LKResponseBean<CO> returnValue(CI cin) throws LKException {
		getService().handle(cin2sin(cin));
		return super.returnValue(cin);
	}


	/**
	 * 调用service方法前服务类入参
	 * @param cin 控制器类入参
	 * @return 服务类入参
	 */
	protected abstract SI cin2sin(CI cin);

}
