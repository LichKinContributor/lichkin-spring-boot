package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.KeyValues;
import com.lichkin.framework.defines.beans.LKPageable;
import com.lichkin.framework.defines.entities.I_Comp;
import com.lichkin.framework.defines.entities.I_Login;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;

/**
 * API键值对封装对象
 * @param <CI> 控制器类入参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class ApiKeyValues<CI> extends KeyValues {

	/**
	 * 构造方法
	 * @param locale 国际化
	 * @param originalObject 原参数对象
	 */
	ApiKeyValues(String locale, CI originalObject) {
	}


	/**
	 * 获取国际化
	 * <pre>
	 *   CI实现了I_Locale接口
	 *     如取到值则使用该值
	 *     如取不到值则使用解析值
	 *   CI未实现I_locale接口，使用解析值。
	 * </pre>
	 * @return 国际化
	 */
	public String getLocale() {
		return null;
	}


	/**
	 * 获取原参数对象
	 * @return 原参数对象
	 */
	public CI getOriginalObject() {
		return null;
	}


	/**
	 * 获取ID
	 * @return ID
	 */
	public String getId() {
		return null;
	}


	/**
	 * 获取在用状态
	 * @return 在用状态
	 */
	public LKUsingStatusEnum getUsingStatus() {
		return null;
	}


	/**
	 * 获取分页信息
	 * @return 分页信息
	 */
	public LKPageable getPageable() {
		return null;
	}


	/**
	 * 设置公司信息
	 * @param comp 公司信息
	 */
	void setComp(I_Comp comp) {
	}


	/**
	 * 获取公司信息
	 * @return 公司信息
	 */
	public I_Comp getComp() {
		return null;
	}


	/**
	 * 获取公司ID
	 * @return 公司ID
	 */
	public String getCompId() {
		return null;
	}


	/**
	 * 获取公司ID
	 * <pre>
	 *   非ROOT权限取值为null
	 *   ROOT权限且CI实现了I_CompId接口，并且传入了参数，将返回该值，否则返回null。
	 * </pre>
	 * @return 公司ID
	 */
	public String getBusCompId() {
		return null;
	}


	/**
	 * 设置登录信息
	 * @param login 登录信息
	 */
	void setLogin(I_Login login) {
	}


	/**
	 * 获取登录信息
	 * @return 登录信息
	 */
	public I_Login getLogin() {
		return null;
	}


	/**
	 * 获取登录ID
	 * @return 登录ID
	 */
	public String getLoginId() {
		return null;
	}

}
