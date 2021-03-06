package com.lichkin.springframework.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 删除接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusDeleteService<CI extends I_ID, E extends I_ID> extends LKApiBusService<CI, Void, E> implements LKApiVoidService<CI> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusDeleteService() {
		super();
		classCI = (Class<CI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(CI cin, ApiKeyValues<CI> params) throws LKException {
		// 先查寻出要删除的数据
		QuerySQL sql = new QuerySQL(false, classE);
		sql.in(getIdColumnResId(), cin.getId());
		List<E> listEntity = dao.getList(sql, classE);

		// 判断在用状态实现情况
		for (E entity : listEntity) {
			beforeRealDelete(cin, params, entity, entity.getId());
		}
		dao.removeList(listEntity);
	}


	/**
	 * 获取主键对应的列资源ID
	 * @return 主键对应的列资源ID
	 */
	protected abstract int getIdColumnResId();


	/**
	 * 实际删除数据前操作
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 待删除数据
	 * @param id 主键
	 */
	protected void beforeRealDelete(CI cin, ApiKeyValues<CI> params, E entity, String id) {
	}

}
