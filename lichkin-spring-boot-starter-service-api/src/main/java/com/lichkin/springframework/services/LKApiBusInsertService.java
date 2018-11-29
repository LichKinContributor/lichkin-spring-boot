package com.lichkin.springframework.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import com.lichkin.framework.defines.beans.LKInvokeBean;
import com.lichkin.framework.defines.beans.LKInvokeDatas;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 新增接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusInsertService<CI extends LKInvokeBean<? extends LKInvokeDatas>, E extends I_ID> extends LKApiBusInsertWithoutCheckerService<CI, E> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusInsertService() {
		super();
		classCI = (Class<CI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Override
	protected boolean busCheck(CI cin, ApiKeyValues<CI> params) throws LKException {
		// 查询冲突数据
		final List<E> listExist = findExist(cin, params);
		if (CollectionUtils.isEmpty(listExist)) {
			return true;
		}

		// 是否强制校验
		if (forceCheck(cin, params)) {
			// 强制校验，则抛异常。
			throw new LKRuntimeException(existErrorCode(cin, params));
		}

		// 有冲突数据
		if (listExist.size() != 1) {
			// 冲突数据不只一条，则抛异常。
			throw new LKRuntimeException(existErrorCode(cin, params));
		}

		// 冲突数据只有一条，取冲突数据。
		E exist = listExist.get(0);

		// 有状态的实体类只有删除状态才可进行还原操作
		if (LKClassUtils.checkImplementsInterface(classE, I_UsingStatus.class) && !((I_UsingStatus) exist).getUsingStatus().equals(LKUsingStatusEnum.DEPRECATED)) {
			// 冲突数据不是删除状态，则抛异常。
			throw new LKRuntimeException(existErrorCode(cin, params));
		}

		// 先从入参创建新的实体对象
		E entity = newInstance(params, exist);

		// 替换数据前操作
		beforeRestore(cin, params, entity, exist);

		// 使用除主键外的新数据替换原有数据
		BeanUtils.copyProperties(entity, exist, "id");

		// 保存主表数据前操作
		beforeSaveMain(cin, params, exist);

		// 保存主表数据
		dao.mergeOne(exist);

		// 保存主表数据后操作
		afterSaveMain(cin, params, exist, exist.getId());

		// 还原数据，需先清空子表数据
		clearSubs(cin, params, exist, exist.getId());

		// 新增子表数据
		addSubs(cin, params, exist, exist.getId());

		// 新增子表后操作
		afterSubs(cin, params, exist, exist.getId());

		return false;
	}


	/**
	 * 查询冲突数据
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @return 冲突数据
	 */
	protected List<E> findExist(CI cin, ApiKeyValues<CI> params) {
		return null;
	}


	/**
	 * 强制校验，即当数据存在时则视为校验失败，将不进行还原处理。
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @return true:报数据存在异常;false:继续校验;
	 */
	protected abstract boolean forceCheck(CI cin, ApiKeyValues<CI> params);


	/**
	* 数据已存在错误编码（findExist返回结果时才使用）
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	* @return 错误编码
	*/
	protected LKCodeEnum existErrorCode(CI cin, ApiKeyValues<CI> params) {
		return LKErrorCodesEnum.EXIST;
	}


	/**
	 * 还原时数据所需的特殊操作
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 待还原实体类对象
	 * @param exist 待还原实体类对象（不能操作这个实体对象）
	 */
	protected void beforeRestore(CI cin, ApiKeyValues<CI> params, E entity, E exist) {
	}

}
