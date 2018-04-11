package com.lichkin.springframework.web.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置适配器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Configuration
@EnableWebMvc
public class LKWebMvcConfigurerAdapter implements WebMvcConfigurer {

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(new LKHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/res/**", "/webjars/**");// 添加拦截器
	}

}
