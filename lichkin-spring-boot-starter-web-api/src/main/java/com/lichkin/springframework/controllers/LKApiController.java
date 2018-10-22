package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.entities.I_Comp;
import com.lichkin.framework.defines.entities.I_Login;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.framework.web.annotations.LKApiType;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.springframework.web.LKSession;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @param <CO> 控制器类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKApiController<CI extends LKRequestBean, CO> extends LKController {

	/**
	 * 请求处理方法
	 * @deprecated API必有方法，不可重写。
	 * @param cin 控制器类入参
	 * @return 控制器类出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	@PostMapping
	@Deprecated
	public LKResponseBean<CO> invoke(@Valid @RequestBody CI cin) throws LKException {
		initCI(cin);
		return new LKResponseBean<>(handleInvoke(cin));
	}


	/**
	 * 初始化控制器类入参
	 * @param cin 控制器类入参
	 */
	protected void initCI(CI cin) {
		cin.setLocale(LKRequestUtils.getLocale(request).toString());

		String requestURI = LKRequestUtils.getRequestURI(request);
		if (requestURI.startsWith(LKFrameworkStatics.WEB_MAPPING_API_APP)) {
			initAsApp(cin);
		} else {
			initAsWeb(cin);
		}
	}


	/**
	 * 客户端访问接口
	 * @param cin 控制器类入参
	 */
	private void initAsApp(CI cin) {
	}


	/**
	 * 页面访问接口
	 * @param cin 控制器类入参
	 */
	private void initAsWeb(CI cin) {
		switch (((LKApiType) LKClassUtils.deepGetAnnotation(getClass(), LKApiType.class.getName())).apiType()) {
			case OPEN: {
			}
			break;
			case ROOT_QUERY: {
				cin.setCompId(LKFrameworkStatics.LichKin);
			}
			break;
			case BEFORE_LOGIN: {
				cin.setToken(LKSession.getToken(session));
				cin.setLogin(LKSession.getLogin(session));
				cin.setLoginId(LKSession.getLoginId(session));
			}
			break;
			case PERSONAL_BUSINESS: {
				String token = LKSession.getToken(session);
				I_Login login = LKSession.getLogin(session);
				String loginId = LKSession.getLoginId(session);
				if (StringUtils.isBlank(token) || StringUtils.isBlank(loginId) || (login == null)) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("You must login before when invoke a personal business API."));
				}
				cin.setToken(token);
				cin.setLogin(login);
				cin.setLoginId(loginId);
			}
			break;
			case COMPANY_BUSINESS: {
				String token = LKSession.getToken(session);
				I_Login login = LKSession.getLogin(session);
				String loginId = LKSession.getLoginId(session);
				if (StringUtils.isBlank(token) || StringUtils.isBlank(loginId) || (login == null)) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("You must login before when invoke a company business API."));
				}
				I_Comp comp = LKSession.getComp(session);
				String compId = LKSession.getCompId(session);
				if (StringUtils.isBlank(compId) || (comp == null)) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("You must belongs to a company when invoke a company business API."));
				}
				cin.setToken(token);
				cin.setLogin(login);
				cin.setLoginId(loginId);
				cin.setComp(comp);
				cin.setCompId(compId);
			}
			break;
		}
	}


	/**
	 * 请求处理方法
	 * @deprecated API框架内部实现，不可重写。
	 * @param cin 控制器类入参
	 * @return 控制器类出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	@Deprecated
	protected abstract CO handleInvoke(@Valid CI cin) throws LKException;

}
