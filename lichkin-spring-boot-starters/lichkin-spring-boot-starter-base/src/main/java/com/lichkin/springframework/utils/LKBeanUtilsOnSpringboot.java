package com.lichkin.springframework.utils;

import org.springframework.beans.BeanUtils;

import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;

/**
 * Bean工具类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public final class LKBeanUtilsOnSpringboot {

	/**
	 * 创建对象
	 * @param <T> 目标对象类型泛型
	 * @param source 源对象
	 * @param targetClass 目标对象类型
	 * @param ignoreProperties 忽略复制的字段名
	 * @return 目标对象
	 */
	public static <T> T newInstance(final Object source, final Class<T> targetClass, final String... ignoreProperties) {
		try {
			final T target = targetClass.newInstance();
			BeanUtils.copyProperties(source, target, ignoreProperties);
			return target;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new LKRuntimeException(LKErrorCodesEnum.INTERNAL_SERVER_ERROR, e);
		}
	}


	/**
	 * 创建对象
	 * @param <T> 目标对象类型泛型
	 * @param source 源对象
	 * @param targetClass 目标对象类型
	 * @return 目标对象
	 */
	public static <T> T newInstance(final Object source, final Class<T> targetClass) {
		return newInstance(source, targetClass, (String[]) null);
	}

}
