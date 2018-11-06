package com.lichkin.springframework.services;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lichkin.framework.db.beans.Order;
import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.beans.impl.LKPageBean;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKCodeUtils;

@Service
public class LKCodeService extends LKDBService {

	/**
	 * 创建编码，如果有同级编码则创建下一个编码，如果没有同级编码则创建一个新的编码。
	 * @param <T> 泛型
	 * @param tableClazz 实体类类型
	 * @param compId 公司ID
	 * @param parentCode 上级编码值
	 * @param codeFieldName 编码字段名称
	 * @param compIdColumnResId 公司ID字段资源ID
	 * @param parentCodeColumnResId 上级编码字段资源ID
	 * @param codeColumnResId 编码字段资源ID
	 * @param levelOutErrorCode 超出编码定义范围时的错误编码
	 * @return 编码
	 */
	public <T> String analysisCode(Class<T> tableClazz, String compId, String parentCode, String codeFieldName, int compIdColumnResId, int parentCodeColumnResId, int codeColumnResId, LKCodeEnum levelOutErrorCode) {
		// 最多只支持8级编码
		if (LKCodeUtils.currentLevel(parentCode) == 8) {
			throw new LKRuntimeException(levelOutErrorCode);
		}

		// 查询上级编码下最大的编码
		QuerySQL sql = new QuerySQL(false, tableClazz);
		if (compId != null) {
			sql.eq(compIdColumnResId, compId);
		}
		sql.eq(parentCodeColumnResId, parentCode);
		sql.setPage(new LKPageBean(1));
		sql.addOrders(new Order(codeColumnResId, false));
		Page<T> page = dao.getPage(sql, tableClazz);
		List<T> content = page.getContent();

		// 没有下级编码，则创建一个新的编码。
		if (CollectionUtils.isEmpty(content)) {
			return LKCodeUtils.createCode(parentCode);
		}

		// 有下级编码，取下一个编码。
		try {
			Field codeField = tableClazz.getDeclaredField(codeFieldName);
			codeField.setAccessible(true);
			return LKCodeUtils.nextCode(codeField.get(content.get(0)).toString());
		} catch (Exception e) {
			throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, e);
		}
	}

}
