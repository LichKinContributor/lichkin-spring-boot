package com.lichkin.springframework.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKBeanUtils;
import com.lichkin.springframework.services.checker.BusUpdateChecker;

/**
 * 编辑接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusUpdateService<SI extends I_ID, E extends I_ID> extends LKApiBusService<SI, Void, E> implements LKApiVoidService<SI>, BusUpdateChecker<SI, E> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusUpdateService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(SI sin) throws LKException {
		// 通过ID找到该条数据
		E exist = dao.findOneById(classE, sin.getId());
		if (exist == null) {
			// 无数据则抛异常
			throw new LKRuntimeException(LKErrorCodesEnum.INEXIST);
		}

		// 业务规则校验
		if (needCheckExist(sin, exist)) {// 业务规则字段修改了才需要进行校验
			// 查询冲突数据
			final List<E> listExist = findExist(sin, exist);
			if (CollectionUtils.isNotEmpty(listExist)) {
				// 有冲突数据则抛异常
				throw new LKRuntimeException(existErrorCode(sin));
			}
		}

		// 保存主表数据前操作
		beforeSaveMainTable(sin, exist);

		// 修改数据
		LKBeanUtils.copyProperties(false, sin, exist, excludeFieldNames(sin, exist));

		// 保存主表数据
		dao.mergeOne(exist);

		// 修改数据，需先清空子表数据
		clearSubTables(sin, exist);

		// 新增子表数据
		addSubTables(sin, exist);
	}


	/**
	 * 保存主表数据前操作
	 * @param sin 入参
	 * @param exist 原实体类对象
	 */
	protected void beforeSaveMainTable(SI sin, E exist) {
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
