package com.lichkin.springframework.db.configs;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;

/**
 * 数据库配置
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
public abstract class LKDBConfigs {

	/** JPA属性 */
	private LKJpaProperties jpaProperties;

	/** 数据源 */
	private DataSource dataSource;

	/** 实体类管理对象工厂 */
	private LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

	/** 事务管理对象 */
	private PlatformTransactionManager platformTransactionManager;


	/**
	 * 构建JPA属性
	 * @return JPA属性
	 */
	LKJpaProperties jpaProperties() {
		jpaProperties = new LKJpaProperties();
		return jpaProperties;
	}


	/**
	 * 构建数据源
	 * @param properties 数据源属性
	 * @return 数据源
	 */
	DataSource dataSource() {
		dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
		return dataSource;
	}


	/**
	 * 配置实体类管理对象工厂
	 * @param builder 实体类管理对象工厂
	 * @param jpaProperties JPA属性
	 * @return 实体类管理对象工厂
	 */
	LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
		localContainerEntityManagerFactoryBean = builder.dataSource(dataSource)

				.properties(jpaProperties.toMapProperties())

				.packages(getEntityPackages())

				.persistenceUnit(getPersistenceUnit())

				.build();
		return localContainerEntityManagerFactoryBean;
	}


	/**
	 * 定义事务管理对象
	 * @return 事务管理对象
	 */
	PlatformTransactionManager platformTransactionManager() {
		platformTransactionManager = new JpaTransactionManager(localContainerEntityManagerFactoryBean.getObject());
		return platformTransactionManager;
	}


	public abstract String getPersistenceUnit();


	public abstract String getEntityPackages();

}
