package com.lichkin.springframework.generator;

import java.io.File;
import java.io.FileOutputStream;

import com.lichkin.springframework.generator.LKApiGenerator.Type;

class GeneratorInsert extends GeneratorCommon {

	@SuppressWarnings("resource")
	static void generate(String dir, String packageName, String entity, int index, Type type) throws Exception {
		new FileOutputStream(new File(dir + "/I.java")).write(

				commonReplace(index, type, packageName, entity, I.replaceAll("#fields", getFields(true, false, false, entity, "id", "compId", "usingStatus", "insertTime"))).getBytes()

		);

		new FileOutputStream(new File(dir + "/C.java")).write(

				commonReplace(index, type, packageName, entity, C).getBytes()

		);

		new FileOutputStream(new File(dir + "/S.java")).write(

				commonReplace(index, type, packageName, entity, S).getBytes()

		);
	}


	protected static final String I;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.framework.beans.impl.LKRequestBean;").append("\n");
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

	protected static final String C;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired;").append("\n");
		sb.append("import org.springframework.web.bind.annotation.RequestMapping;").append("\n");
		sb.append("import org.springframework.web.bind.annotation.RestController;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.application.services.impl.SysAdminOperLogService;").append("\n");
		sb.append("import com.lichkin.framework.defines.LKFrameworkStatics;").append("\n");
		sb.append("import com.lichkin.framework.web.annotations.LKApiType;").append("\n");
		sb.append("import com.lichkin.framework.web.enums.ApiType;").append("\n");
		sb.append("import com.lichkin.springframework.controllers.LKApiBusInsertController;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusInsertService;").append("\n");
		sb.append("import com.lichkin.springframework.services.OperLogService;").append("\n");
		sb.append("").append("\n");
		sb.append("@RestController(\"#Controller\")").append("\n");
		sb.append("@RequestMapping(value = LKFrameworkStatics.WEB_MAPPING_API_WEB_ADMIN + \"#url\")").append("\n");
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
		sb.append("	@Autowired").append("\n");
		sb.append("	private SysAdminOperLogService logService;").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	public OperLogService getLogService() {").append("\n");
		sb.append("		return logService;").append("\n");
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
		sb.append("import org.springframework.stereotype.Service;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.framework.defines.enums.LKCodeEnum;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusInsertService;").append("\n");
		sb.append("").append("\n");
		sb.append("import lombok.Getter;").append("\n");
		sb.append("import lombok.RequiredArgsConstructor;").append("\n");
		sb.append("").append("\n");
		sb.append("@Service(\"#Service\")").append("\n");
		sb.append("public class S extends LKApiBusInsertService<I, #entityEntity> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Getter").append("\n");
		sb.append("	@RequiredArgsConstructor").append("\n");
		sb.append("	enum ErrorCodes implements LKCodeEnum {").append("\n");
		sb.append("").append("\n");
		sb.append("		#entity_EXIST(100000),").append("\n");
		sb.append("").append("\n");
		sb.append("		;").append("\n");
		sb.append("").append("\n");
		sb.append("		private final Integer code;").append("\n");
		sb.append("").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	public boolean needCheckExist(I sin) {").append("\n");
		sb.append("		return false;").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	public List<#entityEntity> findExist(I sin) {").append("\n");
		sb.append("		return null;").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	public LKCodeEnum existErrorCode(I sin) {").append("\n");
		sb.append("		return ErrorCodes.#entity_EXIST;").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		S = sb.toString();
	}

}
