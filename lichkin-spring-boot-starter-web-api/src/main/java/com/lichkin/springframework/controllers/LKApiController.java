package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.enums.impl.LKClientTypeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.framework.web.annotations.WithoutCompId;
import com.lichkin.framework.web.annotations.WithoutLogin;
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

		boolean jsEnv = LKClientTypeEnum.JAVASCRIPT.equals(in.getClientType());

		in.setAppKey(checkAppKey(jsEnv, in, in.getAppKey()));
		in.setToken(checkToken(jsEnv, in, in.getToken()));
		in.setCompId(checkCompId(jsEnv, in, in.getCompId()));

		return new LKResponseBean<>(handle(validateIn(in)));
	}


	/**
	 * 校验客户端唯一标识
	 * @param jsEnv 是否为脚本环境
	 * @param in 入参
	 * @param appKey 客户端唯一标识
	 * @return 客户端唯一标识
	 */
	private String checkAppKey(boolean jsEnv, I in, String appKey) {
		if (jsEnv) {
			if (StringUtils.isNotBlank(appKey)) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("appKey must blank in JAVASCRIPT enviroment."));
			}
		} else {
			if (StringUtils.isBlank(appKey)) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("appKey must not blank unless in JAVASCRIPT enviroment."));
			}
		}
		return StringUtils.trimToEmpty(appKey);
	}


	/**
	 * 校验令牌
	 * @param jsEnv 是否为脚本环境
	 * @param in 入参
	 * @param token 令牌
	 * @return 令牌
	 */
	private String checkToken(boolean jsEnv, I in, String token) {
		if (jsEnv) {
			if (StringUtils.isNotBlank(token)) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("token must blank in JAVASCRIPT enviroment."));
			}
			token = LKSession.getToken();
		} else {
			if (StringUtils.isBlank(token) && (this instanceof LKLoginApiController)) {
				if (getClass().getAnnotation(WithoutLogin.class) == null) {
					throw new LKRuntimeException(LKErrorCodesEnum.INVALIDED_TOKEN, new LKFrameworkException("not invoke with token and @WithoutLogin not annotated on controller ."));
				} else {
					LKSession.setLogin(session, null);
					LKSession.setLoginId(session, null);
				}
			}
		}
		return StringUtils.trimToEmpty(token);
	}


	/**
	 * 校验公司ID
	 * @param jsEnv 是否为脚本环境
	 * @param in 入参
	 * @param compId 公司ID
	 * @return 公司ID
	 */
	private String checkCompId(boolean jsEnv, I in, String compId) {
		if (jsEnv) {
			if (StringUtils.isNotBlank(compId)) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("compId must blank in JAVASCRIPT enviroment."));
			}
			compId = LKSession.getCompId();
		} else {
			if (StringUtils.isBlank(compId)) {
				WithoutCompId annotationWithoutCompId = getClass().getAnnotation(WithoutCompId.class);
				if (annotationWithoutCompId == null) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException(jsEnv ? "no compId in session and @WithoutCompId not annotated on controller ." : "not invoke with compId and @WithoutCompId not annotated on controller ."));
				} else {
					compId = annotationWithoutCompId.defaultValue();
				}
			} else {
				if (StringUtils.isBlank(in.getToken())) {// 非登录操作不能直接做公司业务操作
					throw new LKRuntimeException(LKErrorCodesEnum.INVALIDED_TOKEN);
				}
			}
		}

		compId = StringUtils.trimToEmpty(compId);
		LKSession.setCompId(session, compId);
		return compId;
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
