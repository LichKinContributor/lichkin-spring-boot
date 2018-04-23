package com.lichkin.springframework.web.configs.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.LKIpUtils;
import com.lichkin.springframework.web.beans.LKRequestInfo;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * 过滤器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKFilter implements Filter {

	/** 日志对象 */
	private final LKLog logger = LKLogFactory.getLog(getClass());


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 调用链前处理
		beforeChain(request, response, chain);

		// 调用链
		chain.doFilter(request, response);

		// 调用链后处理
		afterChain(request, response, chain);
	}


	/**
	 * 调用链后处理
	 * @param request ServletRequest
	 * @param response ServletResponse
	 * @param chain FilterChain
	 */
	protected void beforeChain(ServletRequest request, ServletResponse response, FilterChain chain) {
		// 所有约定的请求都会访问到过滤器
		LKRequestInfo requestInfo = new LKRequestInfo();

		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			requestInfo.setRequestUri(LKRequestUtils.getRequestURI(req));
			requestInfo.setRequestIp(LKIpUtils.getIp(req));

			if (logger.isDebugEnabled()) {
				logger.debug(LKJsonUtils.toJsonWithIncludes(requestInfo, "requestId", "requestTime", "requestUri", "requestIp"));
			}

			request.setAttribute("requestInfo", requestInfo);
			request.setAttribute("errorOccurs", false);
		} else {
			// 框架没有约定的情况
			logger.error(LKJsonUtils.toJsonWithIncludes(requestInfo, "requestId", "requestTime"));
			throw new LKRuntimeException(LKErrorCodesEnum.CONFIG_ERROR);
		}
	}


	/**
	 * 调用链前处理
	 * @param request ServletRequest
	 * @param response ServletResponse
	 * @param chain FilterChain
	 */
	protected void afterChain(ServletRequest request, ServletResponse response, FilterChain chain) {
	}


	@Override
	public void destroy() {
	}

}
