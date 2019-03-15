package com.lichkin.springframework.redis.configs;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

/**
 * Redis库配置
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
public abstract class LKReidsConfigs {

	/** Redis属性 */
	private LKRedisProperties redisProperties;

	/** Redis连接工厂 */
	private RedisConnectionFactory factory;

	/** Redis工具 */
	private RedisTemplate<String, Object> redisTemplate;


	/**
	 * 构建Redis属性
	 * @return Redis属性
	 */
	LKRedisProperties redisProperties() {
		redisProperties = new LKRedisProperties();
		return redisProperties;
	}


	/**
	 * 构建Redis连接工厂
	 * @return Redis连接工厂
	 */
	RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisProperties.getHostName());
		redisStandaloneConfiguration.setPort(redisProperties.getPort());
		redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
		redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));

		factory = new LettuceConnectionFactory(redisStandaloneConfiguration);

		return factory;
	}


	/**
	 * 构建Redis工具
	 * @return Redis工具
	 */
	RedisTemplate<String, Object> redisTemplate() {
		redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(factory);

		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);

		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		jackson2JsonRedisSerializer.setObjectMapper(new ObjectMapper().setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY).enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL));
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

		redisTemplate.afterPropertiesSet();

		return redisTemplate;
	}

}