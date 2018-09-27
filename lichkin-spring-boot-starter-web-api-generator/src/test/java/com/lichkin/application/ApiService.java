package com.lichkin.application;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ApiService<SI, SO> {

	/** 泛型类型 */
	Type[] types;

	/** 服务类入参类型 */
	public Class<SI> classSI;

	/** 服务类出参类型 */
	public Class<SO> classSO;


	/**
	 * 构造方法
	 */
	public ApiService() {
		super();
		types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
	}

}
