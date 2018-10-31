package com.lichkin.springframework.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;

/**
 * 编辑接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusUpdateService<SI extends I_ID, E extends I_ID> extends LKApiBusUpdateWithoutCheckerService<SI, E> {

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


	@Override
	protected boolean busCheck(SI sin, String locale, String compId, String loginId, E entity, String id) {
		if (!needCheckExist(sin, locale, compId, loginId, entity, id)) {// 业务规则字段修改了才需要进行校验
			return true;
		}

		// 查询冲突数据
		final List<E> listExist = findExist(sin, locale, compId, loginId, entity, id);
		if (CollectionUtils.isNotEmpty(listExist)) {
			// 有冲突数据则抛异常
			throw new LKRuntimeException(existErrorCode(sin, locale, compId, loginId));
		}

		return true;
	}


	/**
	 * 是否需要校验冲突数据
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 原实体类对象
	 * @param id 主键
	 * @return true:校验;false:不校验;
	 */
	protected boolean needCheckExist(SI sin, String locale, String compId, String loginId, E entity, String id) {
		return false;
	}


	/**
	 * 查询冲突数据
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 原实体类对象
	 * @param id 主键
	 * @return 冲突数据
	 */
	protected List<E> findExist(SI sin, String locale, String compId, String loginId, E entity, String id) {
		return null;
	}


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

}
