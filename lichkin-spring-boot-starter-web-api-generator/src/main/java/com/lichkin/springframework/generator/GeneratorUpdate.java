package com.lichkin.springframework.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Column;

import com.lichkin.framework.defines.annotations.FieldGenerator;
import com.lichkin.framework.defines.annotations.InsertType;
import com.lichkin.framework.defines.annotations.UpdateCheckType;
import com.lichkin.framework.defines.entities.I_CompId;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.framework.utils.LKFieldUtils;
import com.lichkin.springframework.generator.LKApiGenerator.GenerateInfo;

class GeneratorUpdate extends GeneratorCommon {

	@SuppressWarnings("resource")
	static void generate(GenerateInfo info) throws Exception {
		StringBuilder fields = new StringBuilder();
		StringBuilder importEnums = new StringBuilder();
		List<Field> listField = LKFieldUtils.getFieldListWithAnnotation(info.entityClass, FieldGenerator.class);
		for (Field field : listField) {
			FieldGenerator fieldGenerator = field.getAnnotation(FieldGenerator.class);
			boolean updateable = fieldGenerator.updateable();
			if (!updateable) {
				continue;
			}
			InsertType insertType = fieldGenerator.insertType();
			boolean open = false;
			switch (insertType) {
				case CHANGE_HANDLE:
					open = true;
				break;
				default:
				break;
			}
			Class<?> type = field.getType();
			boolean check = fieldGenerator.check();
			fields.append("\n");
			if (!check && !open) {
				fields.append("//");
			}
			fields.append("\t").append("private ").append(type.getSimpleName()).append(" ").append(field.getName()).append(";").append("\n");
			if (type.isEnum()) {
				if (!check && !open) {
					importEnums.append("//");
				}
				importEnums.append("import ").append(type.getName()).append(";").append("\n");
			}
		}
		String[] updateFields = info.classGenerator.updateFields();
		for (String updateField : updateFields) {
			String[] updateFieldArr = updateField.split(" ");
			fields.append("\n").append("\t").append("/** ").append(updateFieldArr[2]).append(" */");
			fields.append("\n").append("\t").append("private ").append(updateFieldArr[0]).append(" ").append(updateFieldArr[1]).append(";").append("\n");
		}

		new FileOutputStream(new File(info.dir + "/I.java")).write(

				commonReplace(info, I.replaceAll("#fields", fields.toString()).replaceAll("#importEnums", importEnums.toString())).getBytes("UTF-8")

		);

		UpdateCheckType updateCheckType = info.classGenerator.updateCheckType();

		new FileOutputStream(new File(info.dir + "/C.java")).write(

				commonReplace(info, C.replaceAll("#importStringUtils", "").replaceAll("#subOperBusType", "\"\"")).replaceAll("LKApiBusUpdateService", updateCheckType.equals(UpdateCheckType.UNCHECK) ? "LKApiBusUpdateWithoutCheckerService" : "LKApiBusUpdateService").getBytes("UTF-8")

		);

		new FileOutputStream(new File(info.dir + "/S.java")).write(

				commonReplace(info, analysisFields(info, S))

						.getBytes("UTF-8")

		);
	}


	private static String analysisFields(GenerateInfo info, String str) {
		List<Field> listFields = LKFieldUtils.getFieldListWithAnnotation(info.entityClass, FieldGenerator.class, "compId");
		StringBuilder errorCodes = new StringBuilder();
		StringBuilder checkExistFields = new StringBuilder();
		StringBuilder saveMainFields = new StringBuilder();
		StringBuilder analysisMethods = new StringBuilder();
		StringBuilder needCheckExistFields = new StringBuilder();
		boolean containsError = false;
		for (Field field : listFields) {
			String fieldName = field.getName();
			String fieldTypeName = field.getType().getSimpleName();
			FieldGenerator fieldGenerator = field.getAnnotation(FieldGenerator.class);
			InsertType insertType = fieldGenerator.insertType();
			boolean updateable = fieldGenerator.updateable();
			switch (insertType) {
				case CHANGE_HANDLE:
					handle(true, saveMainFields, fieldName, fieldTypeName);
				break;
				case HANDLE_HANDLE:
					handle(false, saveMainFields, fieldName, fieldTypeName);
				break;
				default:
				break;
			}
			boolean check = fieldGenerator.check();
			if (check && !(insertType.equals(InsertType.HANDLE_RETAIN) || insertType.equals(InsertType.HANDLE_HANDLE))) {
				checkExistFields.append(", ").append(updateable ? get("sin", fieldName) : get("entity", fieldName));
			}
			if (check && updateable) {
				boolean nullable = field.getAnnotation(Column.class).nullable();
				if (nullable) {
					String fieldValueSaved = fieldName + "Saved";
					String fieldValueIn = fieldName + "In";
					needCheckExistFields.append("\t\t").append("String ").append(fieldValueSaved).append(" = ").append(get("entity", fieldName)).append(";").append("\n");
					needCheckExistFields.append("\t\t").append("String ").append(fieldValueIn).append(" = ").append(get("sin", fieldName)).append(";").append("\n");
					needCheckExistFields.append("\t\t").append("if (((").append(fieldValueSaved).append(" == null) && (").append(fieldValueIn).append(" != null)) || ((").append(fieldValueSaved).append(" != null) && ((").append(fieldValueIn).append(" == null) || !").append(fieldValueSaved).append(".equals(").append(fieldValueIn).append(")))) {").append("\n");
					needCheckExistFields.append("\t\t\t").append("return true;").append("\n");
					needCheckExistFields.append("\t\t").append("}").append("\n");
				} else {
					needCheckExistFields.append("\t\t").append("if (!").append(eq(get("entity", fieldName), get("sin", fieldName))).append(") {").append("\n");
					needCheckExistFields.append("\t\t\t").append("return true;").append("\n");
					needCheckExistFields.append("\t\t").append("}").append("\n");
				}
			}
		}

		UpdateCheckType updateCheckType = info.classGenerator.updateCheckType();
		boolean afterSaveMain = info.classGenerator.afterSaveMain();

		StringBuilder clearSubs = new StringBuilder();
		StringBuilder addSubs = new StringBuilder();
		String[] subTables = info.classGenerator.IUSubTables();
		for (String subTable : subTables) {
			subTable = subTable.substring("Sys".length(), subTable.indexOf("Entity"));
			clearSubs.append("\t\t").append("busService.clear").append(subTable).append("(id);").append("\n");
			addSubs.append("\t\t").append("busService.add").append(subTable).append("(id, ").append(get("sin", subTable.replaceAll(info.entity.substring("Sys".length()), "") + "Ids")).append(");").append("\n");
		}

		boolean findExist = false;
		switch (updateCheckType) {
			case UNCHECK:
				str = str.replaceAll("\n\t\t#entity_EXIST\\(#errorCode\\),\n", "");
				str = str.replaceAll("LKApiBusUpdateService", "LKApiBusUpdateWithoutCheckerService");
				str = str.replaceAll("#needCheckExist", "");
				str = str.replaceAll("#findExist", "");
				str = str.replaceAll("#existErrorCode", "");
				str = str.replaceAll("#beforeSaveMain", saveMainFields.length() == 0 ? "" : beforeSaveMain());
				str = str.replaceAll("#afterSaveMain", afterSaveMain ? afterSaveMain() : "");
				str = str.replaceAll("#clearSubs", clearSubs(clearSubs.toString()));
				str = str.replaceAll("#addSubs", addSubs(addSubs.toString()));
			break;
			case CHECK:
				containsError = needCheckExistFields.length() != 0;
				findExist = containsError;
				str = str.replaceAll("#needCheckExist", !containsError ? "" : needCheckExist());
				str = str.replaceAll("#findExist", findExist ? findExist(LKClassUtils.checkImplementsInterface(info.entityClass, I_CompId.class)) : "");
				str = str.replaceAll("#existErrorCode", !containsError ? "" : existErrorCode());
				str = str.replaceAll("#beforeSaveMain", saveMainFields.length() == 0 ? "" : beforeSaveMain());
				str = str.replaceAll("#afterSaveMain", afterSaveMain ? afterSaveMain() : "");
				str = str.replaceAll("#clearSubs", clearSubs(clearSubs.toString()));
				str = str.replaceAll("#addSubs", addSubs(addSubs.toString()));
			break;
			case BUS_CHECK:
				containsError = true;
				findExist = true;
				str = str.replaceAll("#needCheckExist", "");
				str = str.replaceAll("#findExist", findExist(LKClassUtils.checkImplementsInterface(info.entityClass, I_CompId.class)));
				str = str.replaceAll("#existErrorCode", existErrorCode());
				str = str.replaceAll("#beforeSaveMain", saveMainFields.length() == 0 ? "" : beforeSaveMain());
				str = str.replaceAll("#afterSaveMain", afterSaveMain ? afterSaveMain() : "");
				str = str.replaceAll("#clearSubs", clearSubs(clearSubs.toString()));
				str = str.replaceAll("#addSubs", addSubs(addSubs.toString()));
			break;
		}

		if (!findExist) {
			str = str.replaceAll("import java.util.List;\\n\\n", "");
		}

		if (!containsError) {
			str = str.replaceAll("import com.lichkin.framework.defines.enums.LKCodeEnum;\\n", "");
			str = str.replaceAll("\\nimport lombok.Getter;\\n", "");
			str = str.replaceAll("import lombok.RequiredArgsConstructor;\\n", "");
		}

		return str//
				.replaceAll("#ErrorCodes", containsError ? ErrorCodes() : "")//
				.replaceAll("#errorCodes", containsError ? errorCodes.toString() : "")//
				.replaceAll("#needCheckExistFields", needCheckExistFields.toString())//
				.replaceAll("#checkExistFields", checkExistFields.toString())//
				.replaceAll("#saveMainFields", saveMainFields.toString())//
				.replaceAll("#analysisMethods", analysisMethods.toString())//
				.replaceAll("#LKRuntimeException", containsError ? "import com.lichkin.framework.defines.exceptions.LKRuntimeException;\n" : "")//
		;
	}


	private static final String I;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.framework.beans.impl.LKRequestIDBean;").append("\n");
		sb.append("#importEnums");
		sb.append("").append("\n");
		sb.append("import lombok.Getter;").append("\n");
		sb.append("import lombok.Setter;").append("\n");
		sb.append("").append("\n");
		sb.append("@Getter").append("\n");
		sb.append("@Setter").append("\n");
		sb.append("public class I extends LKRequestIDBean {").append("\n");
		sb.append("#fields").append("\n");
		sb.append("}").append("\n");
		I = sb.toString();
	}

	private static final String C;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("#importStringUtils");
		sb.append("import org.springframework.beans.factory.annotation.Autowired;").append("\n");
		sb.append("import org.springframework.web.bind.annotation.RequestMapping;").append("\n");
		sb.append("import org.springframework.web.bind.annotation.RestController;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.framework.defines.LKFrameworkStatics;").append("\n");
		sb.append("import com.lichkin.framework.web.annotations.LKApiType;").append("\n");
		sb.append("import com.lichkin.framework.web.enums.ApiType;").append("\n");
		sb.append("import com.lichkin.springframework.controllers.LKApiBusUpdateController;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusUpdateService;").append("\n");
		sb.append("").append("\n");
		sb.append("@RestController(\"#Controller\")").append("\n");
		sb.append("@RequestMapping(value = LKFrameworkStatics.WEB_MAPPING_API_#userType_#clientType + \"#url\")").append("\n");
		sb.append("@LKApiType(apiType = ApiType.COMPANY_BUSINESS)").append("\n");
		sb.append("public class C extends LKApiBusUpdateController<I, #entityEntity> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Autowired").append("\n");
		sb.append("	private S service;").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected LKApiBusUpdateService<I, #entityEntity> getService(I cin) {").append("\n");
		sb.append("		return service;").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected String getSubOperBusType(I cin) {").append("\n");
		sb.append("		return #subOperBusType;").append("\n");
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
		sb.append("import java.util.List;").append("\n");
		sb.append("").append("\n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired;").append("\n");
		sb.append("import org.springframework.stereotype.Service;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.application.services.bus.impl.#entityBusService;").append("\n");
		sb.append("import com.lichkin.framework.defines.enums.LKCodeEnum;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusUpdateService;").append("\n");
		sb.append("").append("\n");
		sb.append("import lombok.Getter;").append("\n");
		sb.append("import lombok.RequiredArgsConstructor;").append("\n");
		sb.append("").append("\n");
		sb.append("@Service(\"#Service\")").append("\n");
		sb.append("public class S extends LKApiBusUpdateService<I, #entityEntity> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Autowired").append("\n");
		sb.append("	private #entityBusService busService;").append("\n");

		sb.append("#ErrorCodes");

		sb.append("#needCheckExist");

		sb.append("#findExist");

		sb.append("#existErrorCode");

		sb.append("#beforeSaveMain");

		sb.append("#afterSaveMain");

		sb.append("#clearSubs");

		sb.append("#addSubs");

		sb.append("#analysisMethods");

		sb.append("").append("\n");
		sb.append("}").append("\n");
		S = sb.toString();
	}


	private static String ErrorCodes() {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Getter").append("\n");
		sb.append("	@RequiredArgsConstructor").append("\n");
		sb.append("	enum ErrorCodes implements LKCodeEnum {").append("\n");
		sb.append("").append("\n");
		sb.append("		#entity_EXIST(#errorCode),").append("\n");
		sb.append("").append("\n");
		sb.append("		;").append("\n");
		sb.append("").append("\n");
		sb.append("		private final Integer code;").append("\n");
		sb.append("").append("\n");
		sb.append("	}").append("\n");
		return sb.toString();
	}


	private static String needCheckExist() {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected boolean needCheckExist(I sin, String locale, String compId, String loginId, #entityEntity entity, String id) {").append("\n");
		sb.append("#needCheckExistFields");
		sb.append("		return false;").append("\n");
		sb.append("	}").append("\n");
		return sb.toString();
	}


	private static String findExist(boolean needCompId) {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected List<#entityEntity> findExist(I sin, String locale, String compId, String loginId, #entityEntity entity, String id) {").append("\n");
		sb.append("		return busService.findExist(id").append(needCompId ? ", compId, null" : "").append("#checkExistFields);").append("\n");
		sb.append("	}").append("\n");
		return sb.toString();
	}


	private static String existErrorCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected LKCodeEnum existErrorCode(I sin, String locale, String compId, String loginId) {").append("\n");
		sb.append("		return ErrorCodes.#entity_EXIST;").append("\n");
		sb.append("	}").append("\n");
		return sb.toString();
	}


	private static String beforeSaveMain() {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected void beforeSaveMain(I sin, String locale, String compId, String loginId, #entityEntity entity) {").append("\n");
		sb.append("#saveMainFields");
		sb.append("	}").append("\n");
		return sb.toString();
	}


	private static String afterSaveMain() {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected void afterSaveMain(I sin, String locale, String compId, String loginId, #entityEntity entity, String id) {").append("\n");
		sb.append("		// TODO").append("\n");
		sb.append("	}").append("\n");
		return sb.toString();
	}

}
