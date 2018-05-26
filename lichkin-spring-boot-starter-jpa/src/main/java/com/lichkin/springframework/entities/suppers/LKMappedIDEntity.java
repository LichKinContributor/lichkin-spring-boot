package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.suppers.LKIDInterface;

import lombok.Getter;
import lombok.Setter;

/**
 * ID实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public class LKMappedIDEntity extends _LKMappedIDEntity implements LKIDInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = -1L;

	/** 公司ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String compId;

}
