package com.lichkin.springframework.web.configs;

import static com.lichkin.springframework.web.LKRequestStatics.REQUEST_ID;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lichkin.framework.defines.LKConfigStatics;
import com.lichkin.framework.defines.LKSessionStatics;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.framework.web.annotations.WithoutLogin;
import com.lichkin.springframework.controllers.LKPagesController;

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

		if (LKClassUtils.checkExtendsClass(controllerClass, LKPagesController.class) && (method.getAnnotation(WithoutLogin.class) == null) && (request.getSession().getAttribute(LKSessionStatics.KEY_LOGIN) == null)) {
			response.sendRedirect(LKConfigStatics.WEB_CONTEXT_PATH + "/index");
		}

		if (LOGGER.isDebugEnabled()) {
			String requestId = (String) request.getAttribute(REQUEST_ID);
			LOGGER.debug(String.format("preHandle -> {\"requestId\":\"%s\",\"handlerClassName\":\"%s\",\"handlerMethodName\":\"%s\"}", requestId, controllerClass.getName(), method.getName()));
		}
		return true;
	}

}
