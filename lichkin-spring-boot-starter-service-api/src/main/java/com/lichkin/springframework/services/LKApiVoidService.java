package com.lichkin.springframework.services;

import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 接口服务类定义
 * @param <CI> 服务类入参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface LKApiVoidService<CI> extends ApiService {

	/**
	 * 调用
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	public abstract void handle(CI cin, ApiKeyValues<CI> params) throws LKException;

}
