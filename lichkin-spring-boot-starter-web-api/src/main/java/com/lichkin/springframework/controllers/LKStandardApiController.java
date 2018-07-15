package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.entities.I_Comp;
import com.lichkin.framework.defines.entities.I_Login;
import com.lichkin.framework.defines.enums.impl.LKClientTypeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.web.annotations.LKApiType;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.framework.web.enums.ApiType;
import com.lichkin.springframework.services.LKApiService;
import com.lichkin.springframework.services.LKCompService;
import com.lichkin.springframework.services.LKLoginService;
import com.lichkin.springframework.web.LKSession;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * API数据请求控制器类定义（开放接口）
 * @param <I> 控制器类入参类型
 * @param <O> 控制器类出参类型
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKStandardApiController<I extends LKRequestInterface, O, SI, SO> extends LKController {

	/**
	 * 获取服务类
	 * @return 服务类
	 */
	protected abstract LKApiService<SI, SO> getService();


	/** 接口类型 */
	protected ApiType apiType = ApiType.COMPANY_BUSINESS;

	/** 入参 */
	protected I in;


	/**
	 * 请求处理方法
	 * @param in 入参
	 * @return 出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	@PostMapping
	public LKResponseBean<O> invoke(@Valid @RequestBody I in) throws LKException {
		this.in = in;

		in.setLocale(LKRequestUtils.getLocale(request).toString());

		boolean jsEnv = LKClientTypeEnum.JAVASCRIPT.equals(in.getClientType());

		Class<?> clazz = getClass();
		LKApiType annotationApiType = clazz.getAnnotation(LKApiType.class);
		if (annotationApiType != null) {
			apiType = annotationApiType.apiType();
		}

		beforeCheck();

		checkAppKey(jsEnv);
		checkToken(jsEnv);
		checkCompId(jsEnv);

		switch (apiType) {
			case OPEN: {
			}
			break;
			case ROOT_QUERY: {
				in.setCompId(LKFrameworkStatics.ROOT);
			}
			break;
			case BEFORE_LOGIN: {
				String token = in.getToken();
				if (StringUtils.isNotBlank(token)) {
					checkLogin(token, in.getCompId());
				}
			}
			break;
			case PERSONAL_BUSINESS: {
				String token = in.getToken();
				if (StringUtils.isBlank(token)) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("token must not blank as API is a personal business api."));
				}
				checkLogin(token, in.getCompId());
			}
			break;
			case COMPANY_BUSINESS: {
				String token = in.getToken();
				if (StringUtils.isBlank(token)) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("token must not blank as API is a company business api."));
				}
				String compId = in.getCompId();
				if (StringUtils.isBlank(compId)) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("compId must not blank as API is a company business api."));
				}
				checkCompany(compId);
				checkLogin(token, in.getCompId());
			}
			break;
		}

		afterCheck();

		return new LKResponseBean<>(sout2out(getService().handle(in2sin(in))));
	}


	/**
	 * 校验客户端唯一标识
	 * @param jsEnv 是否为脚本环境
	 * @return 客户端唯一标识
	 */
	private void checkAppKey(boolean jsEnv) {
		String appKey = in.getAppKey();
		boolean blank = StringUtils.isBlank(appKey);

		// 脚本环境不能传入appKey
		if (jsEnv && !blank) {
			throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("appKey must blank in JAVASCRIPT enviroment."));
		}

		// 非脚本环境必须传入appKey
		if (!jsEnv && blank) {
			throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("appKey must not blank unless in JAVASCRIPT enviroment."));
		}
	}


	/**
	 * 校验令牌
	 * @param jsEnv 是否为脚本环境
	 * @return 令牌
	 */
	private void checkToken(boolean jsEnv) {
		String token = in.getToken();
		boolean blank = StringUtils.isBlank(token);

		if (jsEnv) {
			if (blank) {
				// 从session中取token设置值
				in.setToken(LKSession.getToken(session));
			} else {
				// 脚本环境不能传入token
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("token must blank in JAVASCRIPT enviroment."));
			}
		}
	}


	/**
	 * 校验公司ID
	 * @param jsEnv 是否为脚本环境
	 * @return 公司ID
	 */
	private void checkCompId(boolean jsEnv) {
		String compId = in.getCompId();
		boolean blank = StringUtils.isBlank(compId);

		if (jsEnv) {
			if (blank) {
				// 从session中取compId设置值
				in.setCompId(LKSession.getCompId(session));
			} else {
				// 脚本环境不能传入compId
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("compId must blank in JAVASCRIPT enviroment."));
			}
		}
	}


	/**
	 * 校验登录信息
	 * @param token 令牌
	 * @param compId 公司ID
	 */
	private void checkLogin(String token, String compId) {
		try {
			I_Login login = ((LKLoginService) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(LKRequestUtils.getApiUserService(request))).findUserLoginByToken(token, compId);
			if (login == null) {
				throw new LKRuntimeException(LKErrorCodesEnum.INVALIDED_TOKEN);
			}
			in.setLogin(login);
			in.setLoginId(login.getId());
		} catch (ClassNotFoundException e) {
			throw new LKFrameworkException(e);
		}
	}


	/**
	 * 校验公司信息
	 * @param compId 公司ID
	 */
	private void checkCompany(String compId) {
		try {
			I_Comp comp = ((LKCompService) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(Class.forName("com.lichkin.application.services.impl.SysCompService"))).findCompById(compId);
			if (comp == null) {
				throw new LKRuntimeException(LKErrorCodesEnum.INVALIDED_COMP_ID);
			}
			in.setComp(comp);
			in.setCompId(comp.getId());
		} catch (ClassNotFoundException e) {
			throw new LKFrameworkException(e);
		}
	}


	/**
	 * 校验前操作
	 */
	protected void beforeCheck() {
	}


	/**
	 * 校验后操作
	 */
	protected void afterCheck() {
	}


	/**
	 * 调用service方法前将控制器类入参转换为服务类入参
	 * @param in 控制器类入参
	 * @return 服务类入参
	 */
	protected abstract SI in2sin(I in);


	/**
	 * 调用service方法后将服务类出参转换为控制器类出参
	 * @param out 服务类出参
	 * @return 控制器类出参
	 */
	protected abstract O sout2out(SO out);

}
