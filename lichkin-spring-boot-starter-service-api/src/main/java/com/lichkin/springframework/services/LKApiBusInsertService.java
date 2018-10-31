package com.lichkin.springframework.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKBeanUtils;
import com.lichkin.framework.utils.LKClassUtils;

/**
 * 新增接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusInsertService<SI, E extends I_ID> extends LKApiBusInsertWithoutCheckerService<SI, E> {

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


	@Override
	protected boolean busCheck(SI sin, String locale, String compId, String loginId) {
		// 查询冲突数据
		final List<E> listExist = findExist(sin, locale, compId, loginId);
		if (CollectionUtils.isEmpty(listExist)) {
			return true;
		}

		// 是否强制校验
		if (forceCheck(sin, locale, compId, loginId)) {
			// 强制校验，则抛异常。
			throw new LKRuntimeException(existErrorCode(sin, locale, compId, loginId));
		}

		// 有冲突数据
		if (listExist.size() != 1) {
			// 冲突数据不只一条，则抛异常。
			throw new LKRuntimeException(existErrorCode(sin, locale, compId, loginId));
		}

		// 冲突数据只有一条，取冲突数据。
		E exist = listExist.get(0);

		// 有状态的实体类只有删除状态才可进行还原操作
		if (LKClassUtils.checkImplementsInterface(classE, I_UsingStatus.class) && !((I_UsingStatus) exist).getUsingStatus().equals(LKUsingStatusEnum.DEPRECATED)) {
			// 冲突数据不是删除状态，则抛异常。
			throw new LKRuntimeException(existErrorCode(sin, locale, compId, loginId));
		}

		// 先从入参创建新的实体对象
		E entity = LKBeanUtils.newInstance(true, sin, classE, "id");

		// 替换数据前操作
		beforeRestore(sin, locale, compId, loginId, entity, exist);

		// 使用除主键外的新数据替换原有数据
		BeanUtils.copyProperties(entity, exist, "id");

		// 保存主表数据前操作
		beforeSaveMain(sin, locale, compId, loginId, exist);

		// 保存主表数据
		dao.mergeOne(exist);

		// 保存主表数据后操作
		afterSaveMain(sin, locale, compId, loginId, exist, exist.getId());

		// 还原数据，需先清空子表数据
		clearSubs(sin, locale, compId, loginId, exist, exist.getId());

		// 新增子表数据
		addSubs(sin, locale, compId, loginId, exist, exist.getId());

		return false;
	}


	/**
	 * 查询冲突数据
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @return 冲突数据
	 */
	protected List<E> findExist(SI sin, String locale, String compId, String loginId) {
		return null;
	}


	/**
	 * 强制校验，即当数据存在时则视为校验失败，将不进行还原处理。
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @return true:报数据存在异常;false:继续校验;
	 */
	protected abstract boolean forceCheck(SI sin, String locale, String compId, String loginId);


	/**
	* 数据已存在错误编码（findExist返回结果时才使用）
	* @param sin 入参
	* @param locale 国际化
	* @param compId 公司ID
	* @param loginId 登录ID
	* @return 错误编码
	*/
	protected LKCodeEnum existErrorCode(SI sin, String locale, String compId, String loginId) {
		return LKErrorCodesEnum.EXIST;
	}


	/**
	 * 还原时数据所需的特殊操作
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 待还原实体类对象
	 * @param exist 待还原实体类对象（不能操作这个实体对象）
	 */
	protected void beforeRestore(SI sin, String locale, String compId, String loginId, E entity, E exist) {
	}

}
