package com.lichkin.springframework.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lichkin.framework.defines.annotations.FieldGenerator;
import com.lichkin.framework.defines.entities.I_CompId;
import com.lichkin.framework.defines.entities.I_Locale;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.framework.utils.LKFieldUtils;
import com.lichkin.springframework.generator.LKApiGenerator.GenerateInfo;

class GeneratorGetPage extends GeneratorCommon {

	static void generate(GenerateInfo info) throws Exception {
		generate(info, false);
	}


	@SuppressWarnings("resource")
	static void generate(GenerateInfo info, boolean list) throws Exception {
		initIn(info, list);

		initOut(info);

		new FileOutputStream(new File(info.dir + "/C.java")).write(

				commonReplace(info,

						C//
								.replaceAll("LKApiBusGetPageController", list ? "LKApiBusGetListController" : "LKApiBusGetPageController")//
								.replaceAll("LKApiBusGetPageService", list ? "LKApiBusGetListService" : "LKApiBusGetPageService")//

				).getBytes("UTF-8")

		);

		initS(info, list);
	}


	@SuppressWarnings("resource")
	private static void initIn(GenerateInfo info, boolean list) throws IOException, FileNotFoundException {
		StringBuilder fields = new StringBuilder();
		StringBuilder importEnums = new StringBuilder();

		List<Field> listField = LKFieldUtils.getFieldListWithAnnotation(info.entityClass, FieldGenerator.class);
		for (Field field : listField) {
			FieldGenerator fieldGenerator = field.getAnnotation(FieldGenerator.class);
			if (!fieldGenerator.queryCondition()) {
				continue;
			}
			Class<?> type = field.getType();
			fields.append("\n").append("\t").append("private ").append(type.getSimpleName()).append(" ").append(field.getName()).append(";").append("\n");
			if (type.isEnum()) {
				importEnums.append("import ").append(type.getName()).append(";").append("\n");
			}
		}

		String[] pageQueryConditions = info.classGenerator.pageQueryConditions();
		for (String pageQueryCondition : pageQueryConditions) {
			String[] pageQueryConditionArr = pageQueryCondition.split(" ");
			fields.append("\n").append("\t").append("/** ").append(pageQueryConditionArr[2]).append(" */");
			String pageFieldType = pageQueryConditionArr[0];
			if (pageFieldType.contains(".")) {
				importEnums.append("import ").append(pageFieldType).append(";").append("\n");
				pageFieldType = pageFieldType.substring(pageFieldType.lastIndexOf(".") + 1);
			}
			fields.append("\n").append("\t").append("private ").append(pageFieldType).append(" ").append(pageQueryConditionArr[1]).append(";").append("\n");
		}

		new FileOutputStream(new File(info.dir + "/I.java")).write(

				commonReplace(info,

						I//
								.replaceAll("LKRequestPageBean", list ? "LKRequestBean" : "LKRequestPageBean")//
								.replaceAll("#fields", fields.toString())//
								.replaceAll("#importEnums", importEnums.toString())//

				).getBytes("UTF-8")

		);
	}


	@SuppressWarnings("resource")
	private static void initOut(GenerateInfo info) throws IOException, FileNotFoundException {
		StringBuilder fields = new StringBuilder();

		List<Field> listField = LKFieldUtils.getFieldListWithAnnotation(info.entityClass, FieldGenerator.class);
		for (Field field : listField) {
			FieldGenerator fieldGenerator = field.getAnnotation(FieldGenerator.class);
			if (!fieldGenerator.resultColumn()) {
				continue;
			}
			Class<?> type = field.getType();
			String fieldName = field.getName();
			if (type.isEnum()) {
				fields.append("\n").append("\t").append("private ").append("String").append(" ").append(fieldName).append(";").append("\n");
				fields.append("\n").append("\t").append("private ").append("String").append(" ").append(fieldName + "DictCode").append(";").append("// ").append("for ").append(fieldName).append("\n");
			} else {
				fields.append("\n").append("\t").append("private ").append(type.getSimpleName()).append(" ").append(fieldName).append(";").append("\n");
			}
		}

		String[] pageResultColumns = info.classGenerator.pageResultColumns();
		for (String pageResultColumn : pageResultColumns) {
			String[] pageResultColumnArr = pageResultColumn.split(" ");
			fields.append("\n").append("\t").append("/** ").append(pageResultColumnArr[2]).append(" */");
			String pageFieldType = pageResultColumnArr[0];
			if (pageFieldType.contains(".")) {
				pageFieldType = pageFieldType.substring(pageFieldType.lastIndexOf(".") + 1);
			}
			fields.append("\n").append("\t").append("private ").append(pageFieldType).append(" ").append(pageResultColumnArr[1]).append(";").append("\n");
		}

		new FileOutputStream(new File(info.dir + "/O.java")).write(

				commonReplace(info, O.replaceAll("#fields", fields.toString())).getBytes("UTF-8")

		);
	}


	@SuppressWarnings("resource")
	private static void initS(GenerateInfo info, boolean list) throws IOException, FileNotFoundException {
		StringBuilder fields = new StringBuilder();
		StringBuilder jrs = new StringBuilder();
		StringBuilder dictionarys = new StringBuilder();

		List<Field> listField = LKFieldUtils.getFieldListWithAnnotation(info.entityClass, FieldGenerator.class);
		for (Field field : listField) {
			FieldGenerator fieldGenerator = field.getAnnotation(FieldGenerator.class);
			if (!fieldGenerator.resultColumn()) {
				continue;
			}
			Class<?> type = field.getType();
			String fieldName = field.getName();
			if (fieldGenerator.dictionary()) {
				dictionarys.append("\t\t").append("LKDictUtils." + fieldName + "(sql, #entityR." + fieldName + ", i++);").append("\n");
			} else {
				if (type.isEnum()) {
					dictionarys.append("\t\t").append("LKDictUtils." + fieldName + "(sql, #entityR." + fieldName + ", i++);").append("\n");
				} else {
					fields.append("\t\t").append("sql.select(#entityR.").append(fieldName).append(");").append("\n");
				}
			}
		}

		String[] pageResultColumns = info.classGenerator.pageResultColumns();
		for (String pageResultColumn : pageResultColumns) {
			String[] pageResultColumnArr = pageResultColumn.split(" ");
			jrs.append("\t\t").append("sql.select(").append(pageResultColumnArr[3]).append(".").append(pageResultColumnArr[1]).append(");").append("\n");
		}

		StringBuilder conditions = new StringBuilder();
		if (LKClassUtils.checkImplementsInterface(info.entityClass, I_Locale.class)) {
			conditions.append("\t\t").append("if (!LKFrameworkStatics.LichKin.equals(compId)) {").append("\n");
			conditions.append("\t\t\t").append("sql.eq(#entityR.locale, locale)").append(";").append("\n");
			conditions.append("\t\t").append("}").append("\n").append("\n");
		}

		for (Field field : listField) {
			String fieldName = field.getName();
			FieldGenerator fieldGenerator = field.getAnnotation(FieldGenerator.class);
			if (!fieldGenerator.queryCondition() || fieldName.equals("compId") || fieldName.equals("usingStatus")) {
				continue;
			}
			Class<?> type = field.getType();
			conditions.append("\t\t").append(type.getSimpleName()).append(" ").append(fieldName).append(" = ").append(get("sin", fieldName)).append(";").append("\n");
			conditions.append("\t\t").append("if (").append(type.isEnum() ? fieldName + " != null" : "StringUtils.isNotBlank(" + fieldName + ")").append(") {").append("\n");
			conditions.append("\t\t\t").append("sql.").append(type.isEnum() || !fieldGenerator.queryConditionLike() ? "eq(#entityR." + fieldName + ", " + fieldName + ")" : "like(" + (fieldGenerator.dictionary() ? "SysDictionaryR.dictName" : "#entityR" + "." + fieldName) + ", LikeType." + (StringUtils.containsIgnoreCase(fieldName, "cellphone") ? "RIGHT" : "ALL") + ", " + fieldName + ")").append(";").append("\n");
			conditions.append("\t\t").append("}").append("\n").append("\n");
		}

		String[] pageQueryConditions = info.classGenerator.pageQueryConditions();
		for (String pageQueryCondition : pageQueryConditions) {
			String[] pageQueryConditionArr = pageQueryCondition.split(" ");
			String pageFieldType = pageQueryConditionArr[0];
			boolean isEnum = pageFieldType.contains(".enums.");
			if (pageFieldType.contains(".")) {
				pageFieldType = pageFieldType.substring(pageFieldType.lastIndexOf(".") + 1);
			}
			String fieldName = pageQueryConditionArr[1];
			conditions.append("\t\t").append(pageFieldType).append(" ").append(fieldName).append(" = ").append(get("sin", fieldName)).append(";").append("\n");
			if (isEnum) {
				conditions.append("\t\t").append("if (").append(fieldName + " != null").append(") {").append("\n");
			} else {
				conditions.append("\t\t").append("if (").append("StringUtils.isNotBlank(" + fieldName + ")").append(") {").append("\n");
			}
			switch (fieldName) {
				case "startDate":
					conditions.append("\t\t\t").append("sql.").append("gte(#entityR.insertTime, LKDateTimeUtils.toString(LKDateTimeUtils.toDateTime(startDate, LKDateTimeTypeEnum.DATE_ONLY), LKDateTimeTypeEnum.TIMESTAMP_MIN)" + ")").append(";").append("\n");
				break;
				case "endDate":
					conditions.append("\t\t\t").append("sql.").append("lt(#entityR.insertTime, LKDateTimeUtils.toString(LKDateTimeUtils.toDateTime(endDate, LKDateTimeTypeEnum.DATE_ONLY).plusDays(1), LKDateTimeTypeEnum.TIMESTAMP_MIN)" + ")").append(";").append("\n");
				break;
				case "includes":
					conditions.append("\t\t\t").append("sql.").append("in(" + pageQueryConditionArr[3] + ".id, " + fieldName + ")").append(";").append("\n");
				break;
				case "excludes":
					conditions.append("\t\t\t").append("sql.").append("notIn(" + pageQueryConditionArr[3] + ".id, " + fieldName + ")").append(";").append("\n");
				break;
				default:
					if (isEnum) {
						conditions.append("\t\t\t").append("sql.").append("eq(" + pageQueryConditionArr[3] + "." + fieldName + ", " + fieldName + ")").append(";").append("\n");
					} else {
						if (StringUtils.containsIgnoreCase(fieldName, "cellphone")) {
							conditions.append("\t\t\t").append("sql.").append("like(" + pageQueryConditionArr[3] + "." + fieldName + ", LikeType.RIGHT, " + fieldName + ")").append(";").append("\n");
						} else if (StringUtils.endsWithIgnoreCase(fieldName, "ids")) {
							conditions.append("\t\t\t").append("sql.").append("in(" + pageQueryConditionArr[3] + ".id, " + fieldName + ")").append(";").append("\n");
						} else {
							conditions.append("\t\t\t").append("sql.").append("like(" + pageQueryConditionArr[3] + "." + fieldName + ", LikeType.ALL, " + fieldName + ")").append(";").append("\n");
						}
					}
				break;
			}
			conditions.append("\t\t").append("}").append("\n").append("\n");
		}

		boolean implemetsUsingStatus = LKClassUtils.checkImplementsInterface(info.entityClass, I_UsingStatus.class);
		new FileOutputStream(new File(info.dir + "/S.java")).write(

				commonReplace(info,

						S//
								.replaceAll("LKApiBusGetPageService", list ? "LKApiBusGetListService" : "LKApiBusGetPageService")//
								.replaceAll("#Rs", fields.toString())//
								.replaceAll("#Jrs", jrs.toString())//
								.replaceAll("#compId", LKClassUtils.checkImplementsInterface(info.entityClass, I_CompId.class) ? compId() : "")//
								.replaceAll("#usingStatus", implemetsUsingStatus ? usingStatus() : "")//
								.replaceAll("#dictionarys", dictionarys.toString())//
								.replaceAll("#conditions", conditions.toString())//

				).getBytes("UTF-8")

		);
	}


	protected static final String I;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.framework.beans.impl.LKRequestPageBean;").append("\n");
		sb.append("#importEnums");
		sb.append("").append("\n");
		sb.append("import lombok.Getter;").append("\n");
		sb.append("import lombok.Setter;").append("\n");
		sb.append("").append("\n");
		sb.append("@Getter").append("\n");
		sb.append("@Setter").append("\n");
		sb.append("public class I extends LKRequestPageBean {").append("\n");
		sb.append("#fields").append("\n");
		sb.append("}").append("\n");
		I = sb.toString();
	}

	protected static final String O;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import lombok.Getter;").append("\n");
		sb.append("import lombok.Setter;").append("\n");
		sb.append("").append("\n");
		sb.append("@Getter").append("\n");
		sb.append("@Setter").append("\n");
		sb.append("public class O {").append("\n");
		sb.append("#fields").append("\n");
		sb.append("}").append("\n");
		O = sb.toString();
	}

	protected static final String C;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired;").append("\n");
		sb.append("import org.springframework.web.bind.annotation.RequestMapping;").append("\n");
		sb.append("import org.springframework.web.bind.annotation.RestController;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.framework.defines.LKFrameworkStatics;").append("\n");
		sb.append("import com.lichkin.framework.web.annotations.LKApiType;").append("\n");
		sb.append("import com.lichkin.framework.web.enums.ApiType;").append("\n");
		sb.append("import com.lichkin.springframework.controllers.LKApiBusGetPageController;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusGetPageService;").append("\n");
		sb.append("").append("\n");
		sb.append("@RestController(\"#Controller\")").append("\n");
		sb.append("@RequestMapping(value = LKFrameworkStatics.WEB_MAPPING_API_#userType_#clientType + \"#url\")").append("\n");
		sb.append("@LKApiType(apiType = ApiType.COMPANY_BUSINESS)").append("\n");
		sb.append("public class C extends LKApiBusGetPageController<I, O, #entityEntity> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Autowired").append("\n");
		sb.append("	private S service;").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected LKApiBusGetPageService<I, O, #entityEntity> getService(I cin) {").append("\n");
		sb.append("		return service;").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		C = sb.toString();
	}

	private static final String S;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import org.apache.commons.lang3.StringUtils;").append("\n");
		sb.append("import org.springframework.stereotype.Service;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.application.utils.LKDictUtils;").append("\n");
		sb.append("import com.lichkin.framework.db.beans.Order;").append("\n");
		sb.append("import com.lichkin.framework.db.beans.QuerySQL;").append("\n");
		sb.append("import com.lichkin.framework.db.beans.#entityR;").append("\n");
		sb.append("import com.lichkin.framework.db.enums.LikeType;").append("\n");
		sb.append("import com.lichkin.framework.defines.LKFrameworkStatics;").append("\n");
		sb.append("import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusGetPageService;").append("\n");
		sb.append("").append("\n");
		sb.append("@Service(\"#Service\")").append("\n");
		sb.append("public class S extends LKApiBusGetPageService<I, O, #entityEntity> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected void initSQL(I sin, String locale, String compId, String loginId, QuerySQL sql) {").append("\n");
		sb.append("		// 主表").append("\n");
		sb.append("#Rs");
		sb.append("\n");
		sb.append("		// 关联表").append("\n");
		sb.append("#Jrs");
		sb.append("\n");
		sb.append("		// 字典表").append("\n");
		sb.append("		int i = 0;").append("\n");
		sb.append("#dictionarys");
		sb.append("\n");
		sb.append("		// 筛选条件（必填项）").append("\n");
		sb.append("#compId");
		sb.append("#usingStatus");
		sb.append("\n");
		sb.append("		// 筛选条件（业务项）").append("\n");
		sb.append("#conditions");
		sb.append("		// 排序条件").append("\n");
		sb.append("		sql.addOrders(new Order(#entityR.id, false));").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		S = sb.toString();
	}

}
