package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.Datas;
import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiVoidService;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @param <SI> 服务类入参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiVVController<CI extends LKRequestBean, SI> extends LKApiV0Controller<CI> {

	/**
	 * 执行请求处理方法
	 * @deprecated 如框架约定的实现不能满足业务需要时才重写该方法
	 * @param cin 控制器类入参
	 * @throws LKException 业务操作时可能抛出的异常
	 */
	@Override
	@Deprecated
	protected void doInvoke(CI cin) throws LKException {
		SI sin = beforeInvokeService(cin);
		Datas datas = cin.getDatas();
		getService(cin).handle(sin, datas.getLocale(), datas.getCompId(), datas.getLoginId());
		afterInvokeService(cin, sin);
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
	 * @throws LKException 转换时可能抛出的异常
	 */
	protected void afterInvokeService(CI cin, SI sin) throws LKException {
	}

}
