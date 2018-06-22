package com.lichkin.springframework.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.lichkin.framework.defines.LKFramework;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.utils.i18n.LKI18NReader4ErrorCodes;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * 控制器类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
class LKController extends LKFramework {

	@Autowired
	protected HttpServletRequest request;


	/**
	 * 获取当前请求的国际化对象
	 * @return 国际化对象
	 */
	protected Locale getLocale() {
		return LKRequestUtils.getLocale(request);
	}


	/**
	 * 获取国际化错误信息
	 * @param errorCode 错误编码枚举类型
	 * @return 国际化错误信息
	 */
	protected String getI18NErrorMsg(LKCodeEnum errorCode) {
		return LKI18NReader4ErrorCodes.read(getLocale(), errorCode, null);
	}

}
