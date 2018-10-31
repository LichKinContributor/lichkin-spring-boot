package com.lichkin.springframework.generator;

import java.io.File;
import java.io.FileOutputStream;

import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.springframework.generator.LKApiGenerator.GenerateInfo;

class GeneratorDelete extends GeneratorCommon {

	@SuppressWarnings("resource")
	static void generate(GenerateInfo info) throws Exception {
		new FileOutputStream(new File(info.dir + "/I.java")).write(

				commonReplace(info, I).getBytes()

		);

		new FileOutputStream(new File(info.dir + "/C.java")).write(

				commonReplace(info, C).getBytes()

		);

		boolean implemetsUsingStatus = LKClassUtils.checkImplementsInterface(info.entityClass, I_UsingStatus.class);
		new FileOutputStream(new File(info.dir + "/S.java")).write(

				commonReplace(info, S//
						.replaceAll("#importUsingStatus", implemetsUsingStatus ? "import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;\n" : "")//
						.replaceAll("#realDelete", implemetsUsingStatus ? realDelete : "")//
						.replaceAll("#beforeRealDelete", beforeRealDelete)//
						.replaceAll("#beforeLogicDelete", implemetsUsingStatus ? beforeLogicDelete : ""))//
								.getBytes()

		);
	}


	protected static final String I;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.framework.beans.impl.LKRequestIDsBean;").append("\n");
		sb.append("").append("\n");
		sb.append("import lombok.Getter;").append("\n");
		sb.append("import lombok.Setter;").append("\n");
		sb.append("").append("\n");
		sb.append("@Getter").append("\n");
		sb.append("@Setter").append("\n");
		sb.append("public class I extends LKRequestIDsBean {").append("\n");
		sb.append("").append("\n");
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
		sb.append("import com.lichkin.framework.defines.LKFrameworkStatics;").append("\n");
		sb.append("import com.lichkin.framework.web.annotations.LKApiType;").append("\n");
		sb.append("import com.lichkin.framework.web.enums.ApiType;").append("\n");
		sb.append("import com.lichkin.springframework.controllers.LKApiBusDeleteController;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusDeleteService;").append("\n");
		sb.append("").append("\n");
		sb.append("@RestController(\"#Controller\")").append("\n");
		sb.append("@RequestMapping(value = LKFrameworkStatics.WEB_MAPPING_API_#userType_#clientType + \"#url\")").append("\n");
		sb.append("@LKApiType(apiType = ApiType.COMPANY_BUSINESS)").append("\n");
		sb.append("public class C extends LKApiBusDeleteController<I, #entityEntity> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Autowired").append("\n");
		sb.append("	private S service;").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected LKApiBusDeleteService<I, #entityEntity> getService(I cin) {").append("\n");
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
		sb.append("import org.springframework.stereotype.Service;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.framework.db.beans.#entityR;").append("\n");
		sb.append("import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;").append("\n");
		sb.append("#importUsingStatus");
		sb.append("import com.lichkin.framework.defines.exceptions.LKRuntimeException;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusDeleteService;").append("\n");
		sb.append("").append("\n");
		sb.append("@Service(\"#Service\")").append("\n");
		sb.append("public class S extends LKApiBusDeleteService<I, #entityEntity> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected int getIdColumnResId() {").append("\n");
		sb.append("		return #entityR.id;").append("\n");
		sb.append("	}").append("\n");
		sb.append("#realDelete");
		sb.append("#beforeRealDelete");
		sb.append("#beforeLogicDelete");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		S = sb.toString();
	}

	protected static final String realDelete;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected boolean realDelete(I sin, String locale, String compId, String loginId) {").append("\n");
		sb.append("		return true;").append("\n");
		sb.append("	}").append("\n");
		realDelete = sb.toString();
	}

	protected static final String beforeRealDelete;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected void beforeRealDelete(I sin, String locale, String compId, String loginId, #entityEntity entity, String id) {").append("\n");
		sb.append("		// TODO 删除数据为高风险操作，通常需要做一些业务校验才可以执行删除。").append("\n");
		sb.append("		throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR);").append("\n");
		sb.append("	}").append("\n");
		beforeRealDelete = sb.toString();
	}

	protected static final String beforeLogicDelete;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected void beforeLogicDelete(I sin, String locale, String compId, String loginId, #entityEntity entity, String id) {").append("\n");
		sb.append("		// TODO 删除数据为高风险操作，通常需要做一些业务校验才可以执行删除。").append("\n");
		sb.append("		LKUsingStatusEnum usingStatus = entity.getUsingStatus();").append("\n");
		sb.append("		switch (usingStatus) {").append("\n");
		sb.append("			case STAND_BY:// 待用").append("\n");
		sb.append("			// TODO").append("\n");
		sb.append("			break;").append("\n");
		sb.append("			default:").append("\n");
		sb.append("				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR);").append("\n");
		sb.append("		}").append("\n");
		sb.append("	}").append("\n");
		beforeLogicDelete = sb.toString();
	}

}
