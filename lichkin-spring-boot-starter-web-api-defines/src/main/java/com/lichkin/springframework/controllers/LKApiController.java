package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @param <CO> 控制器类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiController<CI extends LKRequestBean, CO> extends LKController {

	/**
	 * 请求处理方法
	 * @deprecated API必有方法，不可重写。
	 * @param cin 控制器类入参
	 * @return 控制器类出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	@Deprecated
	@PostMapping
	public LKResponseBean<CO> invoke(@Valid @RequestBody CI cin) throws LKException {
		return null;
	}


	/**
	 * 请求处理方法
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @return 控制器类出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	abstract CO handleInvoke(CI cin, ApiKeyValues<CI> params) throws LKException;


	/**
	 * 是否记录日志
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @return true:记录日志;false:不记录日志;
	 */
	protected boolean saveLog(CI cin, ApiKeyValues<CI> params) {
		return true;
	}


	/**
	 * 获取操作类型
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @return 操作类型
	 */
	protected LKOperTypeEnum getOperType(CI cin, ApiKeyValues<CI> params) {
		return LKOperTypeEnum.OTHER;
	}

}
