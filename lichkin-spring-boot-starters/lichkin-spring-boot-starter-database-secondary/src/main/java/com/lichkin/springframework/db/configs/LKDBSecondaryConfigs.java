package com.lichkin.springframework.db.configs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.lichkin.framework.defines.LKFrameworkStatics;

/**
 * 从数据库配置
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(

		entityManagerFactoryRef = "secondaryLocalContainerEntityManagerFactoryBean",

		transactionManagerRef = "secondaryPlatformTransactionManager",

		basePackages = { LKFrameworkStatics.DB_SECONDARY_DAO_PACKAGES }

)
public class LKDBSecondaryConfigs extends LKDBConfigs {

	/** 数据源 */
	private DataSource secondaryDataSource;


	/**
	 * 获取数据源
	 * @return 数据源
	 */
	public DataSource getSecondaryDataSource() {
		return secondaryDataSource;
	}


	/** 实体类管理对象工厂 */
	private LocalContainerEntityManagerFactoryBean secondaryLocalContainerEntityManagerFactoryBean;


	/**
	 * 获取实体类管理对象工厂
	 * @return 实体类管理对象工厂
	 */
	public LocalContainerEntityManagerFactoryBean getSecondaryLocalContainerEntityManagerFactoryBean() {
		return secondaryLocalContainerEntityManagerFactoryBean;
	}


	/** 事务管理对象 */
	private PlatformTransactionManager secondaryPlatformTransactionManager;


	/**
	 * 获取事务管理对象
	 * @return 事务管理对象
	 */
	public PlatformTransactionManager getSecondaryPlatformTransactionManager() {
		return secondaryPlatformTransactionManager;
	}


	/** 是否显示SQL语句 */
	@Value(value = "${lichkin.framework.db.secondary.jpa.show-sql:false}")
	private String showSql;

	/** 建表方式 */
	@Value(value = "${lichkin.framework.db.secondary.jpa.hibernate.ddl-auto:none}")
	private String ddlAuto;

	/** 命名策略 */
	@Value(value = "${lichkin.framework.db.secondary.jpa.hibernate.naming.physical-strategy:com.lichkin.springframework.db.configs.LKPhysicalNamingStrategy}")
	private String namingPhysicalStrategy;


	/**
	 * 构建数据源
	 * @return 数据源
	 */
	@Bean(name = "secondaryDataSource")
	@ConfigurationProperties(prefix = "lichkin.framework.db.secondary")
	public DataSource secondaryDataSource() {
		secondaryDataSource = DataSourceBuilder.create().build();
		return secondaryDataSource;
	}


	/**
	 * 配置实体类管理对象工厂
	 * @param builder 实体类管理对象工厂
	 * @return 实体类管理对象工厂
	 */
	@Bean(name = "secondaryLocalContainerEntityManagerFactoryBean")
	@DependsOn(value = "secondaryDataSource")
	public LocalContainerEntityManagerFactoryBean secondaryLocalContainerEntityManagerFactoryBean(final EntityManagerFactoryBuilder builder) {
		secondaryLocalContainerEntityManagerFactoryBean = builder.dataSource(secondaryDataSource)

				.properties(buildJpaProperties(showSql, ddlAuto, namingPhysicalStrategy))

				.packages(LKFrameworkStatics.DB_SECONDARY_ENTITY_PACKAGES)

				.persistenceUnit("secondaryPersistenceUnit")

				.build();
		return secondaryLocalContainerEntityManagerFactoryBean;
	}


	/**
	 * 定义事务管理对象
	 * @param builder 实体类管理对象工厂
	 * @return 事务管理对象
	 */
	@Bean(name = "secondaryPlatformTransactionManager")
	@DependsOn(value = "secondaryLocalContainerEntityManagerFactoryBean")
	public PlatformTransactionManager secondaryPlatformTransactionManager(final EntityManagerFactoryBuilder builder) {
		secondaryPlatformTransactionManager = new JpaTransactionManager(secondaryLocalContainerEntityManagerFactoryBean(builder).getObject());
		return secondaryPlatformTransactionManager;
	}

}
