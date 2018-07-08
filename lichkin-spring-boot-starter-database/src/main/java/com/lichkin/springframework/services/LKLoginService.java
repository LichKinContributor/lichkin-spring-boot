package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_Login;

/**
 * 登录业务服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKLoginService extends LKDBService {

	/**
	 * 按照令牌获取登录信息
	 * @param token 令牌
	 * @return 登录信息
	 */
	public abstract I_Login findUserLoginByToken(String token);


	/**
	 * 按照登录名获取登录信息
	 * @param loginName 登录名/手机号码/身份证号/邮箱
	 * @return 登录信息
	 */
	public abstract I_Login findUserLoginByLoginName(String loginName);


	/**
	 * 按照手机号码获取登录信息
	 * @param cellphone 手机号码
	 * @return 登录信息
	 */
	public abstract I_Login findUserLoginByCellphone(String cellphone);

}
