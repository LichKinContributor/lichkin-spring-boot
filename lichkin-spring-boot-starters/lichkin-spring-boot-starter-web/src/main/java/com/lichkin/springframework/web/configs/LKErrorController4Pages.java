package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.annotations.LKController4Pages;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;

/**
 * 页面请求无映射错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Pages
@Controller
public class LKErrorController4Pages {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKErrorController4Pages.class);


	@RequestMapping(value = "/**/*" + LKFrameworkStatics.WEB_MAPPING_PAGES, produces = "text/html")
	public ModelAndView noMapping(HttpServletRequest request) {
		LKErrorLogger.logError(LOGGER, new LKRuntimeException(LKErrorCodesEnum.NOT_FOUND), request);
		return new ModelAndView("/error/404");
	}

}
