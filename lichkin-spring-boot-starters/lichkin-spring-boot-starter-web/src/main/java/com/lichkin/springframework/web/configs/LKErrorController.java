package com.lichkin.springframework.web.configs;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lichkin.framework.defines.beans.LKResponseBean;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.utils.i18n.LKI18NReader4ErrorCodes;

@Controller
public class LKErrorController implements ErrorController {

	private static final String ERROR_PATH = "/error";


	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}


	@GetMapping(value = ERROR_PATH, produces = "text/html")
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		// 页面请求异常处理
		ModelAndView mv = new ModelAndView("/error/404");
		LKCodeEnum errorCode = LKErrorCodesEnum.CONFIG_ERROR;
		mv.addObject("responseBean", new LKResponseBean<>(errorCode.getCode(), LKI18NReader4ErrorCodes.read(Locale.ENGLISH, errorCode)));
		return mv;
	}

}
