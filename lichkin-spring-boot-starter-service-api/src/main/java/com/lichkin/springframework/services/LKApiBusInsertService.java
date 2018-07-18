package com.lichkin.springframework.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.defines.entities.I_Base;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKBeanUtils;

/**
 * 新增接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusInsertService<SI, SO, E extends I_Base> extends LKApiBusChangeService<SI, SO, E> {

	@Transactional
	@Override
	public SO handle(SI in) throws LKException {
		// 查询冲突数据
		final List<E> listExist = findExist(in);
		if (CollectionUtils.isNotEmpty(listExist)) {
			// 有冲突数据
			if (listExist.size() != 1) {
				// 冲突数据不只一条，则抛异常。
				throw new LKRuntimeException(existErrorCode);
			}

			// 冲突数据只有一条，取冲突数据。
			E exist = listExist.get(0);

			if (!exist.getUsingStatus().equals(LKUsingStatusEnum.DEPRECATED)) {
				// 冲突数据不是删除状态，则抛异常。
				throw new LKRuntimeException(existErrorCode);
			}

			// 冲突数据是删除状态，改为在用状态，即还原数据。
			E entity = LKBeanUtils.newInstance(in, classE);// 先创建新的实体对象，此操作将会进行与新增一致的初始化操作。

			// 保存主表数据前操作
			beforeSaveMainTable(entity, in);

			// 使用除主键外的新数据替换原有数据
			LKBeanUtils.copyProperties(false, false, entity, exist, "id");

			// 保存主表数据
			dao.mergeOne(exist);

			// 修改数据，需先清空子表数据
			clearSubTables(exist, in);

			// 新增子表数据
			addSubTables(exist, in);

			// 返回结果
			return handleResult(exist, in);
		} else {
			// 无冲突数据，直接做新增业务。
			E entity = LKBeanUtils.newInstance(in, classE);

			// 保存主表数据前操作
			beforeSaveMainTable(entity, in);

			// 保存主表数据
			dao.persistOne(entity);

			// 新增子表数据
			addSubTables(entity, in);

			// 返回结果
			return handleResult(entity, in);
		}
	}


	/**
	 * 查询冲突数据
	 * @param in 入参对象
	 * @return 冲突数据
	 */
	protected List<E> findExist(SI in) {
		return null;
	}

}
