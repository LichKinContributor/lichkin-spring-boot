package com.lichkin.springframework.web.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import com.lichkin.framework.defines.LKConfigStatics;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;

/**
 * 资源地址解析供应者
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Component
public class LKResourceUrlProvider {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKResourceUrlProvider.class);

	@Autowired
	private ResourceUrlProvider provider;


	public String getForLookupPath(String lookupPath) {
		String url = provider.getForLookupPath(lookupPath);
		if (LKConfigStatics.WEB_DEBUG) {
			if (url.endsWith(".js")) {
				url = url.substring(0, url.lastIndexOf("-")) + ".js";
			} else if (url.endsWith(".css")) {
				url = url.substring(0, url.lastIndexOf("-")) + ".css";
			}
		}
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace(url);
		}
		return url;
	}

}
