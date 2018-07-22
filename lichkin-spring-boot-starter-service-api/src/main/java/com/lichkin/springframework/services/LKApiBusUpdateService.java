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

	@Transactional
	@Override
	public void handle(SI in) throws LKException {
		this.in = in;

		// 通过ID找到该条数据
		E entity = dao.findOneById(classE, in.getId());
		if (entity == null) {
			// 无数据则抛异常
			throw new LKRuntimeException(inexistentErrorCode);
		}

		this.entity = entity;

		// 业务规则校验
		if (needCheckExist()) {// 业务规则字段修改了才需要进行校验
			// 查询冲突数据
			final List<E> listExist = findExist();
			if (CollectionUtils.isNotEmpty(listExist)) {
				// 有冲突数据则抛异常
				throw new LKRuntimeException(existErrorCode);
			}
		}

		// 保存主表数据前操作
		beforeSaveMainTable();

		// 修改数据
		LKBeanUtils.copyProperties(false, in, entity, excludeFieldNames());

		// 保存主表数据
		dao.mergeOne(entity);

		// 修改数据，需先清空子表数据
		clearSubTables();

		// 新增子表数据
		addSubTables();
	}

}
