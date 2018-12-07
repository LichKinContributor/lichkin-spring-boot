package com.lichkin.springframework.web.beans;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 页面对象，控制器类方法需要设定该类型返回值，才可以使用框架提供的模板。
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class LKPage {

	/** 属性集合 */
	private Map<String, Object> attributes = new HashMap<>();

	/** 视图名称 */
	@NonNull
	private String viewName;

	/** 服务器解析页面时使用的参数 */
	private Map<String, Object> serverDatas = new HashMap<>();

	/** 空白页 */
	public static final String BLANK = "/blank";

	/** 是否使用实际视图名 */
	private boolean realViewName = false;


	/**
	 * 设置使用实际视图名
	 * @param realViewName 是否使用实际视图名
	 * @return 本对象
	 */
	public LKPage setRealViewName(boolean realViewName) {
		this.realViewName = realViewName;
		return this;
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


	/**
	 * 添加数据
	 * @param key 键
	 * @param value 值
	 * @return 当前对象
	 */
	public LKPage putServerData(String key, Object value) {
		serverDatas.put(key, value);
		return this;
	}


	/**
	 * 删除数据
	 * @param key 键
	 * @return 当前对象
	 */
	public LKPage removeServerData(String key) {
		serverDatas.remove(key);
		return this;
	}

}
