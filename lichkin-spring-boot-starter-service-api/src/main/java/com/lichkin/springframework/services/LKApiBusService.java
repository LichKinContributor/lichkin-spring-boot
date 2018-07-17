package com.lichkin.springframework.services;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;

/**
 * 接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
abstract class LKApiBusService<SI, SO, E extends I_ID> extends LKApiService<SI, SO> {

	/** 数据已存在 */
	protected LKCodeEnum existErrorCode = LKErrorCodesEnum.EXIST;

	/** 数据不存在 */
	protected LKCodeEnum inexistentErrorCode = LKErrorCodesEnum.INEXIST;

	/** 服务类入参类型 */
	protected Class<SI> classSI;

	/** 服务类出参类型 */
	protected Class<SO> classSO;

	/** 实体类类型 */
	protected Class<E> classE;


	@SuppressWarnings("unchecked")
	public LKApiBusService() {
		super();
		Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		classSI = (Class<SI>) types[0];
		classSO = (Class<SO>) types[1];
		classE = (Class<E>) types[2];
	}

}
