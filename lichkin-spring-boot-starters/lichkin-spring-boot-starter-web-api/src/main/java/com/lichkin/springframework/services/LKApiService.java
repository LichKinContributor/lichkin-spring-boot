package com.lichkin.springframework.services;

import com.lichkin.framework.defines.exceptions.LKException;

/**
 * 接口服务类定义
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiService<I, O> extends LKService {

	/**
	 * 调用
	 * @param in 入参
	 * @return 出参
	 */
	public abstract O handle(I in) throws LKException;

}
