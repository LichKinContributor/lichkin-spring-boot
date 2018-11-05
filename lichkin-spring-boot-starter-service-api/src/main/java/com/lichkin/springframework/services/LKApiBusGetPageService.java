package com.lichkin.springframework.services;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.beans.LKPageable;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * 获取分页数据接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusGetPageService<SI extends LKPageable, SO, E extends I_ID> extends LKApiBusGetPLService<SI, SO, E> implements LKApiService<SI, Page<SO>> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusGetPageService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = (Class<SO>) types[1];
		classE = (Class<E>) types[2];
	}


	@Override
	public Page<SO> handle(SI sin, String locale, String compId, String loginId) throws LKException {
		Page<SO> page = beforeQuery(sin, locale, compId, loginId);
		if (page != null) {
			return page;
		}

		QuerySQL sql = new QuerySQL(!classE.equals(classSO), classE, isDistinct());

		initSQL(sin, locale, compId, loginId, sql);

		sql.setPage(sin);

		return afterQuery(sin, locale, compId, loginId, dao.getPage(sql, classSO));
	}


	/**
	 * 返回空的分页数据
	 * @return 空的分页数据
	 */
	protected <T> Page<T> emptyPage() {
		return new PageImpl<>(Collections.emptyList());
	}


	/**
	 * 返回结果
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @return 出参
	 */
	protected Page<SO> beforeQuery(SI sin, String locale, String compId, String loginId) {
		return null;
	}


	/**
	 * 查询语句执行后处理
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param page 查询结果
	 * @return 出参
	 */
	protected Page<SO> afterQuery(SI sin, String locale, String compId, String loginId, Page<SO> page) {
		return page;
	}

}
