package com.lichkin.springframework.web.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.utils.i18n.LKI18NUtils;

/**
 * 请求工具类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKRequestUtils {

	/**
	 * 获取国际化类型
	 * @param request 请求对象
	 * @return 国际化类型
	 */
	public static Locale getLocale(HttpServletRequest request) {
		Locale locale = analyzeLocale(request);
		if (locale.getLanguage().equals("en")) {
			return Locale.ENGLISH;
		}
		return locale;
	}


	/**
	 * 解析国际化类型
	 * @param request 请求对象
	 * @return 国际化类型
	 */
	private static Locale analyzeLocale(HttpServletRequest request) {
		// 从属性中取
		Object attribut = request.getAttribute("locale");
		if (attribut != null) {
			Locale locale = LKI18NUtils.getLocale(((Locale) attribut).toString(), null);
			if (locale != null) {
				request.setAttribute("locale", locale);
				return locale;
			}
		}

		// 从参数中取
		Locale parameter = LKI18NUtils.getLocale(request.getParameter("locale"), null);
		if (parameter != null) {
			request.setAttribute("locale", parameter);
			return parameter;
		}

		// 从头中取
		Locale header = LKI18NUtils.getLocale(request.getHeader("Accept-Language"), null);
		if (header != null) {
			request.setAttribute("locale", header);
			return header;
		}

		// 取默认值
		Locale locale = request.getLocale();
		if ((locale != null) && !locale.equals(Locale.ROOT)) {
			request.setAttribute("locale", locale);
			return locale;
		}

		// 返回默认值
		locale = LKFrameworkStatics.DEFAULT_LOCALE;
		request.setAttribute("locale", locale);
		return locale;
	}


	/**
	 * 获取当前请求对象
	 * @return 请求对象
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}


	/**
	 * 获取当前请求对象
	 * @param request 请求对象
	 * @return 请求对象
	 */
	public static HttpServletRequest getRequest(ServerHttpRequest request) {
		return ((ServletServerHttpRequest) request).getServletRequest();
	}

}
