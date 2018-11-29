package com.lichkin.springframework.services;

import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.defines.beans.LKInvokeBean;
import com.lichkin.framework.defines.beans.LKInvokeDatas;
import com.lichkin.framework.defines.entities.I_CompId;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.entities.I_Locale;
import com.lichkin.framework.defines.entities.I_LoginId;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.utils.LKBeanUtils;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 新增接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusInsertWithoutCheckerService<CI extends LKInvokeBean<? extends LKInvokeDatas>, E extends I_ID> extends LKApiBusIUService<CI, E> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusInsertWithoutCheckerService() {
		super();
		classCI = (Class<CI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(CI cin, ApiKeyValues<CI> params) throws LKException {
		// 业务规则校验
		if (!busCheck(cin, params)) {
			return;
		}

		// 先从入参创建新的实体对象
		E entity = newInstance(params, null);

		// 新增时数据所需的特殊操作
		beforeAddNew(cin, params, entity);

		// 保存主表数据前操作
		beforeSaveMain(cin, params, entity);

		// 保存主表数据
		dao.persistOne(entity);

		// 保存主表数据后操作
		afterSaveMain(cin, params, entity, entity.getId());

		// 新增子表数据
		addSubs(cin, params, entity, entity.getId());

		// 新增子表后操作
		afterSubs(cin, params, entity, entity.getId());
	}


	/**
	 * 从入参创建新的实体类
	 * @param params 解析值参数
	 * @param exist 待还原实体类对象
	 * @return 实体类
	 */
	E newInstance(ApiKeyValues<CI> params, E exist) {
		CI originalObject = params.getOriginalObject();
		// 先从统一参数创建新实体类
		E entity = LKBeanUtils.newInstance(true, originalObject.getDatas(), classE);
		// 再将业务参数复制到实体类
		LKBeanUtils.copyProperties(originalObject, entity, "id");

		// 国际化处理
		if (entity instanceof I_Locale) {
			if (exist == null) {
				((I_Locale) entity).setLocale(params.getLocale());
			} else {
				((I_Locale) entity).setLocale(((I_Locale) exist).getLocale());
			}
		}

		// 登录ID处理
		if (entity instanceof I_LoginId) {
			if (exist == null) {
				((I_LoginId) entity).setLoginId(params.getLoginId());
			} else {
				((I_LoginId) entity).setLoginId(((I_LoginId) exist).getLoginId());
			}
		}

		// 公司ID处理
		if (entity instanceof I_CompId) {
			if (exist == null) {
				String busCompId = params.getBusCompId();
				((I_CompId) entity).setCompId(busCompId != null ? busCompId : params.getCompId());
			} else {
				((I_CompId) entity).setCompId(((I_CompId) exist).getCompId());
			}
		}

		// 在用状态处理
		if (entity instanceof I_UsingStatus) {
			((I_UsingStatus) entity).setUsingStatus(LKUsingStatusEnum.USING);
		}

		return entity;
	}


	/**
	 * 业务规则校验
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @return 需要执行新增操作返回true，否则返回false。
	 * @throws LKException 不回滚时抛出的异常
	 */
	protected boolean busCheck(CI cin, ApiKeyValues<CI> params) throws LKException {
		return true;
	}


	/**
	 * 新增时数据所需的特殊操作
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 待还原实体类对象
	 */
	protected void beforeAddNew(CI cin, ApiKeyValues<CI> params, E entity) {
	}


	/**
	 * 新增子后操作
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 保存后的实体类对象
	 * @param id 主键
	 * @throws LKException 不回滚主子表操作时抛出的异常
	 */
	protected void afterSubs(CI cin, ApiKeyValues<CI> params, E entity, String id) throws LKException {
	}

}
