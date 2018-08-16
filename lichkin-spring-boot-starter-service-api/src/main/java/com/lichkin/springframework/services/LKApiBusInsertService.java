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
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusInsertService<SI, E extends I_Base> extends LKApiBusChangeService<SI, E> {

	/** 待还原数据 */
	protected E exist;

	@Transactional
	@Override
	public void handle(SI in) throws LKException {
		this.in = in;

		if (needCheckExist()) {
			// 查询冲突数据
			final List<E> listExist = findExist();
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

				this.exist = exist;

				// 冲突数据是删除状态，改为在用状态，即还原数据。
				E entity = LKBeanUtils.newInstance(true, in, classE, excludeFieldNames());// 先创建新的实体对象，此操作将会进行与新增一致的初始化操作。

				this.entity = entity;

				// 保存主表数据前操作
				beforeSaveMainTable();

				// 使用除主键外的新数据替换原有数据
				LKBeanUtils.copyProperties(entity, exist, "id");

				// 保存主表数据
				dao.mergeOne(exist);

				this.entity = exist;

				// 修改数据，需先清空子表数据
				clearSubTables();

				// 新增子表数据
				addSubTables();
			} else {
				doAdd();
			}
		} else {
			doAdd();
		}
	}


	/**
	 * 新增
	 */
	private void doAdd() {
		// 无冲突数据，直接做新增业务。
		E entity = LKBeanUtils.newInstance(true, in, classE, excludeFieldNames());

		this.entity = entity;

		// 保存主表数据前操作
		beforeSaveMainTable();

		// 保存主表数据
		dao.persistOne(entity);

		// 新增子表数据
		addSubTables();
	}

}
