package com.lichkin.springframework.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.defines.entities.I_Base;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKBeanUtils;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.springframework.services.checker.BusInsertChecker;

/**
 * 新增接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusInsertService<SI, E extends I_ID> extends LKApiBusService<SI, Void, E> implements LKApiVoidService<SI>, BusInsertChecker<SI, E> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusInsertService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(SI sin) throws LKException {
		if (needCheckExist(sin)) {
			// 查询冲突数据
			final List<E> listExist = findExist(sin);
			if (CollectionUtils.isNotEmpty(listExist)) {
				// 有冲突数据
				if (listExist.size() != 1) {
					// 冲突数据不只一条，则抛异常。
					throw new LKRuntimeException(existErrorCode(sin));
				}

				// 冲突数据只有一条，取冲突数据。
				E exist = listExist.get(0);

				if (LKClassUtils.checkImplementsInterface(classE, I_Base.class)) {
					if (!((I_Base) exist).getUsingStatus().equals(LKUsingStatusEnum.DEPRECATED)) {
						// 冲突数据不是删除状态，则抛异常。
						throw new LKRuntimeException(existErrorCode(sin));
					}
				}

				// 冲突数据是删除状态，改为在用状态，即还原数据。
				E entity = LKBeanUtils.newInstance(true, sin, classE, excludeFieldNames(sin, exist));// 先创建新的实体对象，此操作将会进行与新增一致的初始化操作。

				// 保存主表数据前操作
				beforeSaveMainTable(sin, entity, exist);

				// 使用除主键外的新数据替换原有数据
				LKBeanUtils.copyProperties(entity, exist, "id");

				// 保存主表数据
				dao.mergeOne(exist);

				// 修改数据，需先清空子表数据
				clearSubTables(sin, exist);

				// 新增子表数据
				addSubTables(sin, exist);
			} else {
				doAdd(sin);
			}
		} else {
			doAdd(sin);
		}
	}


	/**
	 * 新增
	 * @param sin 入参
	 */
	private void doAdd(SI sin) {
		// 无冲突数据，直接做新增业务。
		E entity = LKBeanUtils.newInstance(true, sin, classE, excludeFieldNames(sin, null));

		// 保存主表数据前操作
		beforeSaveMainTable(sin, entity, null);

		// 保存主表数据
		dao.persistOne(entity);

		// 新增子表数据
		addSubTables(sin, entity);
	}


	/**
	 * 保存主表数据前操作
	 * @param sin 入参
	 * @param entity 待新增实体类对象
	 * @param exist 待还原实体类对象（非还原数据时为null）
	 */
	protected void beforeSaveMainTable(SI sin, E entity, E exist) {
	}


	/**
	 * 清除子表数据
	 * @param sin 入参
	 * @param exist 原实体类对象
	 */
	protected void clearSubTables(SI sin, E exist) {
	}


	/**
	 * 新增子表数据
	 * @param sin 入参
	 * @param exist 原实体类对象
	 */
	protected void addSubTables(SI sin, E exist) {
	}


	/**
	 * 从入参复制参数时需要忽略的字段
	 * @param sin 入参
	 * @param exist 原实体类对象
	 * @return 字段名数组
	 */
	protected String[] excludeFieldNames(SI sin, E exist) {
		return new String[] { "id" };
	}

}
