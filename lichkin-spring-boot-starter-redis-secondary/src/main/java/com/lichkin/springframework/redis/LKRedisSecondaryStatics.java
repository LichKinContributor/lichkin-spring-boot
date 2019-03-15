package com.lichkin.springframework.redis;

/**
 * Redis库常量定义
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface LKRedisSecondaryStatics {

	/** 前缀 */
	public static final String KEY = "secondary";

	/** 配置前缀 */
	public static final String CONFIG_KEY_PREFIX = "lichkin.framework.redis." + KEY;

	/** Redis属性定义KEY */
	public static final String REDIS_PORPERTEIS = KEY + "RedisProperties";

	/** Redis连接工厂定义KEY */
	public static final String REDIS_CONNECTION_FACTORY_BEAN = KEY + "RedisConnectionFactory";

	/** Redis工具定义KEY */
	public static final String REDIS_TEMPLATE_BEAN = KEY + "RedisTemplate";

}
