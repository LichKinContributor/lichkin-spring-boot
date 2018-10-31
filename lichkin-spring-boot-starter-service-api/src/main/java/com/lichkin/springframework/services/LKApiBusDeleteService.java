package com.lichkin.springframework.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.utils.LKClassUtils;

/**
 * 删除接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusDeleteService<SI extends I_ID, E extends I_ID> extends LKApiBusService<SI, Void, E> implements LKApiVoidService<SI> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusDeleteService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(SI sin, String locale, String compId, String loginId) throws LKException {
		// 先查寻出要删除的数据
		QuerySQL sql = new QuerySQL(false, classE);
		sql.in(getIdColumnResId(), sin.getId());
		List<E> listEntity = dao.getList(sql, classE);

		// 判断在用状态实现情况
		if (LKClassUtils.checkImplementsInterface(classE, I_UsingStatus.class) && !realDelete(sin, locale, compId, loginId)) {
			for (E entity : listEntity) {
				((I_UsingStatus) entity).setUsingStatus(LKUsingStatusEnum.DEPRECATED);
				beforeLogicDelete(sin, locale, compId, loginId, entity, entity.getId());
			}
			dao.mergeList(listEntity);
		} else {
			for (E entity : listEntity) {
				beforeRealDelete(sin, locale, compId, loginId, entity, entity.getId());
			}
			dao.removeList(listEntity);
		}
	}


	/**
	 * 获取主键对应的列资源ID
	 * @return 主键对应的列资源ID
	 */
	protected abstract int getIdColumnResId();


	/**
	 * 是否实际删除数据（当实体类实现了I_UsingStatus时起作用）
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @return true:实际删除;false:逻辑删除;
	 */
	protected boolean realDelete(SI sin, String locale, String compId, String loginId) {
		return false;
	}


	/**
	 * 实际删除数据前操作
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 待删除数据
	 * @param id 主键
	 */
	protected void beforeRealDelete(SI sin, String locale, String compId, String loginId, E entity, String id) {
	}


	/**
	 * 逻辑删除数据前操作
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 待删除数据
	 * @param id 主键
	 */
	protected void beforeLogicDelete(SI sin, String locale, String compId, String loginId, E entity, String id) {
	}

}
