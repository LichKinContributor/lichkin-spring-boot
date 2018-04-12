package com.lichkin.springframework.db.configs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.lichkin.framework.defines.LKFrameworkStatics;

/**
 * 主数据库配置
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(

		entityManagerFactoryRef = "primaryLocalContainerEntityManagerFactoryBean",

		transactionManagerRef = "primaryPlatformTransactionManager",

		basePackages = { LKFrameworkStatics.DB_PRIMARY_DAO_PACKAGES }

)
public class LKDBPrimaryConfigs extends LKDBConfigs {

	/** 数据源 */
	private DataSource primaryDataSource;

	/** 是否显示SQL语句 */
	@Value(value = "${lichkin.framework.db.primary.jpa.show-sql}")
	private String showSql;

	/** 建表方式 */
	@Value(value = "${lichkin.framework.db.primary.jpa.hibernate.ddl-auto}")
	private String ddlAuto;

	/** 命名策略 */
	@Value(value = "${lichkin.framework.db.primary.jpa.hibernate.naming.physical-strategy}")
	private String namingPhysicalStrategy;


	/**
	 * 构建数据源
	 * @return 数据源
	 */
	@Primary
	@Bean(name = "primaryDataSource")
	@ConfigurationProperties(prefix = "lichkin.framework.db.primary")
	public DataSource primaryDataSource() {
		primaryDataSource = DataSourceBuilder.create().build();
		return primaryDataSource;
	}


	/**
	 * 配置实体类管理对象工厂
	 * @param builder 实体类管理对象工厂
	 * @return 实体类管理对象工厂
	 */
	@Primary
	@Bean(name = "primaryLocalContainerEntityManagerFactoryBean")
	@DependsOn(value = "primaryDataSource")
	public LocalContainerEntityManagerFactoryBean primaryLocalContainerEntityManagerFactoryBean(final EntityManagerFactoryBuilder builder) {
		return builder.dataSource(primaryDataSource)

				.properties(buildJpaProperties(showSql, ddlAuto, namingPhysicalStrategy))

				.packages(LKFrameworkStatics.DB_PRIMARY_ENTITY_PACKAGES)

				.persistenceUnit("primaryPersistenceUnit")

				.build();
	}


	/**
	 * 定义事务管理对象
	 * @param builder 实体类管理对象工厂
	 * @return 事务管理对象
	 */
	@Primary
	@Bean(name = "primaryPlatformTransactionManager")
	@DependsOn(value = "primaryLocalContainerEntityManagerFactoryBean")
	public PlatformTransactionManager primaryPlatformTransactionManager(final EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(primaryLocalContainerEntityManagerFactoryBean(builder).getObject());
	}

}
