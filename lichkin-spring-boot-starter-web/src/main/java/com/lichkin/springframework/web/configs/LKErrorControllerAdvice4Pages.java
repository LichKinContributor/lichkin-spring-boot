package com.lichkin.springframework.web.configs;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.web.annotations.LKController4Pages;
import com.lichkin.springframework.web.beans.LKRequestInfo;

/**
 * 页面请求控制器类执行错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@ControllerAdvice(annotations = LKController4Pages.class)
public class LKErrorControllerAdvice4Pages {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKErrorControllerAdvice4Pages.class);

	@Autowired
	private ResourceUrlProvider provider;


	/**
	 * 异常处理
	 * @param ex 异常
	 * @param request 请求对象
	 * @return 模型视图
	 */
	@ExceptionHandler(value = Exception.class)
	public ModelAndView handle(Exception ex, HttpServletRequest request) {
		// 将异常信息存入属性中
		request.setAttribute("exceptionJson", LKJsonUtils.toJson(ex));
		// 记录日志
		LKErrorLogger.logError(LOGGER, ex, request);
		// 使用500页面响应
		ModelAndView mv = new ModelAndView("/error/500");

		LKRequestInfo requestInfo = (LKRequestInfo) request.getAttribute("requestInfo");
		String requestUri = requestInfo.getRequestUri();

		String viewName = requestUri.substring(0, requestUri.lastIndexOf(LKFrameworkStatics.WEB_MAPPING_PAGES));
		mv.addObject("mappingUri", viewName);

		Enumeration<String> parameterNames = request.getParameterNames();
		for (; parameterNames.hasMoreElements();) {
			String parameterName = parameterNames.nextElement();
			mv.addObject(parameterName, request.getParameter(parameterName));
		}

		// 存入mapping信息
		mv.addObject("mappingPages", LKFrameworkStatics.WEB_MAPPING_PAGES);
		mv.addObject("mappingApi", LKFrameworkStatics.WEB_MAPPING_API);
		mv.addObject("provider", provider);
		return mv;
	}

}
