package com.lichkin.springframework.services;

import java.util.Locale;

import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.utils.i18n.LKI18NReader4ErrorCodes;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * 用于WEB项目的服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKService4Web extends LKService {

	/**
	 * 获取当前请求的国际化对象
	 * @return 国际化对象
	 */
	protected Locale getLocale() {
		return LKRequestUtils.getLocale(LKRequestUtils.getRequest());
	}


	/**
	 * 获取国际化错误信息
	 * @param errorCode 错误编码枚举类型
	 * @return 国际化错误信息
	 */
	protected String getI18NErrorMsg(LKCodeEnum errorCode) {
		return LKI18NReader4ErrorCodes.read(getLocale(), errorCode);
	}

}
