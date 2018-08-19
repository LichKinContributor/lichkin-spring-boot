package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.LKRequestIDBean;
import com.lichkin.framework.defines.entities.I_Base;
import com.lichkin.framework.web.annotations.LKController4Api;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKSameInApiBusUpdateController<CI extends LKRequestIDBean, E extends I_Base> extends LKApiBusUpdateController<CI, CI, E> {

	@Override
	protected CI cin2sin(CI cin) {
		return cin;
	}

}
