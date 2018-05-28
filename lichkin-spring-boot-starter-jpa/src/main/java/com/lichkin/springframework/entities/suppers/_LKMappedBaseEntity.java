package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.suppers._LKBaseInterface;

import lombok.Getter;
import lombok.Setter;

/**
 * 一般业务实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class _LKMappedBaseEntity extends _LKMappedNormalEntity implements _LKBaseInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 新增操作系统编码 */
	@Column(nullable = false, length = SYSTEM_TAG_LENGTH)
	private String insertSystemTag;

	/** 新增操作时间（yyyyMMddHHmmssSSS） */
	@Column(nullable = false, length = TIME_LENGTH)
	private String insertTime;

	/** 新增操作人登录ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String insertLoginId;

	/** 更新操作系统编码 */
	@Column(nullable = false, length = SYSTEM_TAG_LENGTH)
	private String updateSystemTag;

	/** 更新操作时间（yyyyMMddHHmmssSSS） */
	@Column(nullable = false, length = TIME_LENGTH)
	private String updateTime;

	/** 更新操作人登录ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String updateLoginId;


	/**
	 * 设置新增操作系统编码
	 * @param insertSystemTag 新增操作系统编码
	 * @deprecated 系统将自动注入值，手动注入将失效。
	 */
	@Deprecated
	@Override
	public void setInsertSystemTag(String insertSystemTag) {
		this.insertSystemTag = insertSystemTag;
	}


	/**
	 * 设置新增操作时间
	 * @param insertTime 新增操作时间
	 * @deprecated 系统将自动注入值，手动注入将失效。
	 */
	@Deprecated
	@Override
	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}


	/**
	 * 设置新增操作人登录ID
	 * @param insertLoginId 新增操作人登录ID
	 * @deprecated 系统将自动注入值，手动注入将失效。
	 */
	@Deprecated
	@Override
	public void setInsertLoginId(String insertLoginId) {
		this.insertLoginId = insertLoginId;
	}


	/**
	 * 设置更新操作系统编码
	 * @param updateSystemTag 更新操作系统编码
	 * @deprecated 系统将自动注入值，手动注入将失效。
	 */
	@Deprecated
	@Override
	public void setUpdateSystemTag(String updateSystemTag) {
		this.updateSystemTag = updateSystemTag;
	}


	/**
	 * 设置更新操作时间
	 * @param updateTime 更新操作时间
	 * @deprecated 系统将自动注入值，手动注入将失效。
	 */
	@Deprecated
	@Override
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}


	/**
	 * 设置更新操作人登录ID
	 * @param updateLoginId 更新操作人登录ID
	 * @deprecated 系统将自动注入值，手动注入将失效。
	 */
	@Deprecated
	@Override
	public void setUpdateLoginId(String updateLoginId) {
		this.updateLoginId = updateLoginId;
	}

}
