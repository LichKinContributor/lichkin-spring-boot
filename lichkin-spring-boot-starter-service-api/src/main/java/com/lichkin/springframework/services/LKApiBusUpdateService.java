package com.lichkin.springframework.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.defines.entities.I_Base;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKBeanUtils;

/**
 * 编辑接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusUpdateService<SI extends I_ID, E extends I_Base> extends LKApiBusChangeService<SI, E> {

	/**
	 * 查询冲突数据
	 * @param sin 入参
	 * @param entity 实体类对象
	 * @return 冲突数据
	 */
	protected List<E> findExist(SI sin, E entity) {
		return null;
	}


	/**
	 * 业务验证规则，为了减少查表次数，设置合适的规则判断，如登录名修改时才需要校验等。
	 * @param sin 入参
	 * @param entity 实体类对象
	 * @return true:进行业务规则校验;false:不进行业务规则校验.
	 */
	protected boolean needCheckExist(SI sin, E entity) {
		return false;
	}


	@Transactional
	@Override
	public void handle(SI sin) throws LKException {
		// 通过ID找到该条数据
		E entity = dao.findOneById(classE, sin.getId());
		if (entity == null) {
			// 无数据则抛异常
			throw new LKRuntimeException(inexistentErrorCode);
		}

		// 业务规则校验
		if (needCheckExist(sin, entity)) {// 业务规则字段修改了才需要进行校验
			// 查询冲突数据
			final List<E> listExist = findExist(sin, entity);
			if (CollectionUtils.isNotEmpty(listExist)) {
				// 有冲突数据则抛异常
				throw new LKRuntimeException(existErrorCode);
			}
		}

		// 保存主表数据前操作
		beforeSaveMainTable(sin, entity, entity);

		// 修改数据
		LKBeanUtils.copyProperties(false, sin, entity, excludeFieldNames(sin, entity));

		// 保存主表数据
		dao.mergeOne(entity);

		// 修改数据，需先清空子表数据
		clearSubTables(sin, entity, entity);

		// 新增子表数据
		addSubTables(sin, entity, entity);
	}

}
