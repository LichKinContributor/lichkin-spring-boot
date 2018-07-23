package com.lichkin.springframework.web.configs;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import com.lichkin.framework.defines.LKConfigStatics;
import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.json.LKJsonUtils;

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
		registry.addInterceptor(new LKHandlerInterceptor()).addPathPatterns("/**/*" + LKFrameworkStatics.WEB_MAPPING_PAGES, LKFrameworkStatics.WEB_MAPPING_API + "/**");// 添加拦截器
	}


	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		// 增加MD5资源解析器
		VersionResourceResolver versionResourceResolver = new VersionResourceResolver().addContentVersionStrategy("/**");
		int aYear = 31556926;

		if (LKConfigStatics.WEB_DEBUG) {
			aYear = 10;
		}

		registry.addResourceHandler("/res/**")// 资源请求地址
				.addResourceLocations("/res/", "classpath:/META-INF/resources/res/", "classpath*:/META-INF/resources/res/")// 资源映射路径
				.setCachePeriod(aYear).resourceChain(true).addResolver(versionResourceResolver);// 增加解析器
		registry.addResourceHandler("/webjars/**")// 资源请求地址
				.addResourceLocations("/webjars/", "classpath:/META-INF/resources/webjars/", "classpath*:/META-INF/resources/webjars/")// 资源映射路径
				.setCachePeriod(aYear).resourceChain(true).addResolver(versionResourceResolver);// 增加解析器
	}


	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 增加自定义消息转换器
		converters.add(0, new MappingJackson2HttpMessageConverter(LKJsonUtils.newObjectMapper()) {

			@Override
			public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
				// 增加对页面请求，使用了@RequestBody注解的参数进行注入解析。
				if (MediaType.TEXT_HTML.getType().equals(mediaType.getType()) && MediaType.TEXT_HTML.getSubtype().equals(mediaType.getSubtype())) {
					return true;
				}
				return super.canRead(type, contextClass, mediaType);
			}

		});
	}


	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("redirect:/index");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}


	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		handlers.add(new LKHandlerMethodReturnValueHandler4Pages());
	}

}
