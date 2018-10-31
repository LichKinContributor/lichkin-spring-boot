package com.lichkin.springframework.generator;

import java.lang.reflect.Field;
import java.util.List;

import com.lichkin.framework.defines.annotations.FieldGenerator;
import com.lichkin.framework.defines.annotations.InsertType;
import com.lichkin.framework.utils.LKFieldUtils;
import com.lichkin.framework.utils.LKStringUtils;
import com.lichkin.springframework.generator.LKApiGenerator.GenerateInfo;

class GeneratorCommon {

	protected static String getFields(boolean insert, boolean update, boolean ignoreEnumerated, boolean changeEnumerated, boolean addDictCode, String entity, String... excludeFieldNames) throws Exception {
		StringBuilder sb = new StringBuilder();
		Class<?> clazz = Class.forName("com.lichkin.springframework.entities.impl." + entity + "Entity");

		List<Field> listField = LKFieldUtils.getFieldListWithAnnotation(clazz, FieldGenerator.class);
		for (Field field : listField) {
			FieldGenerator fieldGenerator = field.getAnnotation(FieldGenerator.class);
			if (insert) {
				InsertType insertType = fieldGenerator.insertType();
				switch (insertType) {
					case DEFAULT_DEFAULT:
					case HANDLE_RETAIN:
					case HANDLE_ERROR:
					case HANDLE_HANDLE:
						continue;
					default:
				}
			}
			sb.append("\n");
			String fieldName = field.getName();
			if (field.getType().isEnum()) {
				if (changeEnumerated) {
					if (!insert) {
						sb.append("//");
					}
					sb.append("\tprivate ").append("String").append(" ").append(fieldName).append(";").append("// ").append(field.getType().getSimpleName()).append("\n");
				} else {
					if (!ignoreEnumerated) {
						if (!insert) {
							sb.append("//");
						}
						sb.append("\t@Enumerated(EnumType.STRING)").append("\n");
					}
					if (!insert) {
						sb.append("//");
					}
					sb.append("\tprivate ").append(field.getType().getSimpleName()).append(" ").append(fieldName).append(";").append("\n");
				}
				if (addDictCode) {
					sb.append("\n");
					if (!insert) {
						sb.append("//");
					}
					sb.append("\tprivate ").append("String").append(" ").append(fieldName + "DictCode").append(";");
					sb.append("// ").append("for ").append(fieldName);
					sb.append("\n");
				}
			} else {
				if (!fieldName.equals("id")) {
					if (!insert) {
						sb.append("//");
					}
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


	protected static String commonReplace(GenerateInfo info, String str) {
		String result = str.replaceAll("#packageName", info.packageName)

				.replaceAll("#entity", info.entity)

				.replaceAll("#url", "/" + info.entity + "/" + info.type.getName() + (info.index == 0 ? "" : LKStringUtils.fillZero(info.index, 2)))

				.replaceAll("#Controller", info.entity + info.type.getName() + LKStringUtils.fillZero(info.index, 2) + "Controller")

				.replaceAll("#Service", info.entity + info.type.getName() + LKStringUtils.fillZero(info.index, 2) + "Service")

				.replaceAll("#clientType", info.apiType)

				.replaceAll("#userType", info.userType)

				.replaceAll("#errorCode", Integer.toString(info.errorCode));
		return result;
	}


	protected static String get(String obj, String fieldName) {
		return obj + ".get" + LKStringUtils.capitalize(fieldName) + "()";
	}


	protected static String set(String obj, String fieldName, String value) {
		return m(obj + ".set", fieldName, value);
	}


	protected static String m(String methodName, String fieldName, String value) {
		return methodName + LKStringUtils.capitalize(fieldName) + "(" + value + ")";
	}


	protected static String eq(String source, String target) {
		return source + ".equals(" + target + ")";
	}


	protected static void copy(StringBuilder sb, String fieldName) {
		sb.append("\t\t").append(set("entity", fieldName, get("sin", fieldName))).append(";").append("\n");
	}


	protected static void retain(StringBuilder sb, String fieldName) {
		sb.append("\t\t").append(set("entity", fieldName, get("exist", fieldName))).append(";").append("\n");
	}


	protected static void handle(boolean change, StringBuilder sb, String fieldName, String fieldTypeName) {
		sb.append("\t\t").append(set("entity", fieldName, m("busService.analysis", fieldName, change ? get("sin", fieldName) : ""))).append(";").append("\n");
	}


	protected static void error(int errorCode, StringBuilder errorCodes, StringBuilder sb, String fieldName, String fieldTypeName) {
		String errorCodeStr = "#entity_" + LKStringUtils.humpToUnderline(fieldName).toLowerCase() + "_can_not_modify_when_restore";
		errorCodes.append("\t\t").append(errorCodeStr).append("(").append(errorCode).append(")").append(",").append("\n\n");
		sb.append("\t\t").append("if (!").append(eq(get("exist", fieldName), get("entity", fieldName))).append(") {").append("\n");
		sb.append("\t\t").append("\t").append("throw new LKRuntimeException(ErrorCodes.").append(errorCodeStr).append(").withParam(\"#").append(fieldName).append("\", ").append(get("exist", fieldName)).append(");").append("\n");
		sb.append("\t\t").append("}\n");
	}


	protected static String clearSubs(String clearSubs) {
		if (clearSubs.length() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected void clearSubs(I sin, String locale, String compId, String loginId, #entityEntity entity, String id) {").append("\n");
		sb.append(clearSubs.toString());
		sb.append("	}").append("\n");
		return sb.toString();
	}


	protected static String addSubs(String addSubs) {
		if (addSubs.length() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected void addSubs(I sin, String locale, String compId, String loginId, #entityEntity entity, String id) {").append("\n");
		sb.append(addSubs.toString());
		sb.append("	}").append("\n");
		return sb.toString();
	}

}
