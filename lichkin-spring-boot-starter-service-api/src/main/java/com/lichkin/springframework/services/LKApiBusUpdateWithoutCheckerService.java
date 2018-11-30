package com.lichkin.springframework.services;

import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKBeanUtils;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 编辑接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusUpdateWithoutCheckerService<CI extends I_ID, E extends I_ID> extends LKApiBusIUService<CI, E> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusUpdateWithoutCheckerService() {
		super();
		classCI = (Class<CI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(CI cin, ApiKeyValues<CI> params) throws LKException {
		// 通过ID找到该条数据
		E entity = dao.findOneById(classE, params.getId());
		if (entity == null) {
			// 无数据则抛异常
			throw new LKRuntimeException(LKErrorCodesEnum.INEXIST);
		}

		// 业务规则校验
		if (!busCheck(cin, params, entity, entity.getId())) {
			return;
		}

		// 复制属性前操作
		beforeCopyProperties(cin, params, entity);

		// 修改数据
		LKBeanUtils.copyProperties(false, cin, entity, "id", "insertTime", "locale", "compId", "loginId", "usingStatus");

		// 保存主表数据前操作
		beforeSaveMain(cin, params, entity);

		// 保存主表数据
		dao.mergeOne(entity);

		// 保存主表数据后操作
		afterSaveMain(cin, params, entity, entity.getId());

		// 还原数据，需先清空子表数据
		clearSubs(cin, params, entity, entity.getId());

		// 新增子表数据
		addSubs(cin, params, entity, entity.getId());
	}


	/**
	 * 复制属性前操作
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 保存前的实体类对象
	 */
	protected void beforeCopyProperties(CI cin, ApiKeyValues<CI> params, E entity) {
	}


	/**
	 * 业务规则校验
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 保存前的实体类对象
	 * @param id 主键
	 * @return 需要执行更新操作返回true，否则返回false。
	 */
	protected boolean busCheck(CI cin, ApiKeyValues<CI> params, E entity, String id) {
		return true;
	}

}
