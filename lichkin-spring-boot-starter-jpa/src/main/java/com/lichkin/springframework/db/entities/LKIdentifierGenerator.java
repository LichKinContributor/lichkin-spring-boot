package com.lichkin.springframework.db.entities;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.lichkin.framework.defines.enums.impl.LKRangeTypeEnum;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKRandomUtils;

/**
 * 自定义主键生成策略
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKIdentifierGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return LKDateTimeUtils.now() + LKRandomUtils.create(47, LKRangeTypeEnum.NUMBER_AND_LETTER_FULL);
	}

}
