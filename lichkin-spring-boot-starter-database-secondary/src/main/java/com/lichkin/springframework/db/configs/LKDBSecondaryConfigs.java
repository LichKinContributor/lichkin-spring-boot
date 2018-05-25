package com.lichkin.springframework.db.configs;

import static com.lichkin.springframework.db.LKDBSecondaryStatics.DAO_PACKAGES;
import static com.lichkin.springframework.db.LKDBSecondaryStatics.DATA_SOURCE;
import static com.lichkin.springframework.db.LKDBSecondaryStatics.DATA_SOURCE_PORPERTEIS_CONFIG_KEY_PREFIX;
import static com.lichkin.springframework.db.LKDBSecondaryStatics.ENTITY_PACKAGES;
import static com.lichkin.springframework.db.LKDBSecondaryStatics.JPA_PORPERTEIS;
import static com.lichkin.springframework.db.LKDBSecondaryStatics.JPA_PORPERTEIS_CONFIG_KEY_PREFIX;
import static com.lichkin.springframework.db.LKDBSecondaryStatics.LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN;
import static com.lichkin.springframework.db.LKDBSecondaryStatics.PERSISTENCE_UNIT;
import static com.lichkin.springframework.db.LKDBSecondaryStatics.PLATFORM_TRANSACTION_MANAGER;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.AccessLevel;
import lombok.Getter;

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
@Getter(value = AccessLevel.PACKAGE)
public class LKDBSecondaryConfigs extends LKDBConfigs {

	@Bean(name = JPA_PORPERTEIS)
	@ConfigurationProperties(prefix = JPA_PORPERTEIS_CONFIG_KEY_PREFIX)
	@Override
	public LKJpaProperties jpaProperties() {
		return super.jpaProperties();
	}


	@Bean(name = DATA_SOURCE)
	@ConfigurationProperties(prefix = DATA_SOURCE_PORPERTEIS_CONFIG_KEY_PREFIX)
	@Override
	public DataSource dataSource() {
		return super.dataSource();
	}


	@Bean(name = LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN)
	@DependsOn(value = { DATA_SOURCE, JPA_PORPERTEIS })
	@Override
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
		return super.localContainerEntityManagerFactoryBean(builder);
	}


	@Bean(name = PLATFORM_TRANSACTION_MANAGER)
	@DependsOn(value = { DATA_SOURCE, JPA_PORPERTEIS, LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN })
	@Override
	public PlatformTransactionManager platformTransactionManager() {
		return super.platformTransactionManager();
	}


	@Override
	public String getPersistenceUnit() {
		return PERSISTENCE_UNIT;
	}


	@Override
	public String getEntityPackages() {
		return ENTITY_PACKAGES;
	}

}
