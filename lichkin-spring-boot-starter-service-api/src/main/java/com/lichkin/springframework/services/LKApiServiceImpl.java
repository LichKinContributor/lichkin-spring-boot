package com.lichkin.springframework.services;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

import com.lichkin.framework.defines.LKFrameworkStatics;

/**
 * 接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiServiceImpl<SI, SO> extends LKDBService {

	/** 泛型类型 */
	Type[] types;

	/** 服务类入参类型 */
	Class<SI> classSI;

	/** 服务类出参类型 */
	Class<SO> classSO;


	/**
	 * 构造方法
	 */
	public LKApiServiceImpl() {
		super();
		types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
	}


	/**
	 * 获取国际化
	 * @param locale 国际化
	 * @param busLocale 入参中的国际化
	 * @return 国际化
	 */
	protected String getLocale(String locale, String busLocale) {
		return StringUtils.isNotBlank(busLocale) ? busLocale : locale;
	}


	/**
	 * 获取公司ID
	 * @param compId 公司ID
	 * @param busCompId 入参中的公司ID
	 * @return 公司ID
	 */
	protected String getCompId(String compId, String busCompId) {
		return LKFrameworkStatics.LichKin.equals(compId) && StringUtils.isNotBlank(busCompId) ? busCompId : compId;
	}

}
