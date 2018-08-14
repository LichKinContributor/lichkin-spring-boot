package com.lichkin.springframework.web.configs;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lichkin.framework.defines.LKConfigStatics;
import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.framework.web.annotations.WithoutLogin;
import com.lichkin.springframework.controllers.LKPagesController;
import com.lichkin.springframework.web.LKSession;
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
		HandlerMethod handler = (HandlerMethod) obj;
		Class<?> controllerClass = handler.getBean().getClass();
		Method method = handler.getMethod();

		if (LKClassUtils.checkExtendsClass(controllerClass, LKPagesController.class) && (method.getAnnotation(WithoutLogin.class) == null) && (LKSession.getLogin(request.getSession()) == null)) {
			response.sendRedirect(LKConfigStatics.WEB_CONTEXT_PATH + "/index");
		}

		LKRequestInfo requestInfo = (LKRequestInfo) request.getAttribute("requestInfo");
		requestInfo.setHandlerClassName(controllerClass.getName());
		requestInfo.setHandlerMethod(method.getName());
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
