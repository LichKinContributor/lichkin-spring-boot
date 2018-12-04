package com.lichkin.springframework.web.configs.filters;

import static com.lichkin.springframework.web.LKRequestStatics.REQUEST_ID;
import static com.lichkin.springframework.web.LKRequestStatics.REQUEST_IP;
import static com.lichkin.springframework.web.LKRequestStatics.REQUEST_TIME;
import static com.lichkin.springframework.web.LKRequestStatics.REQUEST_URI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.joda.time.DateTime;
import org.springframework.util.StreamUtils;

import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKIpUtils;
import com.lichkin.framework.utils.LKRandomUtils;
import com.lichkin.springframework.web.utils.LKRequestUtils;

import lombok.Cleanup;

/**
 * 过滤器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKFilter implements Filter {

	/** 日志对象 */
	protected final LKLog logger = LKLogFactory.getLog(getClass());


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 包装请求对象
//		LKHttpServletRequestWrapper req = new LKHttpServletRequestWrapper((HttpServletRequest) request);

		// 调用链前处理
		beforeChain(request, response, chain);

		// 调用链
		chain.doFilter(request, response);

		// 调用链后处理
		afterChain(request, response, chain);
	}


	/**
	 * 调用链后处理
	 * @param request LKHttpServletRequestWrapper
	 * @param response ServletResponse
	 * @param chain FilterChain
	 */
	protected void beforeChain(ServletRequest request, ServletResponse response, FilterChain chain) {
		String requestId = LKRandomUtils.create(32);
		DateTime requestTime = DateTime.now();

		// 所有约定的请求都会访问到过滤器
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			String requestUri = LKRequestUtils.getRequestURI(req);
			String requestIp = LKIpUtils.getIp(req);
			Locale locale = LKRequestUtils.getLocale(req);

			req.setAttribute(REQUEST_ID, requestId);
			req.setAttribute(REQUEST_TIME, requestTime);
			req.setAttribute(REQUEST_URI, requestUri);
			req.setAttribute(REQUEST_IP, requestIp);

			if (logger.isDebugEnabled()) {
				logger.debug(String.format("beforeChain -> {\"requestId\":\"%s\",\"requestTime\":\"%s\",\"requestUri\":\"%s\",\"requestIp\":\"%s\",\"locale\":\"%s\"}", requestId, LKDateTimeUtils.toString(requestTime), requestUri, requestIp, locale.toString()));
			}

			request.setAttribute("locale", locale);
			request.setAttribute("errorOccurs", false);
		} else {
			// 框架没有约定的情况
			logger.error(String.format("beforeChain -> {\"requestId\":\"%s\",\"requestTime\":\"%s\"}", requestId, requestTime));
			throw new LKRuntimeException(LKErrorCodesEnum.CONFIG_ERROR);
		}
	}


	/**
	 * 调用链前处理
	 * @param request LKHttpServletRequestWrapper
	 * @param response ServletResponse
	 * @param chain FilterChain
	 */
	protected void afterChain(ServletRequest request, ServletResponse response, FilterChain chain) {
		if (logger.isDebugEnabled()) {
			String requestId = (String) request.getAttribute(REQUEST_ID);
			DateTime responseTime = DateTime.now();
			logger.debug(String.format("afterChain -> {\"requestId\":\"%s\",\"responseTime\":\"%s\",elapsedTime:%s}", requestId, LKDateTimeUtils.toString(responseTime), responseTime.compareTo((DateTime) request.getAttribute(REQUEST_TIME))));
		}
	}


	@Override
	public void destroy() {
	}


	/**
	 * 请求对象包装类
	 * @author SuZhou LichKin Information Technology Co., Ltd.
	 */
	protected class LKHttpServletRequestWrapper extends HttpServletRequestWrapper {

		public LKHttpServletRequestWrapper(HttpServletRequest request) {
			super(request);
			contentLength = request.getContentLength();
		}


		private final int contentLength;

		private ServletInputStream is;


		@Override
		public ServletInputStream getInputStream() throws IOException {
			if (contentLength >= 0) {// 如果有内容则返回处理后的输入流
				return is;
			}
			return super.getInputStream();
		}


		public String readRequestDatasFromInputStream() {
			if (contentLength >= 0) {// 如果有内容则进行输入流的处理
				try {
					ServletInputStream is = super.getInputStream();
					@Cleanup
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte buff[] = new byte[contentLength];
					int read;
					while ((read = is.read(buff)) > 0) {
						baos.write(buff, 0, read);
					}
					this.is = new LKCachingInputStream(new ByteArrayInputStream(baos.toByteArray()));
					return StreamUtils.copyToString(new ByteArrayInputStream(buff), Charset.forName("UTF-8"));
				} catch (IOException e) {
					logger.error(e);
				}
			}
			return null;
		}

	}

	/**
	 * 缓存输入流
	 * @author SuZhou LichKin Information Technology Co., Ltd.
	 */
	private class LKCachingInputStream extends ServletInputStream {

		private final ByteArrayInputStream bais;


		public LKCachingInputStream(ByteArrayInputStream bais) {
			this.bais = bais;
		}


		@Override
		public int read() throws IOException {
			return bais.read();
		}


		@Override
		public boolean isFinished() {
			return false;
		}


		@Override
		public boolean isReady() {
			return true;
		}


		@Override
		public void setReadListener(ReadListener readListener) {
		}

	}

}
