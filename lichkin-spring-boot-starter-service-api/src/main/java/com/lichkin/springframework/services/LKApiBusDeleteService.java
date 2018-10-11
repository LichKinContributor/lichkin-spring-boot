package com.lichkin.springframework.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * 删除接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusDeleteService<SI extends I_ID, E extends I_ID> extends LKApiBusService<SI, Void, E> implements LKApiVoidService<SI> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusDeleteService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(SI sin) throws LKException {
		QuerySQL sql = new QuerySQL(false, classE);
		sql.in(getIdColumnResId(), sin.getId());
		List<E> listEntity = dao.getList(sql, classE);
		if (realDelete(sin)) {
			for (E entity : listEntity) {
				beforeDelete(sin, entity, entity.getId());
			}
			dao.removeList(listEntity);
		} else {
			for (E entity : listEntity) {
				((I_UsingStatus) entity).setUsingStatus(LKUsingStatusEnum.DEPRECATED);
				beforeDelete(sin, entity, entity.getId());
			}
			dao.mergeList(listEntity);
		}
	}


	/**
	 * 获取主键对应的列资源ID
	 * @return 主键对应的列资源ID
	 */
	protected abstract int getIdColumnResId();


	/**
	 * 删除数据前操作
	 * @param sin 入参
	 * @param entity 待删除数据
	 * @param id 主键
	 */
	protected void beforeDelete(SI sin, E entity, String id) {
	}


	/**
	 * 是否实际删除数据
	 * @param sin 入参
	 * @return true:实际删除;false:逻辑删除（需主表实体类实现）.
	 */
	protected boolean realDelete(SI sin) {
		return true;
	}

}
