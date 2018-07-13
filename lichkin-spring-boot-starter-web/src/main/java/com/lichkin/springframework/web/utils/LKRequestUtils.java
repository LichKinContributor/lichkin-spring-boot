package com.lichkin.springframework.web.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.utils.LKStringUtils;
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
		if (!locale.getLanguage().equals("en")) {
			for (Locale implementedLocale : LKFrameworkStatics.IMPLEMENTED_LOCALE_ARR) {
				if (implementedLocale.equals(locale)) {
					return locale;
				}
			}
		}
		return Locale.ENGLISH;
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
				return locale;
			}
		}

		// 从参数中取
		Locale parameter = LKI18NUtils.getLocale(request.getParameter("locale"), null);
		if (parameter != null) {
			return parameter;
		}

		// 从头中取
		Locale header = LKI18NUtils.getLocale(request.getHeader("Accept-Language"), null);
		if (header != null) {
			return header;
		}

		// 取默认值
		Locale locale = request.getLocale();
		if ((locale != null) && !locale.equals(Locale.ROOT)) {
			return locale;
		}

		// 返回默认值
		return LKFrameworkStatics.DEFAULT_LOCALE;
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


	/**
	 * 获取请求路径
	 * @param request 请求对象
	 * @return 请求路径
	 */
	public static String getRequestURI(HttpServletRequest request) {
		final String cotextPath = request.getContextPath();
		String requestURI = request.getRequestURI().replaceFirst(cotextPath, "");
		if (requestURI.contains(";")) {
			requestURI = requestURI.substring(0, requestURI.indexOf(";"));
		}
		return requestURI;
	}


	/**
	 * 获取用户类型对应的服务类型
	 * @param request 请求对象
	 * @return 用户类型对应的服务类型
	 * @throws ClassNotFoundException 没有实现该类时将抛出异常
	 */
	public static Class<?> getApiUserService(HttpServletRequest request) throws ClassNotFoundException {
		String requestURI = getRequestURI(request);
		String userType = requestURI.substring(LKStringUtils.indexOf(requestURI, "/", 0) + 1, LKStringUtils.indexOf(requestURI, "/", 1));
		return Class.forName(String.format("com.lichkin.application.services.impl.Sys%sLoginService", userType));
	}

}
