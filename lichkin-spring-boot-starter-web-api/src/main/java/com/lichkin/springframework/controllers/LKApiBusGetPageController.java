package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lichkin.framework.beans.impl.LKRequestPageBean;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusGetPageService;

public abstract class LKApiBusGetPageController<I extends LKRequestPageBean, O, E extends I_ID> extends LKApiController<I, Page<O>> {

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
	public LKResponseBean<Page<O>> invoke(@Valid @RequestBody I cin) throws LKException {
		initCI(cin);
		return new LKResponseBean<>(handleInvoke(cin));
	}


	@Override
	protected Page<O> handleInvoke(@Valid I cin) throws LKException {
		return getService(cin).handle(cin);
	}


	/**
	 * 获取服务类
	 * @param cin 控制器类入参
	 * @return 服务类
	 */
	protected abstract LKApiBusGetPageService<I, O, E> getService(I cin);

}
