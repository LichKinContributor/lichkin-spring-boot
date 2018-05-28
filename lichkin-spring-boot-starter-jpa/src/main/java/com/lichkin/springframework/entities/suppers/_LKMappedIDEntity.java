package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.lichkin.framework.db.entities.suppers._LKIDInterface;

import lombok.Getter;
import lombok.Setter;

/**
 * ID实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public abstract class _LKMappedIDEntity implements _LKIDInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** ID字段长度 */
	protected static final int ID_LENGTH = 64;

	/** 编码字段长度 */
	protected static final int CODE_LENGTH = 64;

	/** 值字段长度 */
	protected static final int VALUE_BASE64_LENGTH = 20480;

	/** 值字段长度 */
	protected static final int VALUE_CONTENT = 10240000;

	/** 值字段长度 */
	protected static final int VALUE_10240_LENGTH = 10240;

	/** 值字段长度 */
	protected static final int VALUE_2048_LENGTH = 2048;

	/** 值字段长度 */
	protected static final int VALUE_1024_LENGTH = 1024;

	/** 值字段长度 */
	protected static final int VALUE_512_LENGTH = 512;

	/** 值字段长度 */
	protected static final int VALUE_256_LENGTH = 256;

	/** 值字段长度 */
	protected static final int VALUE_128_LENGTH = 128;

	/** 值字段长度 */
	protected static final int VALUE_64_LENGTH = 64;

	/** 值字段长度 */
	protected static final int VALUE_32_LENGTH = 32;

	/** 值字段长度 */
	protected static final int VALUE_16_LENGTH = 16;

	/** 名称字段长度 */
	protected static final int NAME_LENGTH = 64;

	/** 日期字段长度 */
	protected static final int DATE_LENGTH = 10;

	/** 时间字段长度 */
	protected static final int TIME_LENGTH = 17;

	/** 系统编码字段长度 */
	protected static final int SYSTEM_TAG_LENGTH = 64;

	/** 手机号码字段长度 */
	protected static final int CELLPHONE_LENGTH = 11;

	/** 电话号码字段长度 */
	protected static final int TELEPHONE_LENGTH = 20;

	/** 邮箱字段长度 */
	protected static final int EMAIL_LENGTH = 64;

	/** 主键 */
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid.hex")
	@GeneratedValue(generator = "uuid")
	@Column(nullable = false, length = ID_LENGTH)
	private String id;

}
