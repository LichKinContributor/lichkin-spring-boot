package com.lichkin.springframework.db.configs;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.Getter;

/**
 * 数据库配置
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
class LKDBConfigs {

	/** 数据源 */
	protected DataSource dataSource;

	/** 实体类管理对象工厂 */
	protected LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

	/** 事务管理对象 */
	protected PlatformTransactionManager platformTransactionManager;


	/**
	 * 构建JPA属性
	 * @param showSql 是否显示SQL语句
	 * @param ddlAuto 建表方式
	 * @param namingPhysicalStrategy 命名策略
	 * @return JPA属性
	 */
	protected Map<String, Object> buildJpaProperties(String showSql, String ddlAuto, String namingPhysicalStrategy) {
		final Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.show_sql", showSql);
		properties.put("hibernate.hbm2ddl.auto", ddlAuto);
		properties.put("hibernate.physical_naming_strategy", namingPhysicalStrategy);
		return properties;
	}

}
