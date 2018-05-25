package com.lichkin.springframework.utils;

import com.lichkin.LKMain;
import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;

/**
 * 属性工具类，被spring管理的所有属性配置。
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKPropertiesUtils {

	/**
	 * 构造方法
	 */
	private LKPropertiesUtils() {
	}


	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKPropertiesUtils.class);


	/**
	 * 获取配置参数值
	 * @param key 参数名
	 * @param defaultValue 默认值
	 * @return 参数值
	 */
	public static String getProperty(final String key, final String defaultValue) {
		return LKMain.env.getProperty(key, defaultValue);
	}


	/**
	 * 获取配置参数值
	 * @param key 参数名
	 * @return 参数值
	 */
	public static String getProperty(final String key) {
		return getProperty(key, null);
	}


	/**
	 * 获取配置参数值
	 * @param key 参数名
	 * @param defaultValue 默认值
	 * @return 参数值
	 */
	public static boolean getProperty(final String key, final boolean defaultValue) {
		return Boolean.parseBoolean(getProperty(key, String.valueOf(defaultValue)));
	}


	/**
	 * 获取配置参数值
	 * @param key 参数名
	 * @param defaultValue 默认值
	 * @return 参数值
	 */
	public static int getProperty(final String key, final int defaultValue) {
		return Integer.parseInt(getProperty(key, String.valueOf(defaultValue)));
	}


	/**
	 * 验证配置值
	 * @param key 配置键
	 * @param defaultValue 默认值
	 * @return 配置值
	 */
	public static String validateConfigValue(final String key, final String defaultValue) {
		final String value = getProperty(key);
		if (value == null) {
			if (defaultValue == null) {
				throw new LKFrameworkException("systemId[" + LKMain.SYSTEM_ID + "] ✘ -> 【" + key + "】 must config.");
			} else {
				LOGGER.warn("systemId[%s] ✘ -> %s=%s, use default value 【%s】 cause 【%s】 not configed.", LKMain.SYSTEM_ID, key, defaultValue, defaultValue, key);
				return defaultValue;
			}
		}
		LOGGER.warn("systemId[%s] ✔ -> %s=%s", LKMain.SYSTEM_ID, key, value);
		return value;
	}


	/**
	 * 验证配置值
	 * @param key 配置键
	 * @param defaultValue 默认值
	 * @return 配置值
	 */
	public static boolean validateConfigValue(final String key, final boolean defaultValue) {
		return Boolean.parseBoolean(validateConfigValue(key, String.valueOf(defaultValue)));
	}


	/**
	 * 验证配置值
	 * @param key 配置键
	 * @param defaultValue 默认值
	 * @return 配置值
	 */
	public static int validateConfigValue(final String key, final int defaultValue) {
		return Integer.parseInt(validateConfigValue(key, String.valueOf(defaultValue)));
	}

}
