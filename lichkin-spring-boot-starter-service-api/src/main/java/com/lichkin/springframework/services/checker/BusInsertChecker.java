package com.lichkin.springframework.services.checker;

import java.util.List;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.LKCodeEnum;

/**
 * 新增接口服务类校验器
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface BusInsertChecker<SI, E extends I_ID> {

	/**
	 * 是否需要校验冲突数据
	 * @param sin 入参
	 * @return true:需要校验;false:不需要校验.
	 */
	public boolean needCheckExist(SI sin);


	/**
	 * 查询冲突数据（needCheckExist为true时才调用）
	 * @param sin 入参
	 * @return 冲突数据
	 */
	public List<E> findExist(SI sin);


	/**
	 * 数据已存在错误编码（findExist返回结果时才使用）
	 * @param sin 入参
	 * @return 错误编码
	 */
	public LKCodeEnum existErrorCode(SI sin);

}
