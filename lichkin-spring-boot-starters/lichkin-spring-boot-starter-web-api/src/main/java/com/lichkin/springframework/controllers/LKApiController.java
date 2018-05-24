package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lichkin.framework.beans.LKRequestBean;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.springframework.services.LKApiService;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * API数据请求控制器类定义
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKApiController<I extends LKRequestBean, O> extends LKController {

	/**
	 * 请求处理方法
	 * @param in 入参
	 * @return 出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	@PostMapping
	public Object invoke(@Valid @RequestBody I in) throws LKException {
		in.setLocale(LKRequestUtils.getLocale(request).toString());
		return getService().handle(validateIn(in));
	}


	/**
	 * 获取服务类
	 * @return 服务类
	 */
	protected abstract LKApiService<I, O> getService();


	/**
	 * 验证入参
	 * @param in 入参
	 * @return 验证后的入参
	 */
	public I validateIn(I in) {
		return in;
	}

}
