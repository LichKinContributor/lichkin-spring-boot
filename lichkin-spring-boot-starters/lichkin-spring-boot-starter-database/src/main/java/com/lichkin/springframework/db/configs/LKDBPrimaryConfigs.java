package com.lichkin.springframework.db.configs;

import static com.lichkin.springframework.db.LKDBPrimaryStatics.CONFIG_KEY_PREFIX;
import static com.lichkin.springframework.db.LKDBPrimaryStatics.DAO_PACKAGES;
import static com.lichkin.springframework.db.LKDBPrimaryStatics.DATA_SOURCE;
import static com.lichkin.springframework.db.LKDBPrimaryStatics.ENTITY_PACKAGES;
import static com.lichkin.springframework.db.LKDBPrimaryStatics.LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN;
import static com.lichkin.springframework.db.LKDBPrimaryStatics.PERSISTENCE_UNIT;
import static com.lichkin.springframework.db.LKDBPrimaryStatics.PLATFORM_TRANSACTION_MANAGER;

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

/**
 * 数据库配置
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(

		entityManagerFactoryRef = LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN,

		transactionManagerRef = PLATFORM_TRANSACTION_MANAGER,

		basePackages = { DAO_PACKAGES }

)
public class LKDBPrimaryConfigs extends LKDBConfigs {

	/** 是否显示SQL语句 */
	@Value(value = "${" + CONFIG_KEY_PREFIX + ".jpa.show-sql:false}")
	private String showSql;

	/** 建表方式 */
	@Value(value = "${" + CONFIG_KEY_PREFIX + ".jpa.hibernate.ddl-auto:none}")
	private String ddlAuto;

	/** 命名策略 */
	@Value(value = "${" + CONFIG_KEY_PREFIX + ".jpa.hibernate.naming.physical-strategy:com.lichkin.springframework.db.configs.LKPhysicalNamingStrategy}")
	private String namingPhysicalStrategy;


	/**
	 * 构建数据源
	 * @return 数据源
	 */
	@Primary
	@Bean(name = DATA_SOURCE)
	@ConfigurationProperties(prefix = CONFIG_KEY_PREFIX)
	public DataSource primaryDataSource() {
		dataSource = DataSourceBuilder.create().build();
		return dataSource;
	}


	/**
	 * 配置实体类管理对象工厂
	 * @param builder 实体类管理对象工厂
	 * @return 实体类管理对象工厂
	 */
	@Primary
	@Bean(name = LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN)
	@DependsOn(value = DATA_SOURCE)
	public LocalContainerEntityManagerFactoryBean primaryLocalContainerEntityManagerFactoryBean(final EntityManagerFactoryBuilder builder) {
		localContainerEntityManagerFactoryBean = builder.dataSource(dataSource)

				.properties(buildJpaProperties(showSql, ddlAuto, namingPhysicalStrategy))

				.packages(ENTITY_PACKAGES)

				.persistenceUnit(PERSISTENCE_UNIT)

				.build();
		return localContainerEntityManagerFactoryBean;
	}


	/**
	 * 定义事务管理对象
	 * @param builder 实体类管理对象工厂
	 * @return 事务管理对象
	 */
	@Primary
	@Bean(name = PLATFORM_TRANSACTION_MANAGER)
	@DependsOn(value = LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN)
	public PlatformTransactionManager primaryPlatformTransactionManager(final EntityManagerFactoryBuilder builder) {
		platformTransactionManager = new JpaTransactionManager(primaryLocalContainerEntityManagerFactoryBean(builder).getObject());
		return platformTransactionManager;
	}

}
