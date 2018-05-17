package com.lichkin.springframework.db;

import com.lichkin.framework.defines.LKFrameworkStatics;

/**
 * 数据库常量定义
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface LKDBPrimaryStatics {

	/** 前缀 */
	public static final String KEY = "primary";

	/** 配置前缀 */
	public static final String CONFIG_KEY_PREFIX = "lichkin.framework.db." + KEY;

	/** 持久定义KEY */
	public static final String PERSISTENCE_UNIT = KEY + "PersistenceUnit";

	/** 实体类管理对象工厂定义KEY */
	public static final String LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN = KEY + "LocalContainerEntityManagerFactoryBean";

	/** 数据源定义KEY */
	public static final String DATA_SOURCE = KEY + "DataSource";

	/** 数据源属性定义KEY */
	public static final String DATA_SOURCE_PORPERTEIS = KEY + "DataSourceProperties";

	/** 事务管理对象定义KEY */
	public static final String PLATFORM_TRANSACTION_MANAGER = KEY + "PlatformTransactionManager";

	/** dao对象名称 */
	public static final String DAO_NAME = "dao";

	/** dao扫描包 */
	public static final String DAO_PACKAGES = LKFrameworkStatics.DB_PRIMARY_DAO_PACKAGES;

	/** entity扫描包 */
	public static final String ENTITY_PACKAGES = LKFrameworkStatics.DB_PRIMARY_ENTITY_PACKAGES;

}
