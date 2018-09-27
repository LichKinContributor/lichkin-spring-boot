package com.lichkin.springframework.generator;

import java.io.File;
import java.io.FileOutputStream;

import com.lichkin.springframework.generator.LKApiGenerator.Type;

class GeneratorGetDroplist extends GeneratorCommon {

	@SuppressWarnings("resource")
	static void generate(String dir, String packageName, String entity, int index, Type type) throws Exception {
		new FileOutputStream(new File(dir + "/I.java")).write(

				commonReplace(index, type, packageName, entity, I).getBytes()

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
		sb.append("import com.lichkin.springframework.controllers.LKApiBusGetDroplistController;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusGetDroplistService;").append("\n");
		sb.append("").append("\n");
		sb.append("@RestController(\"#Controller\")").append("\n");
		sb.append("@RequestMapping(value = LKFrameworkStatics.WEB_MAPPING_API_WEB_ADMIN + \"#url\")").append("\n");
		sb.append("@LKApiType(apiType = ApiType.COMPANY_BUSINESS)").append("\n");
		sb.append("public class C extends LKApiBusGetDroplistController<I> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Autowired").append("\n");
		sb.append("	private S service;").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected LKApiBusGetDroplistService<I> getService(I cin) {").append("\n");
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
		sb.append("import java.util.List;").append("\n");
		sb.append("").append("\n");
		sb.append("import org.springframework.stereotype.Service;").append("\n");
		sb.append("").append("\n");
		sb.append("import com.lichkin.framework.defines.beans.impl.LKDroplistBean;").append("\n");
		sb.append("import com.lichkin.framework.defines.exceptions.LKException;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusGetDroplistService;").append("\n");
		sb.append("").append("\n");
		sb.append("@Service(\"#Service\")").append("\n");
		sb.append("public class S extends LKApiBusGetDroplistService<I> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	public List<LKDroplistBean> handle(I sin) throws LKException {").append("\n");
		sb.append("		return null;").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		S = sb.toString();
	}

}
