package com.lichkin.springframework.services;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * 接口服务类定义
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiService<I extends LKRequestInterface, O> extends LKDBService {

	/**
	 * 调用
	 * @param in 入参
	 * @return 出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	public abstract O handle(I in) throws LKException;

}
