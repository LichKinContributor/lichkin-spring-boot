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
import com.lichkin.framework.utils.LKStringUtils;
import com.lichkin.framework.web.annotations.LKApiType;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.framework.web.enums.ApiType;
import com.lichkin.springframework.services.LKCompService;
import com.lichkin.springframework.services.LKLoginService;
import com.lichkin.springframework.web.LKSession;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * API数据请求控制器类定义
 * @param <I> 控制器类入参类型
 * @param <O> 控制器类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKWithoutServiceApiController<I extends LKRequestInterface, O> extends LKController {

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

		String appKey = in.getAppKey();
		String token = in.getToken();
		String compId = in.getCompId();
		LKClientTypeEnum clientType = in.getClientType();
		boolean jsEnv = LKClientTypeEnum.JAVASCRIPT.equals(clientType);
		Class<?> clazz = getClass();
		LKApiType annotationApiType = clazz.getAnnotation(LKApiType.class);
		if (annotationApiType != null) {
			apiType = annotationApiType.apiType();
		}
		String requestURI = LKRequestUtils.getRequestURI(request);
		if (jsEnv) {
			// 脚本环境只能调用/API/Web接口
			if (!requestURI.startsWith(LKFrameworkStatics.WEB_MAPPING_API_WEB)) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException(clientType.toString() + " enviroment can only invoke /API/Web/xxx."));
			}

			// 脚本环境不能传入appKey
			if (StringUtils.isNotBlank(appKey)) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException(clientType.toString() + " enviroment appKey must blank."));
			}

			// 脚本环境不能传入token
			if (StringUtils.isNotBlank(token)) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException(clientType.toString() + " enviroment token must blank."));
			} else {
				in.setToken(LKSession.getToken(session));
				in.setLogin(LKSession.getLogin(session));
				in.setLoginId(LKSession.getLoginId(session));
			}

			// 脚本环境不能传入compId
			if (StringUtils.isNotBlank(compId)) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException(clientType.toString() + " enviroment compId must blank."));
			} else {
				in.setComp(LKSession.getComp(session));
				in.setCompId(LKSession.getCompId(session));
			}

			switch (apiType) {
				case OPEN: {
				}
				break;
				case ROOT_QUERY: {
					in.setCompId(LKFrameworkStatics.ROOT);
				}
				break;
				case BEFORE_LOGIN: {
				}
				break;
				case PERSONAL_BUSINESS: {
					if (StringUtils.isBlank(in.getToken()) || StringUtils.isBlank(in.getLoginId()) || (in.getLogin() == null)) {
						throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("You must login before when invoke a personal business API."));
					}
				}
				break;
				case COMPANY_BUSINESS: {
					if (StringUtils.isBlank(in.getToken()) || StringUtils.isBlank(in.getLoginId()) || (in.getLogin() == null)) {
						throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("You must login before when invoke a company business API."));
					}
					if (StringUtils.isBlank(in.getCompId()) || (in.getComp() == null)) {
						throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("You must belongs to a company when invoke a company business API."));
					}
				}
				break;
			}
		} else {
			// 非脚本环境只能调用/API/App接口
			if (!requestURI.startsWith(LKFrameworkStatics.WEB_MAPPING_API_APP)) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException(clientType.toString() + " enviroment can only invoke /API/App/xxx."));
			}

			// 非脚本环境必须传入appKey
			if (StringUtils.isBlank(appKey)) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException(clientType.toString() + " enviroment appKey must not blank."));
			}

			switch (apiType) {
				case OPEN: {
				}
				break;
				case ROOT_QUERY: {
					in.setCompId(LKFrameworkStatics.ROOT);
				}
				break;
				case BEFORE_LOGIN: {
					if (StringUtils.isNotBlank(token)) {
						checkLogin(requestURI, token);
					}
				}
				break;
				case PERSONAL_BUSINESS: {
					if (StringUtils.isBlank(token)) {
						throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("token must not blank as API is a personal business api."));
					}
					checkLogin(requestURI, token);
				}
				break;
				case COMPANY_BUSINESS: {
					if (StringUtils.isBlank(token)) {
						throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("token must not blank as API is a company business api."));
					}
					if (StringUtils.isBlank(compId)) {
						throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("compId must not blank as API is a company business api."));
					}
					checkLogin(requestURI, token);
					checkCompany(compId);
				}
				break;
			}
		}

		beforeReturn();

		return new LKResponseBean<>(returnOut());
	}


	/**
	 * 返回前处理方法
	 * @deprecated 框架内部实现
	 */
	@Deprecated
	protected void beforeReturn() throws LKException {
	}


	/**
	 * 返回出参对象
	 * @return 出参对象
	 */
	protected O returnOut() throws LKException {
		return null;
	}


	/**
	 * 校验登录信息
	 * @param requestURI 请求地址
	 * @param token 令牌
	 */
	private void checkLogin(String requestURI, String token) {
		LKLoginService loginService = null;
		try {
			Class<?> serviceClass = Class.forName(String.format("com.lichkin.application.services.impl.Sys%sLoginService", requestURI.substring(LKStringUtils.indexOf(requestURI, "/", 2) + 1, LKStringUtils.indexOf(requestURI, "/", 3))));
			loginService = (LKLoginService) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(serviceClass);
		} catch (Exception e) {
			throw new LKFrameworkException(e);
		}

		if (loginService == null) {
			throw new LKFrameworkException("loginService can not be null.");
		}

		I_Login login = loginService.findUserLoginByToken(token);
		if (login == null) {
			throw new LKRuntimeException(LKErrorCodesEnum.INVALIDED_TOKEN);
		}

		in.setLogin(login);
		in.setLoginId(login.getId());
	}


	/**
	 * 校验公司信息
	 * @param compId 公司ID
	 */
	private void checkCompany(String compId) {
		LKCompService compService = null;
		try {
			Class<?> compServiceClass = Class.forName("com.lichkin.application.services.impl.SysCompService");
			compService = (LKCompService) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(compServiceClass);
		} catch (ClassNotFoundException e) {
			throw new LKFrameworkException(e);
		}

		if (compService == null) {
			throw new LKFrameworkException("compService can not be null.");
		}

		I_Comp comp = compService.findCompById(compId);
		if (comp == null) {
			throw new LKRuntimeException(LKErrorCodesEnum.INVALIDED_COMP_ID);
		}

		in.setComp(comp);
		in.setCompId(comp.getId());
	}

}
