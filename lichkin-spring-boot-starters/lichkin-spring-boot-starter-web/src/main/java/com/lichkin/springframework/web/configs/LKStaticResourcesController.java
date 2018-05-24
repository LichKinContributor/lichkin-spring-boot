package com.lichkin.springframework.web.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import com.lichkin.framework.web.annotations.LKController4Pages;

/**
 * 静态资源文件请求处理
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@ControllerAdvice(annotations = LKController4Pages.class)
public class LKStaticResourcesController {

	@Autowired
	ResourceUrlProvider provider;


	/**
	 * 静态资源路径解析提供者
	 * @return 提供者
	 */
	@ModelAttribute("provider")
	public ResourceUrlProvider provider() {
		return provider;
	}

}
