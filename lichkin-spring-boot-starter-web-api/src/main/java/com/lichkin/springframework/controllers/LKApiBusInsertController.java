package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.beans.impl.LKResponseBlankBean;
import com.lichkin.framework.defines.entities.I_Base;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.springframework.services.LKApiBusInsertService;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKApiBusInsertController<CI extends LKRequestInterface, SI, E extends I_Base> extends LKVoidServiceApiController<CI, LKResponseBlankBean, SI> {

	/**
	 * 获取服务类
	 * @return 服务类
	 */
	@Override
	protected abstract LKApiBusInsertService<SI, E> getService();

}
