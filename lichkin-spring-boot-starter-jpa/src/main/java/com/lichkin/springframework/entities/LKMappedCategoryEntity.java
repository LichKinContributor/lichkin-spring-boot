package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.LKCategoryInterface;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 类目表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class LKMappedCategoryEntity extends LKMappedBaseSysEntity implements LKCategoryInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 父类目编号 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String parentCode;

	/** 类目编号 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String categoryCode;

	/** 类目值 */
	@Column(nullable = false, length = VALUE_128_LENGTH)
	private String categoryValue;

	/** 类目名称 */
	@Column(nullable = false, length = NAME_LENGTH)
	private String categoryName;

	/** 排序号 */
	@Column(nullable = false)
	private Short orderId;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { parentCode, categoryCode, categoryValue, categoryName, orderId };
	}

}
