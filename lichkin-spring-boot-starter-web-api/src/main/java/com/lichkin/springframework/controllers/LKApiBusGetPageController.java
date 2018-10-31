package com.lichkin.springframework.controllers;

import java.lang.reflect.ParameterizedType;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import com.lichkin.framework.beans.impl.LKRequestPageBean;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusGetPageService;

public abstract class LKApiBusGetPageController<CI extends LKRequestPageBean, O, E extends I_ID> extends LKApiController<CI, Page<O>> {

	protected abstract LKApiBusGetPageService<CI, O, E> getService(CI cin);


	@Deprecated
	@Override
	protected LKOperTypeEnum getOperType(@Valid CI cin) {
		return LKOperTypeEnum.SEARCH;
	}


	Class<E> classE;


	@SuppressWarnings("unchecked")
	public LKApiBusGetPageController() {
		super();
		classE = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[2];
	}


	@Deprecated
	@Override
	protected String getBusType(CI cin) {
		String subOperBusType = StringUtils.trimToEmpty(getSubOperBusType(cin));
		return "PAGE_" + classE.getSimpleName().replace("Entity", "") + (subOperBusType.isEmpty() ? "" : "_" + subOperBusType);
	}


	protected String getSubOperBusType(CI cin) {
		return null;
	}


	@Deprecated
	@Override
	Page<O> handleInvoke(@Valid CI cin, String locale, String compId, String loginId) throws LKException {
		return getService(cin).handle(cin, locale, compId, loginId);
	}

}
