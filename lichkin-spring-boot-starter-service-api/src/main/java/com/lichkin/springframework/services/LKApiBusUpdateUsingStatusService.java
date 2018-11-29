package com.lichkin.springframework.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.entities.I_UsingStatus_ID;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 修改在用状态接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusUpdateUsingStatusService<CI extends I_ID, E extends I_UsingStatus_ID> extends LKApiBusService<CI, Void, E> implements LKApiVoidService<CI> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusUpdateUsingStatusService() {
		super();
		classCI = (Class<CI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(CI cin, ApiKeyValues<CI> params) throws LKException {
		// 取业务字段值
		String id = params.getId();
		LKUsingStatusEnum usingStatus = params.getObject("_usingStatus");

		// 查询主表信息
		List<E> listEntity = findListByIds(id);
		if (CollectionUtils.isNotEmpty(listEntity)) {
			beforeSaveMains(cin, params, listEntity);

			// 修改主表状态
			for (E entity : listEntity) {
				beforeSaveMain(cin, params, entity, entity.getId());
				entity.setUsingStatus(usingStatus);
				dao.mergeOne(entity);
				afterSaveMain(cin, params, entity, entity.getId());
			}

		}

		afterSaveMains(cin, params, listEntity);
	}


	/**
	 * 查询主表信息
	 * @param ids 主表ID（单个/多个）
	 * @return 主表信息
	 */
	private List<E> findListByIds(String ids) {
		QuerySQL sql = new QuerySQL(false, classE);

		sql.in(getIdColumnResId(), ids);

		return dao.getList(sql, classE);
	}


	/**
	 * 保存主表数据前操作
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 实体类对象
	 * @param id 主键
	 */
	protected void beforeSaveMain(CI cin, ApiKeyValues<CI> params, E entity, String id) {
	}


	/**
	 * 保存主表数据前操作
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param listEntity 实体类列表对象
	 */
	protected void beforeSaveMains(CI cin, ApiKeyValues<CI> params, List<E> listEntity) {
	}


	/**
	 * 保存主表数据后操作
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 实体类对象
	 * @param id 主键
	 */
	protected void afterSaveMain(CI cin, ApiKeyValues<CI> params, E entity, String id) {
	}


	/**
	 * 保存主表数据后操作
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param listEntity 实体类列表对象
	 */
	protected void afterSaveMains(CI cin, ApiKeyValues<CI> params, List<E> listEntity) {
	}


	/**
	 * 获取主键对应的列资源ID
	 * @return 主键对应的列资源ID
	 */
	protected abstract int getIdColumnResId();

}
