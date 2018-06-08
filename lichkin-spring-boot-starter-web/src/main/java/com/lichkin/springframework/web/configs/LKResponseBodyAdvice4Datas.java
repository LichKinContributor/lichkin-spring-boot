package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.web.annotations.LKController4Datas;
import com.lichkin.springframework.web.beans.LKRequestInfo;
import com.lichkin.springframework.web.beans.LKResponseInfo;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * 数据请求响应处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@ControllerAdvice(annotations = LKController4Datas.class)
public class LKResponseBodyAdvice4Datas implements ResponseBodyAdvice<Object> {

	/** 日志对象 */
	protected final LKLog logger = LKLogFactory.getLog(getClass());


	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		HttpServletRequest req = LKRequestUtils.getRequest(request);

		// 404响应实际是完全正常的请求，因而也会进入本类执行，复杂的异常处理在LKErrorController4Datas中已经完成。
		if ((boolean) req.getAttribute("errorOccurs")) {
			return body;
		}

		// 统一响应格式
		LKResponseBean<Object> responseBean = null;
		if (body instanceof LKResponseBean) {
			responseBean = (LKResponseBean<Object>) body;
		} else {
			responseBean = new LKResponseBean<>(body);
		}

		// 记录日志
		logger.info(LKJsonUtils.toJsonWithExcludes(new LKResponseInfo((LKRequestInfo) req.getAttribute("requestInfo"), responseBean), "exceptionClassName", "exceptionMessage"));

		// 返回结果
		return responseBean;
	}

}
