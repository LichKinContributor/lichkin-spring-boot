package com.lichkin.springframework.services;

import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKBeanUtils;

/**
 * 编辑接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusUpdateWithoutCheckerService<SI extends I_ID, E extends I_ID> extends LKApiBusIUService<SI, E> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusUpdateWithoutCheckerService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(SI sin, String locale, String compId, String loginId) throws LKException {
		// 通过ID找到该条数据
		E entity = dao.findOneById(classE, sin.getId());
		if (entity == null) {
			// 无数据则抛异常
			throw new LKRuntimeException(LKErrorCodesEnum.INEXIST);
		}

		// 业务规则校验
		if (!busCheck(sin, locale, compId, loginId, entity, entity.getId())) {
			return;
		}

		// 修改数据
		LKBeanUtils.copyProperties(false, sin, entity, "id", "insertTime", "locale", "compId", "loginId");

		// 保存主表数据前操作
		beforeSaveMain(sin, locale, compId, loginId, entity);

		// 保存主表数据
		dao.mergeOne(entity);

		// 保存主表数据后操作
		afterSaveMain(sin, locale, compId, loginId, entity, entity.getId());

		// 还原数据，需先清空子表数据
		clearSubs(sin, locale, compId, loginId, entity, entity.getId());

		// 新增子表数据
		addSubs(sin, locale, compId, loginId, entity, entity.getId());
	}


	/**
	 * 业务规则校验
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 保存前的实体类对象
	 * @param id 主键
	 * @return 需要执行更新操作返回true，否则返回false。
	 */
	protected boolean busCheck(SI sin, String locale, String compId, String loginId, E entity, String id) {
		return true;
	}

}
