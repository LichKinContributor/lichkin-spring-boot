package com.lichkin.springframework.web.configs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.beans.LKResponseBean;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.utils.i18n.LKI18NReader4ErrorCodes;
import com.lichkin.springframework.web.beans.LKRequestInfo;
import com.lichkin.springframework.web.beans.LKResponseInfo;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * 错误日志工具类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
class LKErrorLogger {

	/**
	 * 输出错误日志
	 * @param logger 日志对象
	 * @param ex Exception
	 * @param request HttpServletRequest
	 */
	static LKResponseBean<Object> logError(LKLog logger, Exception ex, HttpServletRequest request) {
		// 统一响应对象
		LKResponseBean<Object> responseBean = null;

		// 根据异常类型确定错误编码
		LKCodeEnum errorCode = null;
		String errorMessage = null;
		String exClassName = ex.getClass().getName();

		// 取请求对象并设置值
		LKRequestInfo requestInfo = (LKRequestInfo) request.getAttribute("requestInfo");
		requestInfo.setExceptionClassName(exClassName);
		requestInfo.setExceptionMessage(ex.getMessage());

		// 根据不通异常进行响应内容预处理
		boolean excludeLogExceptions = false;
		switch (exClassName) {
			case "com.lichkin.framework.defines.exceptions.LKRuntimeException": {
				excludeLogExceptions = true;
				errorCode = ((LKRuntimeException) ex).getErrorCode();
			}
			break;
			case "com.lichkin.framework.defines.exceptions.LKException": {
				excludeLogExceptions = true;
				errorCode = ((LKException) ex).getErrorCode();
			}
			break;
			case "com.lichkin.framework.defines.exceptions.LKFrameworkException": {
				excludeLogExceptions = true;
				errorCode = LKErrorCodesEnum.CONFIG_ERROR;
				if (((LKFrameworkException) ex).isShowMessage()) {
					errorMessage = ((LKFrameworkException) ex).getMessage();
				}
			}
			break;
			case "org.springframework.dao.DataIntegrityViolationException": {
				// 输出到控制台
				if (LKFrameworkStatics.SYSTEM_DEBUG) {
					ex.printStackTrace();
				}
				errorCode = LKErrorCodesEnum.DB_VALIDATE_ERROR;
			}
			break;
			case "org.springframework.web.bind.MethodArgumentNotValidException": {
				excludeLogExceptions = true;
				// 输出到控制台
				if (LKFrameworkStatics.SYSTEM_DEBUG) {
					ex.printStackTrace();
				}
				errorCode = LKErrorCodesEnum.PARAM_ERROR;
				List<ObjectError> errors = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
				StringBuffer sb = new StringBuffer();
				for (ObjectError error : errors) {
					sb.append(LKI18NReader4ErrorCodes.read(LKRequestUtils.getLocale(request), "validation@" + error.getCodes()[0])).append("@#@");
				}
				errorMessage = sb.toString();
			}
			break;
			default: {
				// 输出到控制台
				if (LKFrameworkStatics.SYSTEM_DEBUG) {
					ex.printStackTrace();
				}
				errorCode = LKErrorCodesEnum.INTERNAL_SERVER_ERROR;
				logger.warn("exception[%s] catched by Framework, but not declared errorCode, use INTERNAL_SERVER_ERROR by default.", exClassName);
			}
			break;
		}

		// 处理错误编码值
		Integer code = errorCode.getCode();
		if (code == null) {
			code = -999;
		}

		// 设置属性
		request.setAttribute("errorOccurs", true);
		request.setAttribute("requestInfoJson", LKJsonUtils.toJson(requestInfo));

		if (requestInfo.getRequestUri().endsWith(LKFrameworkStatics.WEB_MAPPING_PAGES)) {
			// 页面请求无需处理响应对象
			// 记录日志
			if (excludeLogExceptions) {
				logger.error(LKJsonUtils.toJsonWithExcludes(new LKResponseInfo(requestInfo, null), "responseBean"));
			} else {
				logger.error(LKJsonUtils.toJsonWithExcludes(new LKResponseInfo(requestInfo, null), "responseBean"), ex);
			}
		} else {
			// 数据请求需处理响应对象
			// 解析响应对象
			responseBean = new LKResponseBean<>(code, (errorMessage == null) ? LKI18NReader4ErrorCodes.read(LKRequestUtils.getLocale(request), errorCode) : errorMessage);

			// 记录日志
			if (excludeLogExceptions) {
				logger.error(LKJsonUtils.toJson(new LKResponseInfo(requestInfo, responseBean)));
			} else {
				logger.error(LKJsonUtils.toJson(new LKResponseInfo(requestInfo, responseBean)), ex);
			}
		}

		// 返回统一响应格式
		return responseBean;
	}

}
