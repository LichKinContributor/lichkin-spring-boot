package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.lichkin.framework.defines.entities.suppers.LKIDInterface;

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
public class LKMappedIDEntity implements LKIDInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = -8888886666660000L;

	/** ID字段长度 */
	protected static final int ID_LENGTH = 64;

	/** 编码字段长度 */
	protected static final int CODE_LENGTH = 64;

	/** 时间字段长度 */
	protected static final int TIME_LENGTH = 17;

	/** 系统编码字段长度 */
	protected static final int SYSTEM_TAG_LENGTH = 64;

	/** 枚举字段长度 */
	protected static final int ENUM_LENGTH = 32;

	/** 主键 */
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid.hex")
	@GeneratedValue(generator = "uuid")
	@Column(nullable = false, length = ID_LENGTH)
	private String id;

	/** 公司ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String compId;

}
