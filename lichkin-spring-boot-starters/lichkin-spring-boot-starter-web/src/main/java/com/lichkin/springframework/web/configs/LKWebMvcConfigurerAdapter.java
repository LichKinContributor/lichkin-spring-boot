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
import org.springframework.web.servlet.resource.VersionResourceResolver;

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
		// 增加MD5资源解析器
		VersionResourceResolver versionResourceResolver = new VersionResourceResolver().addContentVersionStrategy("/**");
		int aYear = 31556926;

		registry.addResourceHandler("/res/**")// 资源请求地址
				.addResourceLocations("/res/", "classpath:/META-INF/resources/res/", "classpath*:/META-INF/resources/res/")// 资源映射路径
				.setCachePeriod(aYear).resourceChain(true).addResolver(versionResourceResolver);// 增加解析器
		registry.addResourceHandler("/webjars/**")// 资源请求地址
				.addResourceLocations("/webjars/", "classpath:/META-INF/resources/webjars/", "classpath*:/META-INF/resources/webjars/")// 资源映射路径
				.setCachePeriod(aYear).resourceChain(true).addResolver(versionResourceResolver);// 增加解析器
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
