package com.lichkin.springframework.entities.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
@Entity
public final class SysMenuEntity extends LKMappedBaseSysEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 8888886666668001L;

	/** 父菜单编号 */
	@Column(nullable = false, length = 64)
	private String parentCode = "ROOT";

	/** 菜单编号 */
	@Column(nullable = false, length = 64)
	private String menuCode;

	/** 菜单名称 */
	@Column(nullable = false, length = 32)
	private String menuName;

	/** 链接地址 */
	@Column(nullable = false, length = 200)
	private String url = "";

	/** 排序号 */
	@Column(nullable = false)
	private Byte orderId = 1;

	/** 图标 */
	@Column(nullable = false, length = 32)
	private String icon = "";

	/** 是否可分配（枚举） */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 3)
	private LKYesNoEnum assignable = LKYesNoEnum.YES;

	/** 是否需要权限（枚举） */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 3)
	private LKYesNoEnum auth = LKYesNoEnum.YES;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { parentCode, menuCode, menuName, url, orderId, icon, assignable, auth };
	}

}
