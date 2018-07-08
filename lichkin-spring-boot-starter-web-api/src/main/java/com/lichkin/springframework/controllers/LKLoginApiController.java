package com.lichkin.springframework.controllers;

import org.apache.commons.lang3.StringUtils;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.defines.entities.I_Login;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.springframework.services.LKLoginService;
import com.lichkin.springframework.web.LKSession;

/**
 * 登录API数据请求控制器类定义
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKLoginApiController<I extends LKRequestInterface, O> extends LKApiController<I, O> {

	@Override
	protected O handle(I in) throws LKException {
		I_Login login = null;
		String loginId = null;

		String token = in.getToken();
		if (StringUtils.isNotBlank(token)) {
			if (token.equals(LKSession.getToken(session))) {
				login = LKSession.getLogin(session);// 先从会话中取登录信息
				if (login == null) {// 会话中没有登录信息
					login = getLoginBusService().findUserLoginByToken(token);// 通过令牌获取登录信息
					if (login == null) {
						throw new LKRuntimeException(LKErrorCodesEnum.INVALIDED_TOKEN);
					}
					loginId = login.getId();
					// 将登录信息传入会话
					LKSession.setLogin(session, login);
					LKSession.setLoginId(session, loginId);
				}
			} else {
				login = getLoginBusService().findUserLoginByToken(token);// 通过令牌获取登录信息
				if (login == null) {
					throw new LKRuntimeException(LKErrorCodesEnum.INVALIDED_TOKEN);
				}
				loginId = login.getId();
				// 将登录信息传入会话
				LKSession.setLogin(session, login);
				LKSession.setLoginId(session, loginId);
			}
		}

		return super.handle(in);
	}


	/**
	 * 获取登录业务服务类
	 * @return 登录业务服务类
	 */
	protected abstract LKLoginService getLoginBusService();

}
