package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.LKDictionaryInterface;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public class LKMappedDictionaryEntity extends LKMappedBaseSysEntity implements LKDictionaryInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = -8888886666661001L;

	/** 类目编号 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String categoryCode;

	/** 字典编号 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String dictCode;

	/** 字典值 */
	@Column(nullable = false, length = VALUE_LENGTH)
	private String dictValue;

	/** 字典名称 */
	@Column(nullable = false, length = NAME_LENGTH)
	private String dictName;

	/** 字典备注 */
	@Column(nullable = false, length = VALUE_LENGTH)
	private String dictRemarks;

	/** 排序号 */
	@Column(nullable = false)
	private Short orderId;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { categoryCode, dictCode, dictValue, dictName, dictRemarks, orderId };
	}

}
