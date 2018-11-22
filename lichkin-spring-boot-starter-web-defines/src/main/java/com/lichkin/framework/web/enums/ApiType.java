package com.lichkin.framework.web.enums;

/**
 * 接口类型枚举
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public enum ApiType {

	/**
	 * 开放接口（不设置登录信息；不设置公司信息；）
	 *
	 * <pre>
	 * 不做任何校验。（谨慎使用）
	 * </pre>
	 */
	OPEN,

	/**
	 * 登录前可用接口（设置登录信息（如果登录的话）；不设置公司信息；）
	 *
	 * <pre>
	 * token无值时不查询登录信息
	 * token有值时将查询登录信息，如果登录信息有误将报错。
	 * </pre>
	 */
	BEFORE_LOGIN,

	/**
	 * 个人业务接口（设置登录信息；不设置公司信息；）
	 *
	 * <pre>
	 * token不能为空，且使用token查询登录信息不能为空。
	 * </pre>
	 */
	PERSONAL_BUSINESS,

	/**
	 * 公司查询接口（不设置登录信息；设置公司信息；）
	 *
	 * <pre>
	 * compToken不能为空，且使用compToken查询公司信息不能为空。
	 * </pre>
	 */
	COMPANY_QUERY,

	/**
	 * ROOT查询接口（不设置登录信息；强制设置ROOT公司信息；）
	 *
	 * <pre>
	 * 不做任何校验。（谨慎使用）
	 * </pre>
	 */
	ROOT_QUERY,

	;

}
