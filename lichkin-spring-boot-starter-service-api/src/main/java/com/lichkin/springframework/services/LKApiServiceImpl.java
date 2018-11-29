package com.lichkin.springframework.services;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiServiceImpl<CI, SO> extends LKDBService {

	/** 泛型类型 */
	Type[] types;

	/** 服务类入参类型 */
	Class<CI> classCI;

	/** 服务类出参类型 */
	Class<SO> classSO;


	/**
	 * 构造方法
	 */
	public LKApiServiceImpl() {
		super();
		types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
	}

}
