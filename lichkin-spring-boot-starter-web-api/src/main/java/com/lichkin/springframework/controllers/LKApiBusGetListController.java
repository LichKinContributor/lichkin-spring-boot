package com.lichkin.springframework.controllers;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusGetListService;

public abstract class LKApiBusGetListController<CI extends LKRequestBean, O, E extends I_ID> extends LKApiController<CI, List<O>> {

	protected abstract LKApiBusGetListService<CI, O, E> getService(CI cin);


	@Deprecated
	@Override
	protected LKOperTypeEnum getOperType(@Valid CI cin) {
		return LKOperTypeEnum.SEARCH;
	}


	/** 实体类类型 */
	Class<E> classE;


	@SuppressWarnings("unchecked")
	public LKApiBusGetListController() {
		super();
		classE = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[2];
	}


	@Deprecated
	@Override
	protected String getBusType(CI cin) {
		String subOperBusType = StringUtils.trimToEmpty(getSubOperBusType(cin));
		return "LIST_" + classE.getSimpleName().replace("Entity", "") + (subOperBusType.isEmpty() ? "" : "_" + subOperBusType);
	}


	protected String getSubOperBusType(CI cin) {
		return null;
	}


	@Deprecated
	@Override
	List<O> handleInvoke(@Valid CI cin, String locale, String compId, String loginId) throws LKException {
		return getService(cin).handle(cin, locale, compId, loginId);
	}

}
