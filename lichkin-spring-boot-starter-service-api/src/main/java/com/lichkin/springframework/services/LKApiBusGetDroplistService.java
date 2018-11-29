package com.lichkin.springframework.services;

import java.util.List;

import com.lichkin.framework.defines.beans.impl.LKDroplistBean;

/**
 * 获取下拉列表数据接口服务类定义
 * @param <CI> 服务类入参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusGetDroplistService<CI> extends LKApiServiceImpl<CI, LKDroplistBean> implements LKApiService<CI, List<LKDroplistBean>> {

	@SuppressWarnings("unchecked")
	public LKApiBusGetDroplistService() {
		super();
		classCI = (Class<CI>) types[0];
		classSO = LKDroplistBean.class;
	}

}
