package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.LKMenuInterface;
import com.lichkin.framework.defines.enums.impl.LKYesNoEnum;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 菜单表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class LKMappedMenuEntity extends LKMappedBaseSysEntity implements LKMenuInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 父菜单编号 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String parentCode;

	/** 菜单编号 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String menuCode;

	/** 菜单名称 */
	@Column(nullable = false, length = NAME_LENGTH)
	private String menuName;

	/** 链接地址 */
	@Column(nullable = false, length = VALUE_128_LENGTH)
	private String url;

	/** 排序号 */
	@Column(nullable = false)
	private Short orderId;

	/** 图标 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String icon;

	/** 是否可分配（枚举） */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 3)
	private LKYesNoEnum assignable;

	/** 是否需要权限（枚举） */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 3)
	private LKYesNoEnum auth;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { super.getCheckCodeFieldValues(), parentCode, menuCode, menuName, url, orderId, icon, assignable, auth };
	}

}
