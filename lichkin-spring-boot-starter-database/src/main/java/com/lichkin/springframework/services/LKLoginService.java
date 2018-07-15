package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_Login;

/**
 * 登录业务服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface LKLoginService {

	/**
	 * 按照令牌获取登录信息
	 * @param token 令牌
	 * @param compId 公司ID
	 * @return 登录信息
	 */
	public I_Login findUserLoginByToken(String token, String compId);

}
