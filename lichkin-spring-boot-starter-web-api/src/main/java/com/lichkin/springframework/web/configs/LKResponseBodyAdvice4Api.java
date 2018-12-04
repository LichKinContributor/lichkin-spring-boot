package com.lichkin.springframework.web.configs;

import static com.lichkin.springframework.web.LKRequestStatics.REQUEST_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.annotations.IgnoreLog;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * API数据请求响应处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@ControllerAdvice(annotations = LKController4Api.class)
public class LKResponseBodyAdvice4Api implements ResponseBodyAdvice<Object> {

	/** 日志对象 */
	protected final LKLog logger = LKLogFactory.getLog(getClass());


	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}


	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		HttpServletRequest req = LKRequestUtils.getRequest(request);

		// 404响应实际是完全正常的请求，因而也会进入本类执行，复杂的异常处理在LKErrorController4Datas中已经完成。
		if ((boolean) req.getAttribute("errorOccurs")) {
			return body;
		}

		// 统一响应格式
		@SuppressWarnings("unchecked")
		LKResponseBean<Object> responseBean = (LKResponseBean<Object>) body;

		// 分页数据特殊处理
		Object datas = responseBean.getDatas();
		if (datas == null) {
			logger.info(String.format("beforeBodyWrite -> {\"requestId\":\"%s\",\"responseDatas\":null}", req.getAttribute(REQUEST_ID)));
		} else {
			if (datas instanceof Page) {
				int totalPages = ((Page<?>) datas).getTotalPages();
				long totalElements = ((Page<?>) datas).getTotalElements();
				int size = ((Page<?>) datas).getSize();
				int number = ((Page<?>) datas).getNumber();
				int numberOfElements = ((Page<?>) datas).getNumberOfElements();
				boolean first = ((Page<?>) datas).isFirst();
				boolean last = ((Page<?>) datas).isLast();
				List<?> content = ((Page<?>) datas).getContent();

				Map<String, Object> map = new HashMap<>();
				map.put("totalPages", totalPages);
				map.put("totalElements", totalElements);
				map.put("size", size);
				map.put("number", number);
				map.put("numberOfElements", numberOfElements);
				map.put("first", first);
				map.put("last", last);
				map.put("content", content);
				responseBean = new LKResponseBean<>(map);

				String result = "[]";
				if (!content.isEmpty()) {
					result = "[...]";
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("beforeBodyWrite -> {\"requestId\":\"%s\",\"pageContentResult\":%s}", req.getAttribute(REQUEST_ID), LKJsonUtils.toJson(content)));
					}
				}

				logger.info(String.format("beforeBodyWrite -> {\"requestId\":\"%s\",\"responseDatas\":{\"totalPages\":%s,\"totalElements\":%s,\"size\":%s,\"number\":%s,\"numberOfElements\":%s,\"first\":%s,\"last\":%s,\"content\":%s}}", req.getAttribute(REQUEST_ID), totalPages, totalElements, size, number, numberOfElements, first, last, result));
			} else if (datas instanceof Map) {
				logger.info(String.format("beforeBodyWrite -> {\"requestId\":\"%s\",\"responseDatas\":\"%s\"}", req.getAttribute(REQUEST_ID), LKJsonUtils.toJsonWithExcludes(datas, new Class<?>[] { IgnoreLog.class }, "photo", "content", "img", "image", "requestDatas", "base64")));
			} else if (datas instanceof List) {
				String result = "[]";
				if (!((List<?>) datas).isEmpty()) {
					result = "[...]";
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("beforeBodyWrite -> {\"requestId\":\"%s\",\"listResult\":%s}", req.getAttribute(REQUEST_ID), LKJsonUtils.toJson(datas)));
					}
				}
				logger.info(String.format("beforeBodyWrite -> {\"requestId\":\"%s\",\"responseDatas\":%s}", req.getAttribute(REQUEST_ID), result));
			} else {
				logger.info(String.format("beforeBodyWrite -> {\"requestId\":\"%s\",\"responseDatas\":\"%s\"}", req.getAttribute(REQUEST_ID), LKJsonUtils.toJsonWithExcludes(datas, new Class<?>[] { IgnoreLog.class })));
			}
		}

		// 返回结果
		return responseBean;
	}

}
