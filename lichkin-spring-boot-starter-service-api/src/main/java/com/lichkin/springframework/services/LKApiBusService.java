package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_ID;

/**
 * 业务接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusService<SI, SO, E extends I_ID> extends LKApiServiceImpl<SI, SO> {

	/** 实体类类型 */
	Class<E> classE;

}
