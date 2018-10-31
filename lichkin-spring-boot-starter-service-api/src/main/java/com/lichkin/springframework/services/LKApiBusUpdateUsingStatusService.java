package com.lichkin.springframework.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * 修改在用状态接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusUpdateUsingStatusService<SI extends I_UsingStatus, E extends I_UsingStatus> extends LKApiBusService<SI, Void, E> implements LKApiVoidService<SI> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusUpdateUsingStatusService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(SI sin, String locale, String compId, String loginId) throws LKException {
		// 取业务字段值
		String id = sin.getId();
		LKUsingStatusEnum usingStatus = sin.getUsingStatus();

		// 查询主表信息
		List<E> listEntity = findListByIds(id);
		if (CollectionUtils.isNotEmpty(listEntity)) {
			beforeSaveMains(sin, locale, compId, loginId, listEntity);

			// 修改主表状态
			for (E entity : listEntity) {
				beforeSaveMain(sin, locale, compId, loginId, entity, entity.getId());
				entity.setUsingStatus(usingStatus);
				dao.mergeOne(entity);
				afterSaveMain(sin, locale, compId, loginId, entity, entity.getId());
			}

		}

		afterSaveMains(sin, locale, compId, loginId, listEntity);
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
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 实体类对象
	 * @param id 主键
	 */
	protected void beforeSaveMain(SI sin, String locale, String compId, String loginId, E entity, String id) {
	}


	/**
	 * 保存主表数据前操作
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param listEntity 实体类列表对象
	 */
	protected void beforeSaveMains(SI sin, String locale, String compId, String loginId, List<E> listEntity) {
	}


	/**
	 * 保存主表数据后操作
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 实体类对象
	 * @param id 主键
	 */
	protected void afterSaveMain(SI sin, String locale, String compId, String loginId, E entity, String id) {
	}


	/**
	 * 保存主表数据后操作
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param listEntity 实体类列表对象
	 */
	protected void afterSaveMains(SI sin, String locale, String compId, String loginId, List<E> listEntity) {
	}


	/**
	 * 获取主键对应的列资源ID
	 * @return 主键对应的列资源ID
	 */
	protected abstract int getIdColumnResId();

}
