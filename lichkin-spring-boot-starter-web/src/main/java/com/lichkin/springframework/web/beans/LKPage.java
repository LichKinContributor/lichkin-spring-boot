package com.lichkin.springframework.web.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * 页面对象，控制器类方法需要设定该类型返回值，才可以使用框架提供的模板。
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKPage {

	/** 属性集合 */
	private Map<String, Object> attributes = new HashMap<>();


	/**
	 * 获取属性集合
	 * @return 属性集合
	 */
	public Map<String, ?> getAttributes() {
		return attributes;
	}


	/**
	 * 添加属性
	 * @param key 键
	 * @param value 值
	 * @return 当前对象
	 */
	public LKPage putAttribute(String key, Object value) {
		attributes.put(key, value);
		return this;
	}


	/**
	 * 删除属性
	 * @param key 键
	 * @return 当前对象
	 */
	public LKPage removeAttribute(String key) {
		attributes.remove(key);
		return this;
	}

}
