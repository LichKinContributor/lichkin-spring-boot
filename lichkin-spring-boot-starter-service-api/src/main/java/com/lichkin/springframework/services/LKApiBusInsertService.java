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
public abstract class LKApiBusInsertService<SI, E extends I_Base> extends LKVoidApiBusService<SI, E> {

	/**
	 * 业务验证规则，为了减少查表次数，设置合适的规则判断，如登录名修改时才需要校验等。
	 * @param sin 入参
	 * @return true:进行业务规则校验;false:不进行业务规则校验.
	 */
	protected boolean needCheckExist(SI sin) {
		return false;
	}


	/**
	 * 查询冲突数据
	 * @param sin 入参
	 * @return 冲突数据
	 */
	protected List<E> findExist(SI sin) {
		return null;
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
	 * @param id 主表主键
	 */
	protected void clearSubTables(SI sin, E exist) {
	}


	/**
	 * 新增子表数据
	 * @param sin 入参
	 * @param exist 原实体类对象
	 * @param id 主表主键
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
					throw new LKRuntimeException(existErrorCode);
				}

				// 冲突数据只有一条，取冲突数据。
				E exist = listExist.get(0);

				if (!exist.getUsingStatus().equals(LKUsingStatusEnum.DEPRECATED)) {
					// 冲突数据不是删除状态，则抛异常。
					throw new LKRuntimeException(existErrorCode);
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
		E exist = LKBeanUtils.newInstance(true, sin, classE, excludeFieldNames(sin, null));

		// 保存主表数据前操作
		beforeSaveMainTable(sin, exist, null);

		// 保存主表数据
		dao.persistOne(exist);

		// 新增子表数据
		addSubTables(sin, exist);
	}

}
