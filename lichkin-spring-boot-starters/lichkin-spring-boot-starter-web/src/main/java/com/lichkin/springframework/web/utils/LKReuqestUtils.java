package com.lichkin.springframework.web.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.lichkin.framework.defines.LKFrameworkStatics;

/**
 * 请求工具类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKReuqestUtils {

	/**
	 * 获取国际化类型
	 * @param request 请求对象
	 * @return 国际化类型
	 */
	public static Locale getLocale(HttpServletRequest request) {
		Object obj = request.getAttribute("locale");
		Locale locale = LKFrameworkStatics.DEFAULT_LOCALE;
		if (obj != null) {
			locale = (Locale) obj;
		}
		return locale;
	}

}
