package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKIpUtils;
import com.lichkin.framework.utils.LKRandomUtils;

public class LKHandlerInterceptor implements HandlerInterceptor {

	/** 日志对象 */
	protected final LKLog logger = LKLogFactory.getLog(getClass());


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (logger.isDebugEnabled()) {
			DateTime requestTime = DateTime.now();
			String requestId = LKRandomUtils.create(32);
			String requestUri = request.getRequestURI();
			String requestIp = LKIpUtils.getIp(request);
			logger.debug("{\"requestId\":\"%s\",\"requestUri\":\"%s\",\"requestIp\":\"%s\",\"requestTime\":\"%s\"}", requestId, requestUri, requestIp, LKDateTimeUtils.toString(requestTime));
			request.setAttribute("requestId", requestId);
			request.setAttribute("requestUri", requestUri);
			request.setAttribute("requestIp", requestIp);
			request.setAttribute("requestTime", requestTime);
		}
		return true;
	}


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}


	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if (logger.isDebugEnabled()) {
			DateTime requestTime = (DateTime) request.getAttribute("requestTime");
			DateTime responseTime = DateTime.now();
			long elapsedTime = responseTime.getMillis() - requestTime.getMillis();
			String requestId = (String) request.getAttribute("requestId");
			String requestUri = (String) request.getAttribute("requestUri");
			String requestIp = (String) request.getAttribute("requestIp");
			logger.debug("{\"requestId\":\"%s\",\"requestUri\":\"%s\",\"requestIp\":\"%s\",\"requestTime\":\"%s\",\"responseTime\":\"%s\",\"elapsedTime\":\"%s\"}", requestId, requestUri, requestIp, LKDateTimeUtils.toString(requestTime), LKDateTimeUtils.toString(responseTime), String.valueOf(elapsedTime));
		}
	}

}
