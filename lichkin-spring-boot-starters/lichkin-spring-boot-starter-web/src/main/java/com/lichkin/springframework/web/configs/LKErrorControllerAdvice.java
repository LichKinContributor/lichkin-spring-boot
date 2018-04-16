package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;

import com.lichkin.framework.defines.beans.LKResponseBean;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.i18n.LKI18NReader4ErrorCodes;
import com.lichkin.springframework.web.utils.LKReuqestUtils;

/**
 * 控制器类错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKErrorControllerAdvice {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKErrorControllerAdvice.class);


	/**
	 * 异常处理
	 * @param ex 异常
	 * @param request 请求对象
	 * @return 统一响应对象
	 */
	public LKResponseBean<Object> analyze2ResponseBean(Exception ex, HttpServletRequest request) {
		// 根据异常类型确定错误编码
		LKCodeEnum errorCode = null;
		String exClassName = ex.getClass().getName();
		switch (exClassName) {
			case "com.lichkin.framework.defines.exceptions.LKRuntimeException":
				errorCode = ((LKRuntimeException) ex).getErrorCode();
			break;
			case "com.lichkin.framework.defines.exceptions.LKException":
				errorCode = ((LKException) ex).getErrorCode();
			break;
			case "com.lichkin.framework.defines.exceptions.LKFrameworkException":
				errorCode = LKErrorCodesEnum.CONFIG_ERROR;
			break;
			case "org.springframework.web.HttpRequestMethodNotSupportedException":
				errorCode = LKErrorCodesEnum.NOT_FOUND;
			break;
			case "org.springframework.dao.DataIntegrityViolationException":
				errorCode = LKErrorCodesEnum.DB_VALIDATE_ERROR;
			break;
			default:
				LOGGER.warn("exception[%s] catched by LKErrorControllerAdvice, but not declared errorCode, use INTERNAL_SERVER_ERROR by default.", exClassName);
				errorCode = LKErrorCodesEnum.INTERNAL_SERVER_ERROR;
			break;
		}

		// 处理错误编码值
		Integer code = errorCode.getCode();
		if (code == null) {
			code = -999;
		}

		// 返回统一响应对象
		return new LKResponseBean<>(code, LKI18NReader4ErrorCodes.read(LKReuqestUtils.getLocale(request), errorCode));
	}

}
