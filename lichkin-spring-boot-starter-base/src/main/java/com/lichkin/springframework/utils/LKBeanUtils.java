package com.lichkin.springframework.utils;

import org.springframework.beans.BeanUtils;

import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;

/**
 * Bean工具类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKBeanUtils {

	/**
	 * 复制属性
	 * @param <B> 返回值类型泛型
	 * @param source 源对象
	 * @param target 目标对象
	 * @param ignoreProperties 不复制的属性
	 * @return 目标对象
	 */
	public static <B> B copyProperties(Object source, B target, String... ignoreProperties) {
		if (source != null) {
			BeanUtils.copyProperties(source, target, ignoreProperties);
		}
		return target;
	}


	/**
	 * 创建对象
	 * @param <B> 目标对象类型泛型
	 * @param source 源对象
	 * @param targetClass 目标对象类型
	 * @param ignoreProperties 忽略复制的字段名
	 * @return 目标对象
	 */
	public static <B> B newInstance(final Object source, final Class<B> targetClass, final String... ignoreProperties) {
		try {
			return copyProperties(source, targetClass.newInstance(), ignoreProperties);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new LKRuntimeException(LKErrorCodesEnum.INTERNAL_SERVER_ERROR, e);
		}
	}

}
