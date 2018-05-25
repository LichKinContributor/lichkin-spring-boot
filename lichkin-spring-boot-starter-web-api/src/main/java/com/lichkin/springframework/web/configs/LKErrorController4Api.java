package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lichkin.framework.beans.LKResponseBean;
import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.web.annotations.LKController4Api;

/**
 * API数据请求无映射错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
@RestController
public class LKErrorController4Api extends LKErrorController {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKErrorController4Api.class);


	@RequestMapping(value = LKFrameworkStatics.WEB_MAPPING_API + "/**", produces = "application/json")
	public LKResponseBean<Object> noMapping(HttpServletRequest request, HttpServletResponse response) {
		return LKErrorLogger.logError(LOGGER, new LKRuntimeException(LKErrorCodesEnum.NOT_FOUND), request);
	}

}
