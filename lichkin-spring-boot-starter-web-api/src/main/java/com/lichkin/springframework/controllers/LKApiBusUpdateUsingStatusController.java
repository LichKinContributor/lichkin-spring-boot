package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.LKRequestIDsUsingStatusBean;
import com.lichkin.framework.beans.impl.LKResponseBlankBean;
import com.lichkin.framework.defines.entities.I_Base;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.springframework.services.LKApiBusUpdateUsingStatusService;

/**
 * API数据请求控制器类定义
 * @param <I> 控制器类入参类型
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKApiBusUpdateUsingStatusController<SI extends I_UsingStatus, E extends I_Base> extends LKVoidServiceApiController<LKRequestIDsUsingStatusBean, LKResponseBlankBean, SI> {

	/**
	 * 获取服务类
	 * @return 服务类
	 */
	@Override
	protected abstract LKApiBusUpdateUsingStatusService<SI, E> getService();

}
