package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.lichkin.framework.defines.annotations.LKController4Pages;

/**
 * 页面请求控制器类错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@ControllerAdvice(annotations = LKController4Pages.class)
public class LKErrorControllerAdvice4Pages extends LKErrorControllerAdvice {

	/**
	 * 异常处理
	 * @param ex 异常
	 * @param request 请求对象
	 * @return 统一响应对象
	 */
	@ExceptionHandler(value = Exception.class)
	public ModelAndView handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
		// 响应状态改为成功
		response.setStatus(HttpStatus.OK.value());

		// 异常处理
		ModelAndView mv = new ModelAndView("/error/500");
		mv.addObject("responseBean", analyze2ResponseBean(ex, request));
		return mv;
	}

}
