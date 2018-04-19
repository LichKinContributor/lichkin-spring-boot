package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.lichkin.framework.defines.annotations.LKController4Pages;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;

/**
 * 页面请求控制器类执行错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@ControllerAdvice(annotations = LKController4Pages.class)
public class LKErrorControllerAdvice4Pages {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKErrorControllerAdvice4Pages.class);


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
		return new ModelAndView("/error/500");
	}

}
