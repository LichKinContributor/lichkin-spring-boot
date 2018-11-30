package com.lichkin.springframework.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lichkin.framework.db.beans.Condition;
import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.db.beans.eq;
import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.springframework.db.daos.LKDao;

/**
 * 服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKDBService extends LKService {

	@Autowired
	protected LKDao dao;


	/**
	 * 添加ID过滤条件
	 * @param sql SQL语句
	 * @param idColumnResId ID资源ID
	 * @param id ID
	 */
	public void addConditionId(QuerySQL sql, int idColumnResId, String id) {
		if (idColumnResId == 0) {
			return;
		}
		if (StringUtils.isBlank(id)) {
			return;
		}
		sql.neq(idColumnResId, id);
	}


	/**
	 * 添加国际化过滤条件
	 * @param sql SQL语句
	 * @param localeColumnResId 国际化资源ID
	 * @param locale 国际化
	 */
	public void addConditionLocale(QuerySQL sql, int localeColumnResId, String locale) {
		if (localeColumnResId == 0) {
			return;
		}
		sql.eq(localeColumnResId, locale);
	}


	/**
	 * 添加公司ID过滤条件
	 * @param rootCheck true:过滤;false:不过滤;
	 * @param sql SQL语句
	 * @param compIdColumnResId 公司ID资源ID
	 * @param compId 公司ID
	 * @param busCompId 公司ID
	 */
	public void addConditionCompId(boolean rootCheck, QuerySQL sql, int compIdColumnResId, String compId, String busCompId) {
		if (compIdColumnResId == 0) {
			return;
		}
		if (LKFrameworkStatics.LichKin.equals(compId)) {
			if (StringUtils.isBlank(busCompId)) {
				if (rootCheck) {
					sql.eq(compIdColumnResId, compId);
				}
			} else {
				sql.eq(compIdColumnResId, busCompId);
			}
		} else {
			sql.eq(compIdColumnResId, compId);
		}
	}


	/**
	 * 添加在用状态过滤条件
	 * @param rootCheck true:过滤;false:不过滤;
	 * @param compId 公司ID
	 * @param sql SQL语句
	 * @param usingStatusColumnResId 在用状态字段资源ID
	 * @param usingStatus 在用状态（入参输入的值）
	 * @param defaultUsingStatus 默认值
	 */
	public void addConditionUsingStatus(boolean rootCheck, String compId, QuerySQL sql, int usingStatusColumnResId, LKUsingStatusEnum usingStatus, LKUsingStatusEnum... defaultUsingStatus) {
		if (usingStatusColumnResId == 0) {
			return;
		}
		if (LKFrameworkStatics.LichKin.equals(compId)) {
			if (usingStatus == null) {
				if (rootCheck) {
					sql.neq(usingStatusColumnResId, LKUsingStatusEnum.DEPRECATED);
				}
			} else {
				sql.eq(usingStatusColumnResId, usingStatus);
			}
		} else {
			if (usingStatus == null) {
				int length = defaultUsingStatus.length;
				switch (length) {
					case 0: {
						sql.eq(usingStatusColumnResId, LKUsingStatusEnum.USING);
					}
					break;
					case 1: {
						sql.eq(usingStatusColumnResId, defaultUsingStatus[0]);
					}
					break;
					case 2: {
						sql.where(

								new Condition(

										new Condition(null, new eq(usingStatusColumnResId, defaultUsingStatus[0]))

										, new Condition(false, new eq(usingStatusColumnResId, defaultUsingStatus[1]))

								)

						);
					}
					break;
					default: {
						Condition[] conditions = new Condition[length - 2];
						for (int i = 2; i < length; i++) {
							conditions[i - 2] = new Condition(false, new eq(usingStatusColumnResId, defaultUsingStatus[i]));
						}
						sql.where(

								new Condition(

										new Condition(null, new eq(usingStatusColumnResId, defaultUsingStatus[0]))

										, new Condition(false, new eq(usingStatusColumnResId, defaultUsingStatus[1]))

										, conditions

								)

						);
					}
					break;
				}
			} else {
				sql.eq(usingStatusColumnResId, usingStatus);
			}
		}
	}

}
