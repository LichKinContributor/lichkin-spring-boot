package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lichkin.framework.beans.LKResponseBean;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.web.annotations.LKController4Api;

/**
 * API数据请求控制器类执行错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@RestControllerAdvice(annotations = LKController4Api.class)
public class LKErrorControllerAdvice4Api {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKErrorControllerAdvice4Api.class);


	/**
	 * 异常处理
	 * @param ex 异常
	 * @param request 请求对象
	 * @return 统一响应对象
	 */
	@ExceptionHandler(value = Exception.class)
	public LKResponseBean<Object> handle(Exception ex, HttpServletRequest request) {
		return LKErrorLogger.logError(LOGGER, ex, request);
	}

}
