package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.enums.impl.LKDateTimeTypeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.enums.impl.LKRangeTypeEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKRandomUtils;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.framework.web.annotations.NotNeedCheckCompId;
import com.lichkin.framework.web.annotations.NotNeedCheckToken;
import com.lichkin.springframework.services.LKApiService;
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

		if (in.getAppKey().startsWith("com.lichkin.app.javascript.")) {
			// TODO 从Session中取
			// 脚本调用时token在session中处理
			in.setToken(LKDateTimeUtils.now(LKDateTimeTypeEnum.TIMESTAMP_MIN) + LKRandomUtils.create(47, LKRangeTypeEnum.NUMBER_AND_LETTER_FULL));

			// TODO 从Session中取
			// 脚本调用时compId在session中处理
			in.setCompId(LKFrameworkStatics.ROOT);
		} else {
			// 客户端调用时，如果传入了token使用传入的值。
			if (StringUtils.isBlank(in.getToken())) {
				// 没有传入值，则判断是否需要校验，需要校验则抛异常。不需要校验则取默认值。
				if (in.getClass().getAnnotation(NotNeedCheckToken.class) == null) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR);
				} else {
					in.setToken("");
				}
			}

			// 客户端调用时，如果传入了compId使用传入的值。
			if (StringUtils.isBlank(in.getCompId())) {
				// 没有传入值，则判断是否需要校验，需要校验则抛异常。不需要校验则取默认值。
				if (in.getClass().getAnnotation(NotNeedCheckCompId.class) == null) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR);
				} else {
					in.setCompId("");
				}
			}
		}

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
	 * @return 验证后的入参
	 */
	public I validateIn(I in) {
		return in;
	}

}
