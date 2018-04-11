package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.lichkin.framework.defines.entities.LKCategoryInterface;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 类目表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@Entity
@Table(name = "T_SYS_CATEGORY")
public class SysCategoryEntity extends LKMappedBaseSysEntity implements LKCategoryInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 8888886666660003L;

	/** 父类目编号 */
	@Column(name = "PARENT_CODE", length = 64)
	private String parentCode = "ROOT";

	/** 类目编号 */
	@Column(name = "CATEGORY_CODE", length = 64)
	private String categoryCode = "";

	/** 类目名称 */
	@Column(name = "CATEGORY_NAME", length = 64)
	private String categoryName = "";

	/** 类目值 */
	@Column(name = "CATEGORY_VALUE", length = 200)
	private String categoryValue = "";

	/** 排序号 */
	@Column(name = "ORDER_ID", length = 4)
	private Short orderId = 0;


	@Override
	public void updateCheckCode() {
		setCheckCode(initCheckCode(parentCode, categoryCode, categoryName, categoryValue, orderId));
	}

}
