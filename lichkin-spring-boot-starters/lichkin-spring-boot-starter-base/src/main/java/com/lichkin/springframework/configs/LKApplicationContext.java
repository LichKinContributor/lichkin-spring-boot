package com.lichkin.springframework.configs;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.lichkin.framework.defines.exceptions.LKFrameworkException;

/**
 * 应用上下文
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Component
public class LKApplicationContext implements ApplicationContextAware {

	/** 应用上下文 */
	private static ApplicationContext context;


	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}


	/**
	 * 获取应用上下文
	 * @return 应用上下文
	 */
	public static ApplicationContext getContext() {
		if (context == null) {
			throw new LKFrameworkException("ApplicationContext is not ready.");
		}
		return context;
	}


	/**
	 * 获取对象
	 * @param name bean名称
	 * @see org.springframework.context.ApplicationContext#getBean(String)
	 * @return 对象
	 */
	public static Object getBean(final String name) {
		return getContext().getBean(name);
	}


	/**
	 * 获取对象
	 * @param <T> 类型
	 * @param requiredType 类型
	 * @see org.springframework.context.ApplicationContext#getBean(Class)
	 * @return 对象
	 */
	public static <T> T getBean(final Class<T> requiredType) {
		return getContext().getBean(requiredType);
	}

}
