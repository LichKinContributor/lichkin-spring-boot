package com.lichkin.springframework.web.configs;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * 视图解析器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKFreeMarkerViewResolver extends FreeMarkerViewResolver {

	@Autowired
	private InternalResourceViewResolver resolver;


	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		View view = super.resolveViewName(viewName, locale);
		if (view == null) {
			return resolver.resolveViewName(viewName, locale);
		}
		return view;
	}

}
