package com.lichkin.springframework.services;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;

/**
 * 接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
abstract class LKVoidApiBusService<SI, E extends I_ID> extends LKVoidApiService<SI> {

	/** 数据已存在 */
	private LKCodeEnum existErrorCode = LKErrorCodesEnum.EXIST;

	/** 数据不存在 */
	private LKCodeEnum inexistentErrorCode = LKErrorCodesEnum.INEXIST;

	/** 服务类入参类型 */
	protected Class<SI> classSI;

	/** 实体类类型 */
	protected Class<E> classE;


	@SuppressWarnings("unchecked")
	public LKVoidApiBusService() {
		super();
		Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		classSI = (Class<SI>) types[0];
		classE = (Class<E>) types[1];
	}


	/**
	 * 数据已存在错误编码
	 * @return 错误编码
	 */
	public LKCodeEnum existErrorCode() {
		return existErrorCode;
	}


	/**
	 * 数据不存在错误编码
	 * @return 错误编码
	 */
	public LKCodeEnum inexistentErrorCode() {
		return inexistentErrorCode;
	}

}
