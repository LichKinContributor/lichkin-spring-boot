package com.lichkin.springframework.web.configs.filters.impl;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.lichkin.framework.defines.LKConfigStatics;
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

		request.setAttribute("compressSuffix", LKConfigStatics.WEB_COMPRESS ? ".min" : "");
		request.setAttribute("webDebug", LKConfigStatics.WEB_DEBUG);
	}

}
