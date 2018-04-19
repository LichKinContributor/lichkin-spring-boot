package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lichkin.framework.defines.annotations.LKController4Api;
import com.lichkin.framework.defines.beans.LKResponseBean;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;

/**
 * 数据请求控制器类执行错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@ControllerAdvice(annotations = LKController4Api.class)
public class LKErrorControllerAdvice4Api {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKErrorControllerAdvice4Api.class);


	/**
	 * 异常处理
	 * @param ex 异常
	 * @param request 请求对象
	 * @return 统一响应对象
	 */
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public LKResponseBean<Object> handle(Exception ex, HttpServletRequest request) {
		return LKErrorLogger.logError(LOGGER, ex, request);
	}

}
