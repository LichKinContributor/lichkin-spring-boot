package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.springframework.web.beans.LKRequestInfo;
import com.lichkin.springframework.web.beans.LKResponseInfo;

/**
 * 拦截器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKHandlerInterceptor implements HandlerInterceptor {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKHandlerInterceptor.class);


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		LKRequestInfo requestInfo = (LKRequestInfo) request.getAttribute("requestInfo");
		HandlerMethod handler = (HandlerMethod) obj;
		Class<?> controllerClass = handler.getBean().getClass();
		requestInfo.setHandlerClassName(controllerClass.getName());
		requestInfo.setHandlerMethod(handler.getMethod().getName());
		String requestInfoJson = LKJsonUtils.toJsonWithExcludes(requestInfo, "exceptionClassName", "exceptionMessage");
		request.setAttribute("requestInfoJson", requestInfoJson);
		LOGGER.info(requestInfoJson);
		return true;
	}


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}


	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		LKRequestInfo requestInfo = (LKRequestInfo) request.getAttribute("requestInfo");
		if (requestInfo.getRequestUri().endsWith(LKFrameworkStatics.WEB_MAPPING_PAGES) && !(boolean) request.getAttribute("errorOccurs")) {
			LOGGER.info(LKJsonUtils.toJsonWithExcludes(new LKResponseInfo(requestInfo, null), "exceptionClassName", "exceptionMessage", "responseBean"));
		}
	}

}
