package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lichkin.framework.defines.beans.LKResponseBean;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.utils.i18n.LKI18NReader4ErrorCodes;
import com.lichkin.springframework.web.utils.LKReuqestUtils;

/**
 * 错误处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Controller
public class LKErrorController implements ErrorController {

	private static final String ERROR_PATH = "/error";


	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}


	@GetMapping(value = ERROR_PATH, produces = "text/html")
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		// 新建模型视图
		ModelAndView mv = new ModelAndView();

		// 使用对应的异常页面响应
		HttpStatus status = HttpStatus.valueOf(response.getStatus());
		LKCodeEnum errorCode = null;
		switch (status) {
			case NOT_FOUND:
				errorCode = LKErrorCodesEnum.NOT_FOUND;
				mv.setViewName("/error/404");
			break;
			case INTERNAL_SERVER_ERROR:
				errorCode = LKErrorCodesEnum.INTERNAL_SERVER_ERROR;
				mv.setViewName("/error/500");
			break;
			default:
				// 暂未实现的使用统一的error页面进行响应
				errorCode = LKErrorCodesEnum.CONFIG_ERROR;
				mv.setViewName("/error/error");
			break;
		}

		// 响应状态改为成功
		response.setStatus(HttpStatus.OK.value());

		// 向页面传递相关信息
		mv.addObject("responseBean", new LKResponseBean<>(errorCode.getCode(), LKI18NReader4ErrorCodes.read(LKReuqestUtils.getLocale(request), errorCode)));

		// 返回模型视图
		return mv;
	}


	@ResponseBody
	@PostMapping(value = ERROR_PATH, produces = "application/json")
	public LKResponseBean<Object> errorJson(HttpServletRequest request, HttpServletResponse response) {
		// 使用对应的异常信息响应
		HttpStatus status = HttpStatus.valueOf(response.getStatus());
		LKCodeEnum errorCode = null;
		switch (status) {
			case NOT_FOUND:
				errorCode = LKErrorCodesEnum.NOT_FOUND;
			break;
			case INTERNAL_SERVER_ERROR:
				errorCode = LKErrorCodesEnum.INTERNAL_SERVER_ERROR;
			break;
			default:
				errorCode = LKErrorCodesEnum.CONFIG_ERROR;
			break;
		}

		// 响应状态改为成功
		response.setStatus(HttpStatus.OK.value());

		// 返回统一响应数据
		return new LKResponseBean<>(errorCode.getCode(), LKI18NReader4ErrorCodes.read(LKReuqestUtils.getLocale(request), errorCode));
	}

}
