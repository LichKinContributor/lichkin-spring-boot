package com.lichkin.springframework.web.configs.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

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

import org.springframework.util.StreamUtils;

import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.LKIpUtils;
import com.lichkin.springframework.web.beans.LKRequestInfo;
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
		LKHttpServletRequestWrapper req = new LKHttpServletRequestWrapper((HttpServletRequest) request);

		// 调用链前处理
		beforeChain(req, response, chain);

		// 调用链
		chain.doFilter(req, response);

		// 调用链后处理
		afterChain(req, response, chain);
	}


	/**
	 * 调用链后处理
	 * @param request LKHttpServletRequestWrapper
	 * @param response ServletResponse
	 * @param chain FilterChain
	 */
	protected void beforeChain(LKHttpServletRequestWrapper request, ServletResponse response, FilterChain chain) {
		// 所有约定的请求都会访问到过滤器
		LKRequestInfo requestInfo = new LKRequestInfo();

		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = request;
			requestInfo.setRequestUri(LKRequestUtils.getRequestURI(req));
			requestInfo.setRequestIp(LKIpUtils.getIp(req));

			String requestDatas = request.readRequestDatasFromInputStream();
			requestInfo.setRequestDatas(requestDatas);
			logger.info(LKJsonUtils.toJsonWithIncludes(requestInfo, "requestId", "requestTime", "requestUri", "requestIp", "requestDatas"));

			request.setAttribute("locale", LKRequestUtils.getLocale(request));
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
	 * @param request LKHttpServletRequestWrapper
	 * @param response ServletResponse
	 * @param chain FilterChain
	 */
	protected void afterChain(LKHttpServletRequestWrapper request, ServletResponse response, FilterChain chain) {
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
