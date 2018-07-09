package com.lichkin.springframework.web.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * JSP相关配置
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Configuration
public class LKJspConfigs {

	@Bean
	public InternalResourceViewResolver jspViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setOrder(2);
		resolver.setPrefix("/WEB-INF/pages");
		resolver.setSuffix(".jsp");
		resolver.setRequestContextAttribute("request");
		resolver.setContentType("text/html;charset=UTF-8");
		return resolver;
	}

}
