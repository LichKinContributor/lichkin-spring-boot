package com.lichkin.springframework.db.daos;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Enumerated;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.lichkin.framework.db.entities.suppers._LKBaseInsertInterface;
import com.lichkin.framework.db.entities.suppers._LKBaseInterface;
import com.lichkin.framework.db.entities.suppers._LKBaseSysInterface;
import com.lichkin.framework.db.entities.suppers._LKIDInterface;
import com.lichkin.framework.db.entities.suppers._LKNormalInterface;
import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.enums.impl.LKRangeTypeEnum;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKRandomUtils;
import com.lichkin.framework.utils.LKStringUtils;

/**
 * 数据库访问辅助工具类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
class LKDaoUtils {

	/**
	 * 获取映射字段集合
	 * @param clazz 查询结果映射对象类型
	 * @param resultKeySet 结果KEY值列表
	 * @return 映射字段集合
	 */
	static <B> Map<String, Field> getMappingFields(Class<B> clazz, Set<String> resultKeySet) {
		List<Field> allFields = FieldUtils.getAllFieldsList(clazz);
		Map<String, Field> mappedFields = new HashMap<>();
		for (String resultKey : resultKeySet) {
			for (int i = allFields.size() - 1; i >= 0; i--) {
				Field field = allFields.get(i);
				String fieldName = field.getName();
				String mappingName = LKStringUtils.humpToUnderline(fieldName);
				if (mappingName.equalsIgnoreCase(resultKey) || fieldName.equals(resultKey)) {
					field.setAccessible(true);
					mappedFields.put(resultKey, field);
					allFields.remove(i);
					break;
				}
			}
		}
		return mappedFields;
	}


	/**
	 * 集合转换为对象
	 * @param <B> 返回值类型为clazz参数定义的类型
	 * @param clazz 查询结果映射对象类型
	 * @param map 集合
	 * @return 对象
	 */
	static <B> B map2bean(Class<B> clazz, Map<String, Object> map) {
		return map2bean(clazz, map, getMappingFields(clazz, map.keySet()));
	}


	/**
	 * 集合转换为对象
	 * @param <B> 返回值类型为clazz参数定义的类型
	 * @param clazz 查询结果映射对象类型
	 * @param map 集合
	 * @param mappginFields 映射字段集合
	 * @return 对象
	 */
	static <B> B map2bean(Class<B> clazz, Map<String, Object> map, Map<String, Field> mappginFields) {
		try {
			B bean = clazz.newInstance();
			for (Entry<String, Field> mappingField : mappginFields.entrySet()) {
				Object value = map.get(mappingField.getKey());
				if (value == null) {
					continue;
				}
				Field field = mappingField.getValue();
				Class<?> fieldType = field.getType();
				final String fieldTypeName = fieldType.getName();
				if (fieldType.isEnum()) {// 字段类型是枚举定义的，则读取枚举注解中配置的值。
					try {
						final Enumerated enumerated = field.getAnnotation(Enumerated.class);
						Object[] enumValues = Class.forName(fieldTypeName).getEnumConstants();
						switch (enumerated.value()) {
							case STRING:// 字符串
								for (final Object enumValue : enumValues) {
									if (value.equals(enumValue.toString())) {
										field.set(bean, enumValue);
									}
								}
							break;
							case ORDINAL:// 数字
								field.set(bean, enumValues[Integer.valueOf(value.toString())]);
							break;
							default:
							break;
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();// ignore this
					}
				} else {
					switch (fieldTypeName) {
						case "java.lang.Byte":
							field.set(bean, Byte.valueOf(value.toString()));
						break;
						case "java.lang.Short":
							field.set(bean, Short.valueOf(value.toString()));
						break;
						case "java.lang.Integer":
							field.set(bean, Integer.valueOf(value.toString()));
						break;
						case "java.lang.Long":
							field.set(bean, Long.valueOf(value.toString()));
						break;
						case "java.lang.Float":
							field.set(bean, Float.valueOf(value.toString()));
						break;
						case "java.lang.Double":
							field.set(bean, Double.valueOf(value.toString()));
						break;
						case "java.lang.Boolean":
							field.set(bean, Boolean.valueOf(value.toString()));
						break;
						default:
							field.set(bean, value);
						break;
					}
				}
			}
			return bean;
		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();// ignore this
			return null;
		}
	}


	/**
	 * 集合列表转换为对象列表
	 * @param <B> 返回值类型为clazz参数定义的类型
	 * @param clazz 查询结果映射对象类型
	 * @param listMap 集合列表
	 * @return 对象列表
	 */
	static <B> List<B> listMap2listBean(Class<B> clazz, List<Map<String, Object>> listMap) {
		if (CollectionUtils.isEmpty(listMap)) {
			return Collections.emptyList();
		}

		List<B> listBean = new ArrayList<>(listMap.size());
		Map<String, Field> mappingFields = getMappingFields(clazz, listMap.get(0).keySet());
		for (Map<String, Object> map : listMap) {
			listBean.add(map2bean(clazz, map, mappingFields));
		}
		return listBean;
	}


	/**
	 * 构建COUNT语句
	 * @param sql SQL/HQL语句
	 * @return COUNT语句
	 */
	static String createCountSQL(String sql) {
		return "SELECT COUNT(1) FROM" + sql.substring(StringUtils.indexOfIgnoreCase(sql, "FROM") + "FROM".length());
	}


	/**
	 * 初始化对象
	 * @param obj 实体类对象
	 */
	static void initEntity(final _LKIDInterface obj) {
		final String currentTime = LKDateTimeUtils.now();
		final String systemTag = LKFrameworkStatics.SYSTEM_TAG;
		final String loginId = getLoginId();

		if (StringUtils.isBlank(obj.getId())) {// 新增数据
			// TODO ID init

			// Normal check
			if ((obj instanceof _LKNormalInterface)) {
				// Normal init
				if (((_LKNormalInterface) obj).getUsingStatus() == null) {
					((_LKNormalInterface) obj).setUsingStatus(LKUsingStatusEnum.USING);
				}

				// Base check
				if ((obj instanceof _LKBaseInsertInterface)) {
					// Base init
					((_LKBaseInsertInterface) obj).setInsertTime(currentTime);
					((_LKBaseInsertInterface) obj).setInsertSystemTag(systemTag);
					((_LKBaseInsertInterface) obj).setInsertLoginId(loginId);

					// Base check
					if ((obj instanceof _LKBaseInterface)) {
						((_LKBaseInterface) obj).setUpdateTime(currentTime);
						((_LKBaseInterface) obj).setUpdateSystemTag(systemTag);
						((_LKBaseInterface) obj).setUpdateLoginId(loginId);

						// Sys check
						if ((obj instanceof _LKBaseSysInterface)) {
							// Sys init
							if (((_LKBaseSysInterface) obj).getSystemTag() == null) {
								((_LKBaseSysInterface) obj).setSystemTag(systemTag);
							}
							if (((_LKBaseSysInterface) obj).getBusId() == null) {
								((_LKBaseSysInterface) obj).setBusId(LKRandomUtils.create(64, LKRangeTypeEnum.NUMBER_AND_LETTER_FULL));
							}
							((_LKBaseSysInterface) obj).updateCheckCode();
						}
					}
				}
			}
		} else {// 更新数据
			// Base check
			if ((obj instanceof _LKBaseInterface)) {
				// Base init
				((_LKBaseInterface) obj).setUpdateTime(currentTime);
				((_LKBaseInterface) obj).setUpdateSystemTag(systemTag);
				((_LKBaseInterface) obj).setUpdateLoginId(loginId);

				// Sys check
				if ((obj instanceof _LKBaseSysInterface)) {
					// Sys init
					((_LKBaseSysInterface) obj).updateCheckCode();
				}
			}
		}
	}


	/**
	 * 获取登录人登录ID
	 * @return 登录人登录ID
	 */
	static String getLoginId() {
		String loginId = null;
		try {// web项目从session中取当前登录人ID
			final Class<?> clazz = Class.forName("com.lichkin.framework.springboot.web.utils.LKSessionUserUtilsOnSpring");
			final Method method = clazz.getMethod("getLoginId");
			loginId = (String) method.invoke(clazz);
		} catch (final Exception exception) {// TODO 非web项目取当前任务ID
			loginId = "GUEST";
		}
		return loginId;
	}

}
