package com.lichkin.springframework.web.configs;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.web.annotations.LKController4Pages;
import com.lichkin.springframework.web.beans.LKRequestInfo;

/**
 * 页面请求无映射错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Pages
@Controller
public class LKErrorController4Pages extends LKErrorController {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKErrorController4Pages.class);


	@RequestMapping(value = "/**/*" + LKFrameworkStatics.WEB_MAPPING_PAGES, produces = "text/html")
	public ModelAndView noMapping(HttpServletRequest request) {
		// 记录日志
		LKErrorLogger.logError(LOGGER, new LKRuntimeException(LKErrorCodesEnum.NOT_FOUND), request);
		// 使用404页面响应
		ModelAndView mv = new ModelAndView("/error/404");

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
		return mv;
	}

}
