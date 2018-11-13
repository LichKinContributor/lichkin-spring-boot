package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_Login;
import com.lichkin.framework.defines.entities.I_User;

/**
 * 用户转换未员工服务类定义
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface UserToEmployeeService {

	/**
	 * 按照登录信息和公司令牌获取员工信息
	 * @param throwException 是否抛出异常
	 * @param userLogin 登录信息
	 * @param compToken 公司令牌
	 * @return 员工信息
	 */
	I_User findEmployeeByUserLoginAndCompToken(boolean throwException, I_Login userLogin, String compToken);

}
