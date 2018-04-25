package com.lichkin.springframework.web.configs.filters.impl;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.joda.time.DateTime;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.springframework.web.configs.filters.LKFilter;

/**
 * 页面请求过滤器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@WebFilter(filterName = "PagesFilter", urlPatterns = "*" + LKFrameworkStatics.WEB_MAPPING_PAGES)
public class LKFilter4Pages extends LKFilter {

	@Override
	protected void beforeChain(LKHttpServletRequestWrapper request, ServletResponse response, FilterChain chain) {
		super.beforeChain(request, response, chain);

		// 解析页面需要使用到的变量
		if (LKFrameworkStatics.WEB_DEBUG) {
			request.setAttribute("compressSuffix", "");
		} else {
			request.setAttribute("compressSuffix", "-min");
		}
		request.setAttribute("requestSuffix", DateTime.now().toString(LKFrameworkStatics.WEB_REQUEST_SUFFIX_PATTERN));
	}

}
