package com.lichkin.springframework.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;

import com.lichkin.framework.defines.annotations.FieldGenerator;
import com.lichkin.framework.defines.annotations.InsertCheckType;
import com.lichkin.framework.defines.annotations.InsertType;
import com.lichkin.framework.defines.entities.I_CompId;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.framework.utils.LKFieldUtils;
import com.lichkin.springframework.generator.LKApiGenerator.GenerateInfo;

class GeneratorInsert extends GeneratorCommon {

	@SuppressWarnings("resource")
	static void generate(GenerateInfo info) throws Exception {
		StringBuilder fields = new StringBuilder();
		StringBuilder importEnums = new StringBuilder();
		List<Field> listField = LKFieldUtils.getFieldListWithAnnotation(info.entityClass, FieldGenerator.class);
		for (Field field : listField) {
			FieldGenerator fieldGenerator = field.getAnnotation(FieldGenerator.class);
			InsertType insertType = fieldGenerator.insertType();
			switch (insertType) {
				// 不需要入参的情况
				case DEFAULT_DEFAULT:
				case DEFAULT_RETAIN:
				case HANDLE_RETAIN:
				case HANDLE_ERROR:
				case HANDLE_HANDLE:
					continue;
				default:
			}
			Class<?> type = field.getType();
			fields.append("\n").append("\t").append("private ").append(type.getSimpleName()).append(" ").append(field.getName()).append(";").append("\n");
			if (type.isEnum()) {
				importEnums.append("import ").append(type.getName()).append(";").append("\n");
			}
		}
		String[] insertFields = info.classGenerator.insertFields();
		for (String insertField : insertFields) {
			String[] insertFieldArr = insertField.split(" ");
			fields.append("\n").append("\t").append("/** ").append(insertFieldArr[2]).append(" */");
			fields.append("\n").append("\t").append("private ").append(insertFieldArr[0]).append(" ").append(insertFieldArr[1]).append(";").append("\n");
		}

		new FileOutputStream(new File(info.dir + "/I.java")).write(

				commonReplace(info, I.replaceAll("#fields", fields.toString()).replaceAll("#importEnums", importEnums.toString())).getBytes()

		);

		InsertCheckType insertCheckType = info.classGenerator.insertCheckType();

		new FileOutputStream(new File(info.dir + "/C.java")).write(

				commonReplace(info, C.replaceAll("#importStringUtils", LKClassUtils.checkImplementsInterface(info.entityClass, I_CompId.class) ? "import org.apache.commons.lang3.StringUtils;\n" : "").replaceAll("#subOperBusType", LKClassUtils.checkImplementsInterface(info.entityClass, I_CompId.class) ? "StringUtils.isBlank(cin.getCompId()) ? \"\" : \"Comp\"" : "null")).replaceAll("LKApiBusInsertService", insertCheckType.equals(InsertCheckType.UNCHECK) ? "LKApiBusInsertWithoutCheckerService" : "LKApiBusInsertService").getBytes()

		);

		new FileOutputStream(new File(info.dir + "/S.java")).write(

				commonReplace(info, analysisFields(info, S))

						.getBytes()

		);
	}


	private static String analysisFields(GenerateInfo info, String str) {
		List<Field> listFields = LKFieldUtils.getFieldListWithAnnotation(info.entityClass, FieldGenerator.class, "compId");
		StringBuilder errorCodes = new StringBuilder();
		StringBuilder checkExistFields = new StringBuilder();
		StringBuilder restoreFields = new StringBuilder();
		StringBuilder addNewFields = new StringBuilder();
		StringBuilder saveMainFields = new StringBuilder();
		StringBuilder analysisMethods = new StringBuilder();
		boolean containsError = false;
		boolean callThrow = false;
		boolean containsHandle = false;
		InsertCheckType insertCheckType = info.classGenerator.insertCheckType();
		for (Field field : listFields) {
			String fieldName = field.getName();
			String fieldTypeName = field.getType().getSimpleName();
			FieldGenerator fieldGenerator = field.getAnnotation(FieldGenerator.class);
			InsertType insertType = fieldGenerator.insertType();
			switch (insertType) {
				case DEFAULT_DEFAULT:
				// LKBeanUtils.newInstance(true, sin, classE);
				// LKBeanUtils.newInstance(true, sin, classE);
				break;
				case DEFAULT_RETAIN:
					// LKBeanUtils.newInstance(true, sin, classE);
					retain(restoreFields, fieldName);
				break;

				case COPY_COPY:
//					copy(saveMainFields, fieldName);
				break;
				case COPY_RETAIN:
//					copy(addNewFields, fieldName);
					retain(restoreFields, fieldName);
				break;
				case COPY_ERROR:
//					copy(addNewFields, fieldName);
					if (insertCheckType.equals(InsertCheckType.CHECK_RESTORE)) {
						callThrow = true;
						error(info.errorCode, errorCodes, restoreFields, fieldName, fieldTypeName);
					}
				break;

				case CHANGE_RETAIN:
					containsHandle = true;
					handle(true, addNewFields, fieldName, fieldTypeName);
					retain(restoreFields, fieldName);
				break;
				case CHANGE_ERROR:
					handle(true, addNewFields, fieldName, fieldTypeName);
					if (insertCheckType.equals(InsertCheckType.CHECK_RESTORE)) {
						containsHandle = true;
						callThrow = true;
						error(info.errorCode, errorCodes, restoreFields, fieldName, fieldTypeName);
					}
				break;
				case CHANGE_HANDLE:
					containsHandle = true;
					handle(true, saveMainFields, fieldName, fieldTypeName);
				break;

				case HANDLE_RETAIN:
					containsHandle = true;
					handle(false, addNewFields, fieldName, fieldTypeName);
					retain(restoreFields, fieldName);
				break;
				case HANDLE_ERROR:
					handle(false, addNewFields, fieldName, fieldTypeName);
					if (insertCheckType.equals(InsertCheckType.CHECK_RESTORE)) {
						containsHandle = true;
						callThrow = true;
						error(info.errorCode, errorCodes, restoreFields, fieldName, fieldTypeName);
					}
				break;
				case HANDLE_HANDLE:
					containsHandle = true;
					handle(false, saveMainFields, fieldName, fieldTypeName);
				break;
				default:
				break;
			}
			if (fieldGenerator.check() && !(insertType.equals(InsertType.HANDLE_RETAIN) || insertType.equals(InsertType.HANDLE_ERROR) || insertType.equals(InsertType.HANDLE_HANDLE))) {
				checkExistFields.append(", ").append(get("sin", fieldName));
			}
		}

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
		switch (insertCheckType) {
			case UNCHECK:
				str = str.replaceAll("\n\t\t#entity_EXIST\\(#errorCode\\),\n", "");
				str = str.replaceAll("LKApiBusInsertService", "LKApiBusInsertWithoutCheckerService");
				str = str.replaceAll("#findExist", "");
				str = str.replaceAll("#forceCheck", "");
				str = str.replaceAll("#existErrorCode", "");
				str = str.replaceAll("#beforeRestore", "");
				str = str.replaceAll("#beforeAddNew", addNewFields.length() == 0 ? "" : beforeAddNew());
				str = str.replaceAll("#beforeSaveMain", saveMainFields.length() == 0 ? "" : beforeSaveMain());
				str = str.replaceAll("#afterSaveMain", afterSaveMain ? afterSaveMain() : "");
				str = str.replaceAll("#clearSubs", "");
				str = str.replaceAll("#addSubs", addSubs(addSubs.toString()));
			break;
			case CHECK_RESTORE:
				containsError = true;
				findExist = true;
				str = str.replaceAll("#findExist", findExist(LKClassUtils.checkImplementsInterface(info.entityClass, I_CompId.class)));
				str = str.replaceAll("#forceCheck", forceCheck(false));
				str = str.replaceAll("#existErrorCode", existErrorCode());
				str = str.replaceAll("#beforeRestore", beforeRestore());
				str = str.replaceAll("#beforeAddNew", addNewFields.length() == 0 ? "" : beforeAddNew());
				str = str.replaceAll("#beforeSaveMain", saveMainFields.length() == 0 ? "" : beforeSaveMain());
				str = str.replaceAll("#afterSaveMain", afterSaveMain ? afterSaveMain() : "");
				str = str.replaceAll("#clearSubs", clearSubs(clearSubs.toString()));
				str = str.replaceAll("#addSubs", addSubs(addSubs.toString()));
			break;
			case FORCE_CHECK:
				containsError = true;
				findExist = true;
				str = str.replaceAll("#findExist", findExist(LKClassUtils.checkImplementsInterface(info.entityClass, I_CompId.class)));
				str = str.replaceAll("#forceCheck", forceCheck(true));
				str = str.replaceAll("#existErrorCode", existErrorCode());
				str = str.replaceAll("#beforeRestore", "");
				str = str.replaceAll("#beforeAddNew", addNewFields.length() == 0 ? "" : beforeAddNew());
				str = str.replaceAll("#beforeSaveMain", saveMainFields.length() == 0 ? "" : beforeSaveMain());
				str = str.replaceAll("#afterSaveMain", afterSaveMain ? afterSaveMain() : "");
				str = str.replaceAll("#clearSubs", "");
				str = str.replaceAll("#addSubs", addSubs(addSubs.toString()));
			break;
		}

		if (!findExist) {
			str = str.replaceAll("import java.util.List;\\n\\n", "");
		}

		if (!containsError) {
			if (!containsHandle) {
				str = str.replaceAll("import com.lichkin.framework.defines.enums.LKCodeEnum;\\n", "");
				str = str.replaceAll("\\nimport lombok.Getter;\\n", "");
				str = str.replaceAll("import lombok.RequiredArgsConstructor;\\n", "");
			}
		}

		return str//
				.replaceAll("#errorCodes", containsError ? errorCodes.toString() : "")//
				.replaceAll("#checkExistFields", checkExistFields.toString())//
				.replaceAll("#restoreFields", restoreFields.toString())//
				.replaceAll("#addNewFields", addNewFields.toString())//
				.replaceAll("#saveMainFields", saveMainFields.toString())//
				.replaceAll("#analysisMethods", analysisMethods.toString())//
				.replaceAll("#LKRuntimeException", callThrow ? "import com.lichkin.framework.defines.exceptions.LKRuntimeException;\n" : "")//
		;
	}


	private static final String I;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.framework.beans.impl.LKRequestBean;").append("\n");
		sb.append("#importEnums");
		sb.append("").append("\n");
		sb.append("import lombok.Getter;").append("\n");
		sb.append("import lombok.Setter;").append("\n");
		sb.append("").append("\n");
		sb.append("@Getter").append("\n");
		sb.append("@Setter").append("\n");
		sb.append("public class I extends LKRequestBean {").append("\n");
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
		sb.append("import com.lichkin.springframework.controllers.LKApiBusInsertController;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusInsertService;").append("\n");
		sb.append("").append("\n");
		sb.append("@RestController(\"#Controller\")").append("\n");
		sb.append("@RequestMapping(value = LKFrameworkStatics.WEB_MAPPING_API_#userType_#clientType + \"#url\")").append("\n");
		sb.append("@LKApiType(apiType = ApiType.COMPANY_BUSINESS)").append("\n");
		sb.append("public class C extends LKApiBusInsertController<I, #entityEntity> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Autowired").append("\n");
		sb.append("	private S service;").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected LKApiBusInsertService<I, #entityEntity> getService(I cin) {").append("\n");
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
		sb.append("#LKRuntimeException");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusInsertService;").append("\n");
		sb.append("").append("\n");
		sb.append("import lombok.Getter;").append("\n");
		sb.append("import lombok.RequiredArgsConstructor;").append("\n");
		sb.append("").append("\n");
		sb.append("@Service(\"#Service\")").append("\n");
		sb.append("public class S extends LKApiBusInsertService<I, #entityEntity> {").append("\n");

		sb.append("").append("\n");
		sb.append("	@Autowired").append("\n");
		sb.append("	private #entityBusService busService;").append("\n");

		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Getter").append("\n");
		sb.append("	@RequiredArgsConstructor").append("\n");
		sb.append("	enum ErrorCodes implements LKCodeEnum {").append("\n");
		sb.append("").append("\n");
		sb.append("		#entity_EXIST(#errorCode),").append("\n");
		sb.append("\n#errorCodes");
		sb.append("		;").append("\n");
		sb.append("").append("\n");
		sb.append("		private final Integer code;").append("\n");
		sb.append("").append("\n");
		sb.append("	}").append("\n");

		sb.append("#findExist");

		sb.append("#forceCheck");

		sb.append("#existErrorCode");

		sb.append("#beforeRestore");

		sb.append("#beforeAddNew");

		sb.append("#beforeSaveMain");

		sb.append("#afterSaveMain");

		sb.append("#clearSubs");

		sb.append("#addSubs");

		sb.append("#analysisMethods");

		sb.append("").append("\n");
		sb.append("}").append("\n");
		S = sb.toString();
	}


	private static String findExist(boolean needCompId) {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected List<#entityEntity> findExist(I sin, String locale, String compId, String loginId) {").append("\n");
		sb.append("		return busService.findExist(null").append(needCompId ? ", compId, sin.getCompId()" : "").append("#checkExistFields);").append("\n");
		sb.append("	}").append("\n");
		return sb.toString();
	}


	private static String forceCheck(boolean forceCheck) {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected boolean forceCheck(I sin, String locale, String compId, String loginId) {").append("\n");
		sb.append("		return ").append(forceCheck).append(";").append("\n");
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


	private static String beforeRestore() {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected void beforeRestore(I sin, String locale, String compId, String loginId, #entityEntity entity, #entityEntity exist) {").append("\n");
		sb.append("#restoreFields");
		sb.append("	}").append("\n");
		return sb.toString();
	}


	private static String beforeAddNew() {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected void beforeAddNew(I sin, String locale, String compId, String loginId, #entityEntity entity) {").append("\n");
		sb.append("#addNewFields");
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
