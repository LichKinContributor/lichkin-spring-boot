package com.lichkin.springframework.generator;

import java.lang.reflect.Field;
import java.util.List;

import com.lichkin.framework.utils.LKFieldUtils;
import com.lichkin.framework.utils.LKStringUtils;
import com.lichkin.springframework.generator.LKApiGenerator.Type;

class GeneratorCommon {

	protected static String getFields(boolean ignoreEnumerated, String entity, String... excludeFieldNames) throws Exception {
		StringBuilder sb = new StringBuilder();
		Class<?> clazz = Class.forName("com.lichkin.springframework.entities.impl." + entity + "Entity");

		List<Field> listFields = LKFieldUtils.getRealFieldList(clazz, true, excludeFieldNames);
		for (Field field : listFields) {
			if (!ignoreEnumerated && field.getType().isEnum()) {
				sb.append("\n").append("\t").append("// @Enumerated(EnumType.STRING)");
			}
			sb.append("\n").append("\t").append("// private ").append(field.getType().getSimpleName()).append(" ").append(field.getName()).append(";").append("\n");
		}

		return sb.toString();
	}


	protected static String commonReplace(int index, Type type, String packageName, String entity, String str) {
		String result = str.replaceAll("#packageName", packageName)

				.replaceAll("#entity", entity)

				.replaceAll("#url", "/" + entity + "/" + type.getName() + (index == 0 ? "" : LKStringUtils.fillZero(index, 2)))

				.replaceAll("#Controller", entity + type.getName() + LKStringUtils.fillZero(index, 2) + "Controller")

				.replaceAll("#Service", entity + type.getName() + LKStringUtils.fillZero(index, 2) + "Service");
		return result;
	}

}
