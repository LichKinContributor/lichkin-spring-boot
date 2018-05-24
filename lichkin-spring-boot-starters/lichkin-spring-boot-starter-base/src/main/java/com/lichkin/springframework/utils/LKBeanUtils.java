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
	 * @param <B> 返回值类型泛型
	 * @param source 源对象
	 * @param target 目标对象
	 * @return 目标对象
	 */
	public static <B> B copyProperties(Object source, B target) {
		BeanUtils.copyProperties(source, target);
		return target;
	}


	/**
	 * 复制属性
	 * @param <B> 返回值类型泛型
	 * @param source 源对象
	 * @param target 目标对象
	 * @param ignoreProperties 不复制的属性
	 * @return 目标对象
	 */
	public static <B> B copyProperties(Object source, B target, String... ignoreProperties) {
		BeanUtils.copyProperties(source, target, ignoreProperties);
		return target;
	}


	/**
	 * 复制属性
	 * @param <B> 返回值类型泛型
	 * @param source 源对象
	 * @param targetClass 目标对象类型
	 * @return 目标对象
	 */
	public static <B> B copyProperties(Object source, Class<B> targetClass) {
		try {
			B target = targetClass.newInstance();
			BeanUtils.copyProperties(source, target);
			return target;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new LKFrameworkException(e);
		}
	}


	/**
	 * 复制属性
	 * @param <B> 返回值类型泛型
	 * @param source 源对象
	 * @param targetClass 目标对象类型
	 * @param ignoreProperties 不复制的属性
	 * @return 目标对象
	 */
	public static <B> B copyProperties(Object source, Class<B> targetClass, String... ignoreProperties) {
		try {
			B target = targetClass.newInstance();
			BeanUtils.copyProperties(source, target, ignoreProperties);
			return target;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new LKFrameworkException(e);
		}
	}

}
