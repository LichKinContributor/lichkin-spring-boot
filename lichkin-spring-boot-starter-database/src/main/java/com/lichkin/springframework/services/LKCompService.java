package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_Comp;

/**
 * 公司业务服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface LKCompService {

	/**
	 * 按照公司ID获取公司信息
	 * @param compId 公司ID
	 * @return 公司信息
	 */
	public I_Comp findCompById(String compId);

}
