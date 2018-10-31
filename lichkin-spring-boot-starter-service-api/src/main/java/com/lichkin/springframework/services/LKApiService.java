package com.lichkin.springframework.services;

import com.lichkin.framework.defines.exceptions.LKException;

/**
 * 接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface LKApiService<SI, SO> extends ApiService {

	/**
	 * 调用
	 * @param sin 服务类入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @return 服务类出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	public SO handle(SI sin, String locale, String compId, String loginId) throws LKException;

}
