package com.lichkin.springframework.web.configs;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * 视图解析器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKFreeMarkerViewResolver extends FreeMarkerViewResolver {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKFreeMarkerViewResolver.class);


	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		View view = super.resolveViewName(viewName, locale);
		if (view == null) {
			HttpServletRequest request = LKRequestUtils.getRequest();
			LKFrameworkException ex = new LKFrameworkException("FreeMarker template doesn't exsit.");
			// 将异常信息存入属性中
			request.setAttribute("exceptionJson", LKJsonUtils.toJson(ex));
			// 记录日志
			LKErrorLogger.logError(LOGGER, ex, request);
			// 使用500页面响应
			return createView("/error/500", locale);
		}
		return view;
	}

}
