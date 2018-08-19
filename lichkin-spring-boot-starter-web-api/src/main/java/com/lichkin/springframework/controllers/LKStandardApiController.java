package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.springframework.services.LKApiService;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @param <CO> 控制器类出参类型
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKStandardApiController<CI extends LKRequestInterface, CO, SI, SO> extends LKWithoutServiceApiController<CI, CO> {

	/**
	 * 获取服务类
	 * @return 服务类
	 */
	protected abstract LKApiService<SI, SO> getService();


	@Override
	protected LKResponseBean<CO> returnValue(CI in) throws LKException {
		return new LKResponseBean<>(sout2cout(getService().handle(cin2sin(in))));
	}


	/**
	 * 调用service方法前
	 * @param in 控制器类入参
	 * @return 服务类入参
	 */
	protected abstract SI cin2sin(CI in);


	/**
	 * 处理返回结果
	 * @param sout 服务类出参
	 * @return 控制器类出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	protected abstract CO sout2cout(SO sout) throws LKException;

}
