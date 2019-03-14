package com.lichkin.springframework.redis.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
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
	 * @param <T> 值对象泛型
	 * @param redisTemplate RedisTemplate
	 * @param redisKey 键
	 * @param value 值
	 * @return 值
	 */
	public static <T> T set(RedisTemplate<String, Object> redisTemplate, String redisKey, T value) {
		redisTemplate.opsForValue().set(redisKey, value);
		return value;
	}


	/**
	 * 获取值
	 * @param <T> 值对象泛型
	 * @param redisTemplate RedisTemplate
	 * @param redisKey 键
	 * @param defaultValue 未取到值时，返回的默认值。
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(RedisTemplate<String, Object> redisTemplate, String redisKey, T defaultValue) {
		Object obj = redisTemplate.opsForValue().get(redisKey);
		if (obj == null) {
			return defaultValue == null ? null : set(redisTemplate, redisKey, defaultValue);
		}
		return (T) obj;
	}


	/**
	 * 设置值
	 * @param <T> 值对象泛型
	 * @param redisTemplate RedisTemplate
	 * @param redisKey 键
	 * @param value 值
	 * @return 值
	 */
	public static <T> List<T> setList(RedisTemplate<String, Object> redisTemplate, String redisKey, List<T> value) {
		// 清空列表
		while (redisTemplate.opsForList().size(redisKey) > 0) {
			redisTemplate.opsForList().leftPop(redisKey);
		}
		if (CollectionUtils.isNotEmpty(value)) {
			redisTemplate.opsForList().rightPushAll(redisKey, value.toArray());
		}
		return value;
	}


	/**
	 * 获取值
	 * @param <T> 值对象泛型
	 * @param redisTemplate RedisTemplate
	 * @param redisKey 键
	 * @param defaultValue 未取到值时，返回的默认值。
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getList(RedisTemplate<String, Object> redisTemplate, String redisKey, List<T> defaultValue) {
		List<Object> obj = redisTemplate.opsForList().range(redisKey, 0, -1);
		if (obj == null) {
			return defaultValue == null ? null : setList(redisTemplate, redisKey, defaultValue);
		}
		return (List<T>) obj;
	}


	/**
	 * 设置值
	 * @param <T> 值对象泛型
	 * @param redisTemplate RedisTemplate
	 * @param redisKey 键
	 * @param value 值
	 * @return 值
	 */
	public static <T> T addToList(RedisTemplate<String, Object> redisTemplate, String redisKey, T value) {
		redisTemplate.opsForList().rightPush(redisKey, value);
		return value;
	}


	/**
	 * 设置值
	 * @param <T> 值对象泛型
	 * @param redisTemplate RedisTemplate
	 * @param redisKey 键
	 * @param value 值
	 * @return 值
	 */
	public static <T> T setToList(RedisTemplate<String, Object> redisTemplate, String redisKey, T value) {
		List<T> list = getList(redisTemplate, redisKey, new ArrayList<>());
		for (int i = 0; i < list.size(); i++) {
			T t = list.get(i);
			if ((t != null) && (value != null) && t.equals(value)) {
				redisTemplate.opsForList().set(redisKey, i, value);
				return value;
			}
		}
		return addToList(redisTemplate, redisKey, value);
	}


	/**
	 * 获取值
	 * @param <T> 值对象泛型
	 * @param redisTemplate RedisTemplate
	 * @param redisKey 键
	 * @param index 索引值
	 * @param defaultValue 未取到值时，返回的默认值。
	 * @return 值
	 */
	public static <T> T getFromList(RedisTemplate<String, Object> redisTemplate, String redisKey, int index, T defaultValue) {
		List<T> list = getList(redisTemplate, redisKey, null);
		if (CollectionUtils.isEmpty(list)) {
			return defaultValue == null ? null : defaultValue;
		}
		return list.get(index);
	}


	/**
	 * 设置值
	 * @param <T> 值对象泛型
	 * @param redisTemplate RedisTemplate
	 * @param redisKey 键
	 * @param mapKey 键
	 * @param value 值
	 * @return 值
	 */
	public static <T> T set(RedisTemplate<String, Object> redisTemplate, String redisKey, String mapKey, T value) {
		redisTemplate.opsForHash().put(redisKey, mapKey, value);
		return value;
	}


	/**
	 * 获取值
	 * @param <T> 值对象泛型
	 * @param redisTemplate RedisTemplate
	 * @param redisKey 键
	 * @param mapKey 键
	 * @param defaultValue 未取到值时，返回的默认值。
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(RedisTemplate<String, Object> redisTemplate, String redisKey, String mapKey, T defaultValue) {
		Object obj = redisTemplate.opsForHash().get(redisKey, mapKey);
		if (obj == null) {
			return defaultValue == null ? null : set(redisTemplate, redisKey, mapKey, defaultValue);
		}
		return (T) obj;
	}


	/**
	 * 获取值
	 * @param <T> 值对象泛型
	 * @param redisTemplate RedisTemplate
	 * @param redisKey 键
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> getMap(RedisTemplate<String, Object> redisTemplate, String redisKey) {
		Map<Object, Object> redisMap = redisTemplate.opsForHash().entries(redisKey);
		if (redisMap == null) {
			return new HashMap<>();
		}
		Map<String, T> map = new HashMap<>();
		for (Entry<Object, Object> entry : redisMap.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			map.put(key == null ? null : key.toString(), value == null ? null : (T) value);
		}
		return map;
	}

}
