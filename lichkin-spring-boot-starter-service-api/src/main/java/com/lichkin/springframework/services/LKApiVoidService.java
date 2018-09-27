package com.lichkin.springframework.services;

import com.lichkin.framework.defines.exceptions.LKException;

/**
 * 接口服务类定义
 * @param <SI> 服务类入参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface LKApiVoidService<SI> extends ApiService {

	/**
	 * 调用
	 * @param sin 服务类入参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	public abstract void handle(SI sin) throws LKException;

}
