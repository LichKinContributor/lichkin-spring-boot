package com.lichkin.springframework.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.lichkin.framework.defines.LKFramework;
import com.lichkin.framework.defines.enums.LKCodeEnum;

/**
 * 控制器类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
class LKController extends LKFramework {

	@Autowired
	protected HttpServletRequest request;


	/**
	 * 验证字符串
	 * @param str 待验证字符串
	 * @param allowEmpty 允许空值
	 * @param errorCode 错误编码
	 * @return 验证成功返回值
	 */
	protected String checkString(String str, boolean allowEmpty, LKCodeEnum errorCode) {
		return checkString(str, allowEmpty, errorCode, (Locale) request.getAttribute("locale"));
	}


	/**
	 * 验证数字
	 * @param num 待验证数字
	 * @param errorCode 错误编码
	 * @param locale 国际化类型
	 * @return 验证成功返回值
	 */
	protected Long checkLong(Long num, LKCodeEnum errorCode) {
		return checkLong(num, errorCode, (Locale) request.getAttribute("locale"));
	}


	/**
	 * 验证数字
	 * @param num 待验证数字
	 * @param errorCode 错误编码
	 * @param locale 国际化类型
	 * @return 验证成功返回值
	 */
	protected Long checkLong(String num, LKCodeEnum errorCode) {
		return checkLong(num, errorCode, (Locale) request.getAttribute("locale"));
	}


	/**
	 * 验证数字
	 * @param num 待验证数字
	 * @param errorCode 错误编码
	 * @param locale 国际化类型
	 * @return 验证成功返回值
	 */
	protected Integer checkInteger(Integer num, LKCodeEnum errorCode) {
		return checkInteger(num, errorCode, (Locale) request.getAttribute("locale"));
	}


	/**
	 * 验证数字
	 * @param num 待验证数字
	 * @param errorCode 错误编码
	 * @param locale 国际化类型
	 * @return 验证成功返回值
	 */
	protected Integer checkInteger(String num, LKCodeEnum errorCode) {
		return checkInteger(num, errorCode, (Locale) request.getAttribute("locale"));
	}


	/**
	 * 验证数字
	 * @param num 待验证数字
	 * @param errorCode 错误编码
	 * @param locale 国际化类型
	 * @return 验证成功返回值
	 */
	protected Short checkShort(Short num, LKCodeEnum errorCode) {
		return checkShort(num, errorCode, (Locale) request.getAttribute("locale"));
	}


	/**
	 * 验证数字
	 * @param num 待验证数字
	 * @param errorCode 错误编码
	 * @param locale 国际化类型
	 * @return 验证成功返回值
	 */
	protected Short checkShort(String num, LKCodeEnum errorCode) {
		return checkShort(num, errorCode, (Locale) request.getAttribute("locale"));
	}


	/**
	 * 验证数字
	 * @param num 待验证数字
	 * @param errorCode 错误编码
	 * @param locale 国际化类型
	 * @return 验证成功返回值
	 */
	protected Byte checkByte(Byte num, LKCodeEnum errorCode) {
		return checkByte(num, errorCode, (Locale) request.getAttribute("locale"));
	}


	/**
	 * 验证数字
	 * @param num 待验证数字
	 * @param errorCode 错误编码
	 * @param locale 国际化类型
	 * @return 验证成功返回值
	 */
	protected Byte checkByte(String num, LKCodeEnum errorCode) {
		return checkByte(num, errorCode, (Locale) request.getAttribute("locale"));
	}

}
