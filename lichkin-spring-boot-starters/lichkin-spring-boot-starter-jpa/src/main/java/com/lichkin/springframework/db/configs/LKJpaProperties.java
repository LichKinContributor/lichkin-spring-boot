package com.lichkin.springframework.db.configs;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

/**
 * JPA配置属性
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Setter
public class LKJpaProperties {

	/**
	 * 将JPA属性转换为Map类型属性
	 * @return Map类型属性
	 */
	public Map<String, Object> toMapProperties() {
		final Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.show_sql", showSql);
		properties.put("hibernate.hbm2ddl.auto", ddlAuto);
		properties.put("hibernate.physical_naming_strategy", LKPhysicalNamingStrategy.class.getName());
		return properties;
	}


	/** 是否显示SQL语句 */
	private String showSql = "false";

	/** DDL方式 */
	private String ddlAuto = "none";

}
