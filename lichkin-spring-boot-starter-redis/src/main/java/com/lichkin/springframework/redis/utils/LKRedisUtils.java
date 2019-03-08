package com.lichkin.springframework.redis.utils;

import org.springframework.data.redis.core.RedisTemplate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Redis工具类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LKRedisUtils {

	/**
	 * 设置值
	 * @param redisTemplate RedisTemplate
	 * @param key 键
	 * @param value 值
	 * @return 值
	 */
	public static <T> T set(RedisTemplate<String, Object> redisTemplate, String key, T value) {
		redisTemplate.opsForValue().set(key, value);
		return value;
	}


	/**
	 * 获取值
	 * @param redisTemplate RedisTemplate
	 * @param key 键
	 * @param defaultValue 未取到值时，返回的默认值。
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(RedisTemplate<String, Object> redisTemplate, String key, T defaultValue) {
		Object obj = redisTemplate.opsForValue().get(key);
		if (obj == null) {
			return set(redisTemplate, key, defaultValue);
		}
		return (T) obj;
	}

}