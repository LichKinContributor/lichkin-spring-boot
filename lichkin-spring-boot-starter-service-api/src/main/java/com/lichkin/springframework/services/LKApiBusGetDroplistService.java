package com.lichkin.springframework.services;

import java.util.List;

import com.lichkin.framework.defines.beans.impl.LKDroplistBean;

/**
 * 获取下拉列表数据接口服务类定义
 * @param <SI> 服务类入参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusGetDroplistService<SI> extends LKApiServiceImpl<SI, LKDroplistBean> implements LKApiService<SI, List<LKDroplistBean>> {

	@SuppressWarnings("unchecked")
	public LKApiBusGetDroplistService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = LKDroplistBean.class;
	}

}
