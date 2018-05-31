package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端相关表基本实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class LKMappedBaseSysAppNewsAppEntity extends LKMappedBaseSysAppEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 分类编码（字典） */
	@Column(nullable = false, length = CODE_LENGTH)
	private String categoryCode;

	/** 标题（超出...） */
	@Column(nullable = false, length = VALUE_16_LENGTH)
	private String title;

	/** 简介（超出...） */
	@Column(nullable = false, length = VALUE_32_LENGTH)
	private String brief;

	/** 关键字（逗号拼接） */
	@Column(nullable = false, length = VALUE_64_LENGTH)
	private String keywords;

	/** 作者（姓名） */
	@Column(nullable = false, length = NAME_LENGTH)
	private String author;

	/** 内容（body内直接可以用于展现的内容） */
	@Lob
	@Column(nullable = false)
	private String content;

	/** 外链地址 */
	@Column(nullable = false, columnDefinition = VALUE_512)
	private String linkUrl;

	/** 排序号 */
	@Column(nullable = false)
	private Byte orderId;

	/** 版本号（逗号拼接x.y.z） */
	@Column(nullable = false, columnDefinition = VALUE_1024)
	private String versions;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { super.getCheckCodeFieldValues(), categoryCode, title, brief, keywords, author, content, linkUrl, orderId, versions };
	}

}
