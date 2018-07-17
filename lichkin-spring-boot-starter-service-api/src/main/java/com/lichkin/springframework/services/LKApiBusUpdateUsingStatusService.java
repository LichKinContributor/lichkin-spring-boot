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
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusUpdateUsingStatusService<SI extends I_UsingStatus, SO, E extends I_UsingStatus> extends LKApiBusService<SI, SO, E> {

	@Transactional
	@Override
	public SO handle(SI in) throws LKException {
		// 取业务字段值
		String id = in.getId();
		LKUsingStatusEnum usingStatus = in.getUsingStatus();

		// 查询主表信息
		List<E> listEntity = findListByIds(id);
		if (CollectionUtils.isNotEmpty(listEntity)) {
			// 修改主表状态
			for (E entity : listEntity) {
				entity.setUsingStatus(usingStatus);
			}
			dao.mergeList(listEntity);
		}

		afterSaveMainTable(listEntity, in);

		// 返回结果
		return handleResult(listEntity, in);
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
	 * 保存主表数据后操作
	 * @param listEntity 实体类列表对象
	 * @param in 入参对象
	 */
	protected void afterSaveMainTable(List<E> listEntity, SI in) {
	}


	/**
	 * 获取主键对应的列资源ID
	 * @return 主键对应的列资源ID
	 */
	protected abstract int getIdColumnResId();


	/**
	 * 返回结果
	 * @param listEntity 实体类列表对象
	 * @param in 入参对象
	 * @return 结果
	 */
	protected SO handleResult(List<E> listEntity, SI in) {
		try {
			return classSO.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}
