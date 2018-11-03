package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_Login;
import com.lichkin.framework.defines.entities.I_User;

/**
 * 登录业务服务类
 * @param <E> 登录表实体类类型
 * @param <U> 用户表实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LoginService<E extends I_Login, U extends I_User> extends LKDBService {

	/**
	 * 按照令牌获取登录信息
	 * @param throwException 是否抛出异常
	 * @param token 令牌
	 * @return 登录信息
	 */
	public abstract E findUserLoginByToken(boolean throwException, String token);

}
