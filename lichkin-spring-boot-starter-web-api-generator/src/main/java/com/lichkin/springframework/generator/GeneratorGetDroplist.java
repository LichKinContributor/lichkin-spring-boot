package com.lichkin.springframework.generator;

import java.io.File;
import java.io.FileOutputStream;

import com.lichkin.framework.defines.entities.I_CompId;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.springframework.generator.LKApiGenerator.GenerateInfo;

class GeneratorGetDroplist extends GeneratorCommon {

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
						.replaceAll("#Rs", getFieldsR(info.entity, "id", "compId")))//
								.replaceAll("#importUsingStatus", implemetsUsingStatus ? "import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;\n" : "")//
								.replaceAll("#compId", LKClassUtils.checkImplementsInterface(info.entityClass, I_CompId.class) ? "" : "//")//
								.replaceAll("#usingStatus", implemetsUsingStatus ? "" : "//")//
								.getBytes()

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
		sb.append("@RequestMapping(value = LKFrameworkStatics.WEB_MAPPING_API_#userType_#clientType + \"#url\")").append("\n");
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
		sb.append("import com.lichkin.framework.db.beans.Order;").append("\n");
		sb.append("import com.lichkin.framework.db.beans.QuerySQL;").append("\n");
		sb.append("import com.lichkin.framework.db.beans.#entityR;").append("\n");
		sb.append("import com.lichkin.framework.defines.beans.impl.LKDroplistBean;").append("\n");
		sb.append("#importUsingStatus");
		sb.append("import com.lichkin.framework.defines.exceptions.LKException;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusGetDroplistService;").append("\n");
		sb.append("").append("\n");
		sb.append("@Service(\"#Service\")").append("\n");
		sb.append("public class S extends LKApiBusGetDroplistService<I> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	public List<LKDroplistBean> handle(I sin, String locale, String compId, String loginId) throws LKException {").append("\n");
		sb.append("		QuerySQL sql = new QuerySQL(#entityEntity.class);").append("\n");
		sb.append("").append("\n");
		sb.append("		// 查询结果").append("\n");
		sb.append("		sql.select(#entityR.id, \"value\");").append("\n");
		sb.append("//		sql.select(#entityR.xxx, \"text\");").append("\n");
		sb.append("").append("\n");
		sb.append("		// 筛选条件（必填项）").append("\n");
		sb.append("#compId		sql.eq(#entityR.compId, compId);").append("\n");
		sb.append("#usingStatus		sql.eq(#entityR.usingStatus, LKUsingStatusEnum.USING);").append("\n");
		sb.append("\n");
		sb.append("		// 筛选条件（业务项）").append("\n");
		sb.append("\n");
		sb.append("		// 排序条件").append("\n");
		sb.append("		sql.addOrders(new Order(#entityR.id, false));").append("\n");
		sb.append("").append("\n");
		sb.append("		// 返回结果").append("\n");
		sb.append("		return dao.getList(sql, LKDroplistBean.class);").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		S = sb.toString();
	}

}
