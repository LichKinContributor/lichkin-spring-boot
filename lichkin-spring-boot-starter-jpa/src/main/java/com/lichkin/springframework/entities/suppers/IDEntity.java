package com.lichkin.springframework.entities.suppers;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.lichkin.framework.defines.annotations.FieldGenerator;
import com.lichkin.framework.defines.annotations.InsertType;
import com.lichkin.framework.defines.entities.I_ID;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public abstract class IDEntity implements I_ID, Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	@GenericGenerator(name = "lichkin", strategy = "com.lichkin.springframework.db.entities.LKIdentifierGenerator")
	@GeneratedValue(generator = "lichkin")
	@FieldGenerator(resultColumn = true, updateable = false, insertType = InsertType.DEFAULT_RETAIN)
	@Column(length = 64)
	private String id;

}
