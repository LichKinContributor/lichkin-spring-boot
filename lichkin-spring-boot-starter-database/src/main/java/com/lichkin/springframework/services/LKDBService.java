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
	 * 获取公司ID
	 * @param compId 公司ID
	 * @param busCompId 入参中的公司ID
	 * @return 公司ID
	 */
	protected String getCompId(String compId, String busCompId) {
		return LKFrameworkStatics.LichKin.equals(compId) && StringUtils.isNotBlank(busCompId) ? busCompId : compId;
	}


	/**
	 * 获取国际化
	 * @param locale 国际化
	 * @param busLocale 入参中的国际化
	 * @return 国际化
	 */
	protected String getLocale(String locale, String busLocale) {
		return StringUtils.isNotBlank(busLocale) ? busLocale : locale;
	}


	/**
	 * 添加公司ID过滤条件
	 * <pre>
	 *   公司ID是ROOT权限时
	 *     如果入参有值，则使用入参值匹配；
	 *     如果入参无值
	 *       rootCheck为true时，使用ROOT权限过滤；
	 *       rootCheck为false时，不过滤；
	 *
	 *   公司ID是非ROOT权限时
	 *     入参有值/无值都不使用该值，只使用登录人的公司ID；
	 * </pre>
	 * @param rootCheck true:过滤;false:不过滤;
	 * @param sql SQL语句
	 * @param compIdColumnResId 公司ID资源ID
	 * @param compId 公司ID
	 * @param busCompId 业务公司ID（入参输入的值）
	 */
	protected void addConditionCompId(boolean rootCheck, QuerySQL sql, int compIdColumnResId, String compId, String busCompId) {
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
	 * <pre>
	 *   公司ID是ROOT权限时
	 *     如果入参有值，则使用入参值匹配；
	 *     如果入参无值，则默认排除删除状态；
	 *
	 *   公司ID是非ROOT权限时
	 *     如果入参有值，则使用入参值匹配；
	 *     如果入参无值，则默认匹配默认值（如果默认值为null则匹配在用状态）；
	 * </pre>
	 * @param sql SQL语句
	 * @param usingStatusColumnResId 在用状态字段资源ID
	 * @param compId 公司ID
	 * @param usingStatus 在用状态（入参输入的值）
	 * @param defaultUsingStatus 默认值
	 */
	protected void addConditionUsingStatus(QuerySQL sql, int usingStatusColumnResId, String compId, LKUsingStatusEnum usingStatus, LKUsingStatusEnum... defaultUsingStatus) {
		if (LKFrameworkStatics.LichKin.equals(compId)) {
			if (usingStatus == null) {
				sql.neq(usingStatusColumnResId, LKUsingStatusEnum.DEPRECATED);
			} else {
				sql.eq(usingStatusColumnResId, usingStatus);
			}
		} else {
			if (usingStatus == null) {
				if (defaultUsingStatus == null) {
					sql.eq(usingStatusColumnResId, LKUsingStatusEnum.USING);
				} else {
					if (defaultUsingStatus.length == 1) {
						sql.eq(usingStatusColumnResId, defaultUsingStatus);
					} else {
						Condition condition0 = new Condition(null, new eq(usingStatusColumnResId, defaultUsingStatus[0]));
						Condition condition1 = new Condition(false, new eq(usingStatusColumnResId, defaultUsingStatus[1]));
						if (defaultUsingStatus.length == 2) {
							sql.where(new Condition(condition0, condition1));
						} else {
							Condition[] conditions = new Condition[defaultUsingStatus.length - 2];
							for (int i = 2; i < defaultUsingStatus.length; i++) {
								conditions[i - 2] = new Condition(false, new eq(usingStatusColumnResId, defaultUsingStatus[i]));
							}
							sql.where(new Condition(condition0, condition1, conditions));
						}
					}
				}
			} else {
				sql.eq(usingStatusColumnResId, usingStatus);
			}
		}
	}

}
