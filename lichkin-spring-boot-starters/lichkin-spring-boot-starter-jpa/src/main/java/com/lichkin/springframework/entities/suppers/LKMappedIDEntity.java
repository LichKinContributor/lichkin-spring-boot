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
	private static final long serialVersionUID = 8888886666660000L;

	/** 主键 */
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid.hex")
	@GeneratedValue(generator = "uuid")
	@Column(name = "ID", length = 64)
	protected String id;

}
