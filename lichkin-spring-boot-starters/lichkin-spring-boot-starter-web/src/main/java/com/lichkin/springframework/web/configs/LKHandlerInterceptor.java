package com.lichkin.springframework.web.configs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.annotations.LKController4Api;
import com.lichkin.framework.defines.annotations.LKController4Datas;
import com.lichkin.framework.defines.annotations.LKController4Pages;
import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.springframework.web.beans.LKRequestInfo;
import com.lichkin.springframework.web.beans.LKResponseInfo;

/**
 * 拦截器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKHandlerInterceptor implements HandlerInterceptor {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKHandlerInterceptor.class);


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		// 在LKWebMvcConfigurerAdapter中声明了本拦截器只拦截框架约定的请求。
		// 并且在ErrorController中映射了所有约定的请求格式。
		// 因而所有框架约定的请求将会进入本拦截器。
		// 在此之前过滤器中已经请求信息进行过初始化，此处只需取出即可。
		LKRequestInfo requestInfo = (LKRequestInfo) request.getAttribute("requestInfo");
		String requestUri = requestInfo.getRequestUri();

		requestInfo.setHandlerClassName(obj.getClass().getName());

		if (!(obj instanceof HandlerMethod)) {
			requestInfo.setExceptionMessage("handler is not a HandlerMethod.");
			LOGGER.error(LKJsonUtils.toJsonWithIncludes(new LKResponseInfo(requestInfo, null), "requestId", "requestTime", "requestUri", "requestIp", "handlerClassName", "exceptionMessage", "responseTime", "elapsedTime"));
			// 框架没有约定的情况
			return false;
		}

		// 强转方便处理
		HandlerMethod handler = (HandlerMethod) obj;

		// TODO 进行框架约定的处理
		// 按照约定控制器应严格区分数据请求和页面请求，不能写在同一控制器中，还有API对数据请求更细化的约定。
		// 此类相关但校验在拦截器中虽然可以实现，但并不合适，应在项目启动时校验比较好，后续需要将以下代码转移。
		Class<?> controllerClass = handler.getBean().getClass();
		requestInfo.setHandlerClassName(controllerClass.getName());
		requestInfo.setHandlerMethod(handler.getMethod().getName());

		if (requestUri.startsWith(LKFrameworkStatics.WEB_MAPPING_API)) {
			// API数据请求需使用LKController4Api注解
			LKController4Api annotationApi = controllerClass.getAnnotation(LKController4Api.class);
			if (annotationApi == null) {
				// 不符合框架约定，进行拦截。
				LKErrorLogger.logError(LOGGER, new LKFrameworkException("can't find @LKController4Api on controlelr."), request);
				return false;
			}
		} else if (requestUri.endsWith(LKFrameworkStatics.WEB_MAPPING_DATAS)) {
			// 数据请求需使用LKController4Datas注解
			LKController4Datas annotationDatas = controllerClass.getAnnotation(LKController4Datas.class);
			if (annotationDatas == null) {
				// 不符合框架约定，进行拦截。
				LKErrorLogger.logError(LOGGER, new LKFrameworkException("can't find @LKController4Datas on controlelr."), request);
				return false;
			}
		} else if (requestUri.endsWith(LKFrameworkStatics.WEB_MAPPING_PAGES)) {
			// 页面请求需使用LKController4Pages注解
			LKController4Pages annotationPages = controllerClass.getAnnotation(LKController4Pages.class);
			if (annotationPages == null) {
				// 不符合框架约定，进行拦截。
				LKErrorLogger.logError(LOGGER, new LKFrameworkException("can't find @LKController4Pages on controlelr."), request);
				return false;
			}
		} else {
			// 理论上不会出现该情况
			LKErrorLogger.logError(LOGGER, new LKFrameworkException("inconceivable!!!"), request);
			return false;
		}

		// 记录日志
		String requestInfoJson = LKJsonUtils.toJsonWithExcludes(requestInfo, "exceptionClassName", "exceptionMessage");
		request.setAttribute("requestInfoJson", requestInfoJson);
		LOGGER.info(requestInfoJson);

		// 到这里拦截器的任务完成了，放行。
		return true;
	}


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO 实际应用场景中，这里到底能干什么呢？
	}


	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// 数据请求响应时，此处只能通过复制响应流的方式来输出日志，很不合适。
		// 数据请求错误相关处理
		// 0、无异常情况时。Filter -> HandlerInterceptor -> Controller -> ResponseBodyAdvice -> HandlerInterceptor -> Filter
		// 1、无映射时由ErrorController接收请求，使用LKErrorCodesEnum.NOT_FOUND响应。
		// 2、控制器类执行时发生的异常，由ControllerAdvice捕获。
		// -> LKRuntimeException/LKException将取对应的错误编码响应。
		// -> LKFrameworkException使用LKErrorCodesEnum.CONFIG_ERROR响应。
		// -> DataIntegrityViolationException使用LKErrorCodesEnum.DB_VALIDATE_ERROR响应。
		// -> MethodArgumentNotValidException使用LKErrorCodesEnum.PARAM_ERROR响应。
		// TODO -> 其它类型异常有待完善，目前统一使用LKErrorCodesEnum.INTERNAL_SERVER_ERROR响应。

		// 页面请求此处可以进行一些处理
		// 页面请求相关的处理
		// 0、无异常情况时。LKFilter -> HandlerInterceptor -> Controller -> HandlerInterceptor -> Filter4Pages
		// 1、无映射时由ErrorController接收请求，使用404页面响应。
		// 2、控制器类执行时发生的异常，由ControllerAdvice捕获，使用500页面响应。
		// 3、控制器类执行完成，返回视图待解析时，找不到视图时，被LKFreeMarkerViewResolver捕获了，会响应500页面。
		// TODO 4、控制器类执行完成，返回视图待解析时，视图中产生bug，暂未被框架捕获，springMVC框架输出错误日志。
		// 综上所述，所有异常情况的产生都被处理了，其中1、2的异常在拦截器链内，3、4的异常在拦截器链外。
		LKRequestInfo requestInfo = (LKRequestInfo) request.getAttribute("requestInfo");
		if (requestInfo.getRequestUri().endsWith(LKFrameworkStatics.WEB_MAPPING_PAGES) && !(boolean) request.getAttribute("errorOccurs")) {
			LOGGER.info(LKJsonUtils.toJsonWithExcludes(new LKResponseInfo(requestInfo, null), "exceptionClassName", "exceptionMessage", "responseBean"));
		}
	}

}
