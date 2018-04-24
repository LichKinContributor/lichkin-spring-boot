package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.springframework.web.beans.LKPage;
import com.lichkin.springframework.web.beans.LKRequestInfo;

/**
 * 页面请求响应数据处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKHandlerMethodReturnValueHandler4Pages implements HandlerMethodReturnValueHandler {

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.getParameterType() == LKPage.class;
	}


	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
		LKRequestInfo requestInfo = (LKRequestInfo) ((HttpServletRequest) webRequest.getNativeRequest()).getAttribute("requestInfo");
		String requestUri = requestInfo.getRequestUri();

		// 根据requestUri动态设定视图名，视图名为请求对应的目录的模板。
		String viewName = requestUri.substring(0, requestUri.lastIndexOf(LKFrameworkStatics.WEB_MAPPING_PAGES));
		mavContainer.addAttribute("mappingUri", viewName);
		mavContainer.setViewName(viewName);

		// 将数据放入视图模型中
		if (returnValue != null) {
			mavContainer.addAllAttributes(((LKPage) returnValue).getAttributes());
		}
	}

}