package com.lichkin.springframework.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lichkin.framework.defines.beans.LKRequestBean;
import com.lichkin.framework.defines.beans.LKResponseBean;
import com.lichkin.framework.utils.i18n.LKI18NUtils;
import com.lichkin.springframework.services.LKApiService;

/**
 * 接口控制器类定义
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiController<I extends LKRequestBean, O> extends LKDatasController {

	/**
	 * 请求处理方法
	 * @param in 入参
	 * @return 出参
	 */
	@PostMapping
	public LKResponseBean<O> invoke(@RequestBody I in) {
		request.setAttribute("locale", LKI18NUtils.getLocale(in.getLocale()));
		return new LKResponseBean<>(getService().handle(validateIn(in)));
	}


	/**
	 * 获取服务类
	 * @return 服务类
	 */
	protected abstract LKApiService<I, O> getService();


	/**
	 * 验证入参
	 * @param in 入参
	 */
	public I validateIn(I in) {
		return in;
	}

}
