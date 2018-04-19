package com.lichkin.springframework.web.configs;

import java.util.List;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lichkin.framework.defines.LKFrameworkStatics;

/**
 * WebMvc配置适配器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Configuration
@EnableWebMvc
@ServletComponentScan
public class LKWebMvcConfigurerAdapter implements WebMvcConfigurer {

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(new LKHandlerInterceptor()).addPathPatterns("/**/*" + LKFrameworkStatics.WEB_MAPPING_PAGES, "/**/*" + LKFrameworkStatics.WEB_MAPPING_DATAS, LKFrameworkStatics.WEB_MAPPING_API + "**");// 添加拦截器
	}


	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/res/**").addResourceLocations("/res/", "classpath:/META-INF/resources/res/", "classpath*:/META-INF/resources/res/");// 添加资源
		registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/", "classpath:/META-INF/resources/webjars/", "classpath*:/META-INF/resources/webjars/");// 添加资源
	}


	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 改变默认都消息转换器顺序
		HttpMessageConverter<?> mappingJackson2XmlHttpMessageConverter = null;
		for (int i = converters.size() - 1; i >= 0; i--) {
			final HttpMessageConverter<?> converter = converters.get(i);
			if (converter.getClass() == MappingJackson2XmlHttpMessageConverter.class) {
				mappingJackson2XmlHttpMessageConverter = converters.remove(i);
			}
		}
		if (mappingJackson2XmlHttpMessageConverter != null) {
			converters.add(mappingJackson2XmlHttpMessageConverter);
		}
	}

}
