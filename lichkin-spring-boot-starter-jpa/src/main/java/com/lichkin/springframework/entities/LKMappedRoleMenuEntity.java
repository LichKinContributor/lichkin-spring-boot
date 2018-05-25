package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.LKRoleMenuInterface;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 角色&amp;菜单关联表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public class LKMappedRoleMenuEntity extends LKMappedBaseSysEntity implements LKRoleMenuInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = -8888886666661004L;

	/** 角色ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String roleId;

	/** 菜单ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String menuId;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { roleId, menuId };
	}

}
