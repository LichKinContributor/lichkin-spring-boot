package com.lichkin.springframework.utils;

import org.springframework.beans.BeanUtils;

import com.lichkin.framework.defines.exceptions.LKFrameworkException;

/**
 * Bean工具类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKBeanUtils {

	/**
	 * 复制属性
	 * @param source 源对象
	 * @param target 目标对象
	 * @return 目标对象
	 */
	public static <T> T copyProperties(Object source, T target) {
		BeanUtils.copyProperties(source, target);
		return target;
	}


	/**
	 * 复制属性
	 * @param source 源对象
	 * @param target 目标对象
	 * @param ignoreProperties 不复制的属性
	 * @return 目标对象
	 */
	public static <T> T copyProperties(Object source, T target, String... ignoreProperties) {
		BeanUtils.copyProperties(source, target, ignoreProperties);
		return target;
	}


	/**
	 * 复制属性
	 * @param source 源对象
	 * @param targetClass 目标对象类型
	 * @return 目标对象
	 */
	public static <T> T copyProperties(Object source, Class<T> targetClass) {
		try {
			T target = targetClass.newInstance();
			BeanUtils.copyProperties(source, target);
			return target;
		} catch (InstantiationException e) {
			throw new LKFrameworkException(e);
		} catch (IllegalAccessException e) {
			throw new LKFrameworkException(e);
		}
	}


	/**
	 * 复制属性
	 * @param source 源对象
	 * @param targetClass 目标对象类型
	 * @param ignoreProperties 不复制的属性
	 * @return 目标对象
	 */
	public static <T> T copyProperties(Object source, Class<T> targetClass, String... ignoreProperties) {
		try {
			T target = targetClass.newInstance();
			BeanUtils.copyProperties(source, target, ignoreProperties);
			return target;
		} catch (InstantiationException e) {
			throw new LKFrameworkException(e);
		} catch (IllegalAccessException e) {
			throw new LKFrameworkException(e);
		}
	}

}
