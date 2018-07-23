package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.LKRequestIDsUsingStatusBean;
import com.lichkin.framework.defines.entities.I_Base;
import com.lichkin.framework.web.annotations.LKController4Api;

/**
 * API数据请求控制器类定义
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKSameInApiBusUpdateUsingStatusController<E extends I_Base> extends LKApiBusUpdateUsingStatusController<LKRequestIDsUsingStatusBean, E> {

	@Override
	protected LKRequestIDsUsingStatusBean beforeInvokeService() {
		return in;
	}

}