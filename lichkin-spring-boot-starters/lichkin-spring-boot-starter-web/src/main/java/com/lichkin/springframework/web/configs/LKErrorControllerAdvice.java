package com.lichkin.springframework.web.configs;

import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lichkin.framework.defines.beans.LKResponseBean;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.i18n.LKI18NReader4ErrorCodes;

/**
 * 错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@ControllerAdvice
public class LKErrorControllerAdvice {

	/**
	 * 服务器异常处理
	 * @param ex 异常
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return JSON
	 */
	@ExceptionHandler(value = Exception.class)
	public Object handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = (Locale) request.getAttribute("locale");

		LKCodeEnum errorCode = null;
		if (ex instanceof LKRuntimeException) {
			errorCode = ((LKRuntimeException) ex).getErrorCode();
		} else if (ex instanceof LKException) {
			errorCode = ((LKException) ex).getErrorCode();
		} else if (ex instanceof LKFrameworkException) {
			errorCode = LKErrorCodesEnum.CONFIG_ERROR;
		} else if (ex instanceof HttpRequestMethodNotSupportedException) {
			errorCode = LKErrorCodesEnum.NOT_FOUND;
			response.setStatus(HttpStatus.NOT_FOUND.value());
		} else {
			errorCode = LKErrorCodesEnum.INTERNAL_SERVER_ERROR;
		}

		LKResponseBean<Object> responseBean = new LKResponseBean<>(errorCode.getCode(), LKI18NReader4ErrorCodes.read(locale, errorCode));

		if (request.getRequestURI().endsWith(".html")) {
			// 页面请求异常处理
			ModelAndView mv = new ModelAndView("/error/500");
			mv.addObject("responseBean", responseBean);
			return mv;
		} else {
			// 数据请求异常处理
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				PrintWriter writer = response.getWriter();
				writer.write(new ObjectMapper().writeValueAsString(responseBean));
				writer.close();
				response.flushBuffer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
