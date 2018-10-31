package com.lichkin.springframework.services;

import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.utils.LKBeanUtils;

/**
 * 新增接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusInsertWithoutCheckerService<SI, E extends I_ID> extends LKApiBusIUService<SI, E> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusInsertWithoutCheckerService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(SI sin, String locale, String compId, String loginId) throws LKException {
		// 业务规则校验
		if (!busCheck(sin, locale, compId, loginId)) {
			return;
		}

		// 先从入参创建新的实体对象
		E entity = LKBeanUtils.newInstance(true, sin, classE);

		// 新增时数据所需的特殊操作
		beforeAddNew(sin, locale, compId, loginId, entity);

		// 保存主表数据前操作
		beforeSaveMain(sin, locale, compId, loginId, entity);

		// 保存主表数据
		dao.persistOne(entity);

		// 保存主表数据后操作
		afterSaveMain(sin, locale, compId, loginId, entity, entity.getId());

		// 新增子表数据
		addSubs(sin, locale, compId, loginId, entity, entity.getId());
	}


	/**
	 * 业务规则校验
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @return 需要执行新增操作返回true，否则返回false。
	 */
	protected boolean busCheck(SI sin, String locale, String compId, String loginId) {
		return true;
	}


	/**
	 * 新增时数据所需的特殊操作
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 待还原实体类对象
	 */
	protected void beforeAddNew(SI sin, String locale, String compId, String loginId, E entity) {
	}

}
