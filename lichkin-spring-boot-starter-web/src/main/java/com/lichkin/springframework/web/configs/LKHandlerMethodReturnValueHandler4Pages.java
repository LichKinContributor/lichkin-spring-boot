package com.lichkin.springframework.web.configs;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.utils.LKStringUtils;
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
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

		LKRequestInfo requestInfo = (LKRequestInfo) request.getAttribute("requestInfo");
		String requestUri = requestInfo.getRequestUri();

		// 根据requestUri动态设定视图名，视图名为请求对应的目录的模板。
		String viewName = requestUri.substring(0, requestUri.lastIndexOf(LKFrameworkStatics.WEB_MAPPING_PAGES));
		if (returnValue == null) {
			mavContainer.addAttribute("mappingUri", viewName);
			mavContainer.setViewName(viewName);
		} else {
			String subViewName = ((LKPage) returnValue).getViewName();
			if (subViewName == null) {
				mavContainer.addAttribute("mappingUri", viewName);
				mavContainer.setViewName(viewName);
			} else {
				if (subViewName.equals(LKPage.BLANK)) {
					mavContainer.addAttribute("mappingUri", viewName);
					mavContainer.setViewName(LKPage.BLANK);
				} else {
					viewName += LKStringUtils.toStandardPath(subViewName);
					mavContainer.addAttribute("mappingUri", viewName);
					mavContainer.setViewName(viewName);
				}
			}
		}

		// 将请求参数放入视图模型中
		Enumeration<String> parameterNames = request.getParameterNames();
		for (; parameterNames.hasMoreElements();) {
			String parameterName = parameterNames.nextElement();
			mavContainer.addAttribute(parameterName, request.getParameter(parameterName));
		}

		// 将数据放入视图模型中
		if (returnValue != null) {
			mavContainer.addAllAttributes(((LKPage) returnValue).getAttributes());
		}

		// 将serverDatas放入视图模型中
		if (returnValue == null) {
			mavContainer.addAttribute("serverDatas", new HashedMap());
			mavContainer.addAttribute("serverDatasJson", "{}");
		} else {
			Map<String, Object> serverDatas = ((LKPage) returnValue).getServerDatas();
			mavContainer.addAttribute("serverDatas", serverDatas);
			mavContainer.addAttribute("serverDatasJson", LKJsonUtils.toJson(serverDatas));
		}

		// 存入mapping信息
		mavContainer.addAttribute("mappingPages", LKFrameworkStatics.WEB_MAPPING_PAGES);
		mavContainer.addAttribute("mappingApi", LKFrameworkStatics.WEB_MAPPING_API);

		// 存入backUrl信息
		mavContainer.addAttribute("backUrl", StringUtils.trimToEmpty(request.getParameter("backUrl")));
	}

}
