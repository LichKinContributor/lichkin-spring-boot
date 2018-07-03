package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.framework.web.annotations.NotNeedCheckCompId;
import com.lichkin.framework.web.annotations.NotNeedCheckToken;
import com.lichkin.springframework.services.LKApiService;
import com.lichkin.springframework.web.LKSession;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * API数据请求控制器类定义
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKApiController<I extends LKRequestInterface, O> extends LKController {

	/**
	 * 请求处理方法
	 * @param in 入参
	 * @return 出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	@PostMapping
	public LKResponseBean<O> invoke(@Valid @RequestBody I in) throws LKException {
		in.setLocale(LKRequestUtils.getLocale(request).toString());

		boolean jsEnv = in.getAppKey().startsWith("com.lichkin.app.javascript");
		Class<? extends LKRequestInterface> cls = in.getClass();

		String token = jsEnv ? LKSession.getToken(session) : in.getToken();
		if (StringUtils.isBlank(token) && (cls.getAnnotation(NotNeedCheckToken.class) == null)) {
			throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR);
		}
		in.setToken(StringUtils.trimToEmpty(token));

		String compId = jsEnv ? LKSession.getCompId(session) : in.getCompId();
		NotNeedCheckCompId annotationNotNeedCheckCompId = cls.getAnnotation(NotNeedCheckCompId.class);
		if (StringUtils.isBlank(compId)) {
			if (annotationNotNeedCheckCompId == null) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR);
			} else {
				compId = annotationNotNeedCheckCompId.defaultValue();
			}
		}
		in.setCompId(StringUtils.trimToEmpty(compId));

		return new LKResponseBean<>(handle(validateIn(in)));
	}


	/**
	 * 获取服务类
	 * @return 服务类
	 */
	protected abstract LKApiService<I, O> getService();


	/**
	 * 调用
	 * @param in 入参
	 * @return 出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	protected O handle(I in) throws LKException {
		return getService().handle(in);
	}


	/**
	 * 验证入参
	 * @param in 入参
	 * @return 验证后的入参
	 */
	protected I validateIn(I in) {
		return in;
	}

}
