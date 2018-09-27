package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lichkin.framework.beans.impl.LKRequestIDsBean;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusDeleteService;

public abstract class LKApiBusDeleteController<I extends LKRequestIDsBean, E extends I_UsingStatus> extends LKApiVVController<I, I> {

	/**
	 * 请求处理方法
	 * @deprecated API必有方法，不可重写。
	 * @param cin 控制器类入参
	 * @return 控制器类出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	@Override
	@PostMapping
	@Deprecated
	public LKResponseBean<Void> invoke(@Valid @RequestBody I cin) throws LKException {
		initCI(cin);
		return new LKResponseBean<>(handleInvoke(cin));
	}


	@Override
	protected abstract LKApiBusDeleteService<I, E> getService(I cin);


	@Override
	protected I beforeInvokeService(I cin) throws LKException {
		return cin;
	}

}
