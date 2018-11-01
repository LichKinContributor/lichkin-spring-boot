package com.lichkin.springframework.generator;

import java.io.File;
import java.io.FileOutputStream;

import com.lichkin.springframework.generator.LKApiGenerator.GenerateInfo;

class GeneratorSpecial extends GeneratorCommon {

	@SuppressWarnings("resource")
	static void generate(GenerateInfo info) throws Exception {
		new FileOutputStream(new File(info.dir + "/CI.java")).write(

				commonReplace(info, CI).getBytes("UTF-8")

		);

		new FileOutputStream(new File(info.dir + "/CO.java")).write(

				commonReplace(info, CO).getBytes("UTF-8")

		);

		new FileOutputStream(new File(info.dir + "/C.java")).write(

				commonReplace(info, C).getBytes("UTF-8")

		);

		new FileOutputStream(new File(info.dir + "/SI.java")).write(

				commonReplace(info, SI).getBytes("UTF-8")

		);

		new FileOutputStream(new File(info.dir + "/SO.java")).write(

				commonReplace(info, SO).getBytes("UTF-8")

		);

		new FileOutputStream(new File(info.dir + "/S.java")).write(

				commonReplace(info, S).getBytes("UTF-8")

		);
	}


	protected static final String CI;
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
		sb.append("public class CI extends LKRequestBean {").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		CI = sb.toString();
	}

	protected static final String CO;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import lombok.Getter;").append("\n");
		sb.append("import lombok.Setter;").append("\n");
		sb.append("").append("\n");
		sb.append("@Getter").append("\n");
		sb.append("@Setter").append("\n");
		sb.append("public class CO {").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		CO = sb.toString();
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
		sb.append("import com.lichkin.framework.defines.exceptions.LKException;").append("\n");
		sb.append("import com.lichkin.framework.web.annotations.LKApiType;").append("\n");
		sb.append("import com.lichkin.framework.web.enums.ApiType;").append("\n");
		sb.append("import com.lichkin.springframework.controllers.LKApiYYController;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiService;").append("\n");
		sb.append("").append("\n");
		sb.append("@RestController(\"#Controller\")").append("\n");
		sb.append("@RequestMapping(value = LKFrameworkStatics.WEB_MAPPING_API_#userType_#clientType + \"#url\")").append("\n");
		sb.append("@LKApiType(apiType = ApiType.COMPANY_BUSINESS)").append("\n");
		sb.append("public class C extends LKApiYYController<CI, CO, SI, SO> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Autowired").append("\n");
		sb.append("	private S service;").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected LKApiService<SI, SO> getService(CI cin) {").append("\n");
		sb.append("		return service;").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected SI beforeInvokeService(CI cin) throws LKException {").append("\n");
		sb.append("		return null;").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected CO afterInvokeService(CI cin, SI sin, SO sout) throws LKException {").append("\n");
		sb.append("		return null;").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		C = sb.toString();
	}

	private static final String SI;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import lombok.Getter;").append("\n");
		sb.append("import lombok.Setter;").append("\n");
		sb.append("").append("\n");
		sb.append("@Getter").append("\n");
		sb.append("@Setter").append("\n");
		sb.append("public class SI {").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		SI = sb.toString();
	}

	private static final String SO;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import lombok.Getter;").append("\n");
		sb.append("import lombok.Setter;").append("\n");
		sb.append("").append("\n");
		sb.append("@Getter").append("\n");
		sb.append("@Setter").append("\n");
		sb.append("public class SO {").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		SO = sb.toString();
	}

	private static final String S;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("package com.lichkin.application.#packageName;").append("\n");
		sb.append("").append("\n");
		sb.append("import org.springframework.stereotype.Service;").append("\n");
		sb.append("import org.springframework.transaction.annotation.Transactional;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.framework.defines.exceptions.LKException;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiService;").append("\n");
		sb.append("").append("\n");
		sb.append("@Service(\"#Service\")").append("\n");
		sb.append("public class S implements LKApiService<SI, SO> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	@Transactional").append("\n");
		sb.append("	public SO handle(SI sin, String locale, String compId, String loginId) throws LKException {").append("\n");
		sb.append("		return null;").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		S = sb.toString();
	}

}
