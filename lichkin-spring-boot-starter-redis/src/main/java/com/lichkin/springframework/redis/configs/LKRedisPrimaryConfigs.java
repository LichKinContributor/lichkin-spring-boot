package com.lichkin.springframework.redis.configs;

import static com.lichkin.springframework.redis.LKRedisPrimaryStatics.CONFIG_KEY_PREFIX;
import static com.lichkin.springframework.redis.LKRedisPrimaryStatics.REDIS_CONNECTION_FACTORY_BEAN;
import static com.lichkin.springframework.redis.LKRedisPrimaryStatics.REDIS_PORPERTEIS;
import static com.lichkin.springframework.redis.LKRedisPrimaryStatics.REDIS_TEMPLATE_BEAN;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Redis库配置
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Configuration
@Getter(value = AccessLevel.PACKAGE)
public class LKRedisPrimaryConfigs extends LKReidsConfigs {

	@Primary
	@Bean(name = REDIS_PORPERTEIS)
	@ConfigurationProperties(prefix = CONFIG_KEY_PREFIX)
	@Override
	public LKRedisProperties redisProperties() {
		return super.redisProperties();
	}


	@Primary
	@Bean(name = REDIS_CONNECTION_FACTORY_BEAN)
	@DependsOn(value = { REDIS_PORPERTEIS })
	@Override
	public RedisConnectionFactory redisConnectionFactory() {
		return super.redisConnectionFactory();
	}


	@Primary
	@Bean(name = REDIS_TEMPLATE_BEAN)
	@DependsOn(value = { REDIS_PORPERTEIS, REDIS_CONNECTION_FACTORY_BEAN })
	@Override
	public RedisTemplate<String, Object> redisTemplate() {
		return super.redisTemplate();
	}

}
