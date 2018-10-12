package com.lichkin.springframework.generator;

import java.lang.reflect.Field;
import java.util.List;

import com.lichkin.framework.utils.LKFieldUtils;
import com.lichkin.framework.utils.LKStringUtils;
import com.lichkin.springframework.generator.LKApiGenerator.Type;

class GeneratorCommon {

	protected static String getFields(boolean ignoreEnumerated, boolean changeEnumerated, boolean addDictCode, String entity, String... excludeFieldNames) throws Exception {
		StringBuilder sb = new StringBuilder();
		Class<?> clazz = Class.forName("com.lichkin.springframework.entities.impl." + entity + "Entity");

		List<Field> listFields = LKFieldUtils.getRealFieldList(clazz, true, excludeFieldNames);
		for (int i = listFields.size() - 1; i >= 0; i--) {
			Field field = listFields.get(i);
			sb.append("\n");
			String fieldName = field.getName();
			if (field.getType().isEnum()) {
				if (changeEnumerated) {
					sb.append("//");
					sb.append("\tprivate ").append("String").append(" ").append(fieldName).append(";").append("// ").append(field.getType().getSimpleName()).append("\n");
				} else {
					if (!ignoreEnumerated) {
						sb.append("//\t@Enumerated(EnumType.STRING)").append("\n");
					}
					sb.append("//");
					sb.append("\tprivate ").append(field.getType().getSimpleName()).append(" ").append(fieldName).append(";").append("\n");
				}
				if (addDictCode) {
					sb.append("\n");
					sb.append("//");
					sb.append("\tprivate ").append("String").append(" ").append(fieldName + "DictCode").append(";");
					sb.append("// ").append("for ").append(fieldName);
					sb.append("\n");
				}
			} else {
				if (!fieldName.equals("id")) {
					sb.append("//");
				}
				sb.append("\tprivate ").append(field.getType().getSimpleName()).append(" ").append(fieldName).append(";").append("\n");
			}
		}

		return sb.toString();
	}


	protected static String getFieldsR(String entity, String... excludeFieldNames) throws Exception {
		StringBuilder sb = new StringBuilder();
		Class<?> clazz = Class.forName("com.lichkin.springframework.entities.impl." + entity + "Entity");

		List<Field> listFields = LKFieldUtils.getRealFieldList(clazz, true, excludeFieldNames);
		for (int i = listFields.size() - 1; i >= 0; i--) {
			Field field = listFields.get(i);
			sb.append("//").append("\t\tsql.select(").append("#entityR.").append(field.getName()).append(");");
			if (field.getType().isEnum()) {
				sb.append("// ").append(field.getType().getSimpleName());
			}
			sb.append("\n");
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
