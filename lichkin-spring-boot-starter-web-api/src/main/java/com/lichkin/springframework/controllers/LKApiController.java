package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * API数据请求控制器类定义
 * @param <CI> 入参类型
 * @param <CO> 出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiController<CI extends LKRequestInterface, CO> extends LKSameInApiController<CI, CO, CO> {

	@Override
	protected CO sout2cout(CO sout) throws LKException {
		return sout;
	}

}
