package com.lichkin.springframework.controllers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lichkin.framework.beans.impl.LKRequestIDsUsingStatusBean;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusUpdateUsingStatusService;
import com.lichkin.springframework.services.OperLogService;
import com.lichkin.springframework.web.beans.LKRequestInfo;

public abstract class LKApiBusUpdateUsingStatusController<I extends LKRequestIDsUsingStatusBean, E extends I_UsingStatus> extends LKApiVVController<I, I> implements OperLogController<I> {

	/** 泛型类型 */
	Type[] types;

	/** 入参类型 */
	Class<I> classI;

	/** 实体类类型 */
	Class<E> classE;


	@SuppressWarnings("unchecked")
	public LKApiBusUpdateUsingStatusController() {
		super();
		types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		classI = (Class<I>) types[0];
		classE = (Class<E>) types[1];
	}


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
		LKResponseBean<Void> responseBean = new LKResponseBean<>(handleInvoke(cin));
		OperLogService logService = getLogService();
		if (logService != null) {
			if (cin.getUsingStatus().equals(LKUsingStatusEnum.DEPRECATED)) {
				logService.logRemove(cin, (LKRequestInfo) request.getAttribute("requestInfo"), cin.getUsingStatus().toString() + "_" + classE.getSimpleName().replace("Entity", "") + getSubOperBusType(cin));
			} else {
				logService.logEdit(cin, (LKRequestInfo) request.getAttribute("requestInfo"), cin.getUsingStatus().toString() + "_" + classE.getSimpleName().replace("Entity", "") + getSubOperBusType(cin));
			}
		}
		return responseBean;
	}


	@Override
	protected abstract LKApiBusUpdateUsingStatusService<I, E> getService(I cin);


	@Override
	protected I beforeInvokeService(I cin) throws LKException {
		return cin;
	}


	protected String getSubOperBusType(I cin) {
		return "";
	}

}
