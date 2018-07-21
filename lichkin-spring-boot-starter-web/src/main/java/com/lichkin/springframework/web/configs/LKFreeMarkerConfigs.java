package com.lichkin.springframework.web.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * FreeMarker相关配置
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Configuration
public class LKFreeMarkerConfigs {

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setTemplateLoaderPath("/WEB-INF/templates");
		configurer.setPreferFileSystemAccess(false);
		configurer.setDefaultEncoding("UTF-8");
		return configurer;
	}


	@Bean
	public FreeMarkerViewResolver freeMarkerViewResolver() {
		FreeMarkerViewResolver resolver = new LKFreeMarkerViewResolver();
		resolver.setOrder(1);
		resolver.setSuffix(".ftl");
		resolver.setRequestContextAttribute("request");
		resolver.setContentType("text/html;charset=UTF-8");
		return resolver;
	}

}
