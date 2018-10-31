package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.Datas;
import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiVoidService;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @param <CO> 控制器类出参类型
 * @param <SI> 服务类入参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiYVController<CI extends LKRequestBean, CO, SI> extends LKApiY0Controller<CI, CO> {

	@Override
	@Deprecated
	protected CO doInvoke(CI cin) throws LKException {
		SI sin = beforeInvokeService(cin);
		Datas datas = cin.getDatas();
		getService(cin).handle(sin, datas.getLocale(), datas.getCompId(), datas.getLoginId());
		return afterInvokeService(cin, sin);
	}


	/**
	 * 获取服务类对象
	 * @param cin 控制器类入参
	 * @return 服务类对象
	 */
	protected abstract LKApiVoidService<SI> getService(CI cin);


	/**
	 * 调用服务类前参数处理
	 * @param cin 控制器类入参
	 * @return 服务类入参
	 * @throws LKException 转换时可能抛出的异常
	 */
	protected abstract SI beforeInvokeService(CI cin) throws LKException;


	/**
	 * 调用服务类后参数处理
	 * @param cin 控制器类入参
	 * @param sin 服务类入参
	 * @return 控制器类出参
	 * @throws LKException 转换时可能抛出的异常
	 */
	protected abstract CO afterInvokeService(CI cin, SI sin) throws LKException;

}
