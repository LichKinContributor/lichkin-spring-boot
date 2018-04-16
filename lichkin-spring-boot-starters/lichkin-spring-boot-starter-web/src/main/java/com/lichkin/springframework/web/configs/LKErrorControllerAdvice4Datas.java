package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lichkin.framework.defines.annotations.LKController4Datas;
import com.lichkin.framework.defines.beans.LKResponseBean;

/**
 * 数据请求控制器类错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@ControllerAdvice(annotations = LKController4Datas.class)
public class LKErrorControllerAdvice4Datas extends LKErrorControllerAdvice {

	/**
	 * 异常处理
	 * @param ex 异常
	 * @param request 请求对象
	 * @return 统一响应对象
	 */
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public LKResponseBean<Object> handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
		// 响应状态改为成功
		response.setStatus(HttpStatus.OK.value());

		// 异常处理
		return analyze2ResponseBean(ex, request);
	}

}
