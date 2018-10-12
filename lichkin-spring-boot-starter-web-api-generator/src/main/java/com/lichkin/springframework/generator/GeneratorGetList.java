package com.lichkin.springframework.generator;

import java.io.File;
import java.io.FileOutputStream;

import com.lichkin.springframework.generator.LKApiGenerator.Type;

class GeneratorGetList extends GeneratorCommon {

	@SuppressWarnings("resource")
	static void generate(String dir, String packageName, String entity, int index, Type type) throws Exception {
		new FileOutputStream(new File(dir + "/I.java")).write(

				commonReplace(index, type, packageName, entity, I.replaceAll("#fields", getFields(true, false, false, entity, "id", "compId", "insertTime"))).getBytes()

		);

		new FileOutputStream(new File(dir + "/O.java")).write(

				commonReplace(index, type, packageName, entity, O.replaceAll("#fields", getFields(true, true, true, entity, "compId"))).getBytes()

		);

		new FileOutputStream(new File(dir + "/C.java")).write(

				commonReplace(index, type, packageName, entity, C).getBytes()

		);

		new FileOutputStream(new File(dir + "/S.java")).write(

				commonReplace(index, type, packageName, entity, S.replaceAll("#Rs", getFieldsR(entity, "compId"))).getBytes()

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
		sb.append("import com.lichkin.springframework.controllers.LKApiBusGetListController;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusGetListService;").append("\n");
		sb.append("").append("\n");
		sb.append("@RestController(\"#Controller\")").append("\n");
		sb.append("@RequestMapping(value = LKFrameworkStatics.WEB_MAPPING_API_WEB_ADMIN + \"#url\")").append("\n");
		sb.append("@LKApiType(apiType = ApiType.COMPANY_BUSINESS)").append("\n");
		sb.append("public class C extends LKApiBusGetListController<I, O, #entityEntity> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Autowired").append("\n");
		sb.append("	private S service;").append("\n");
		sb.append("").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected LKApiBusGetListService<I, O, #entityEntity> getService(I cin) {").append("\n");
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
		sb.append("import com.lichkin.framework.db.beans.Order;").append("\n");
		sb.append("import com.lichkin.framework.db.beans.QuerySQL;").append("\n");
		sb.append("import com.lichkin.framework.db.beans.#entityR;").append("\n");
		sb.append("import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;").append("\n");
		sb.append("import com.lichkin.springframework.entities.impl.#entityEntity;").append("\n");
		sb.append("import com.lichkin.springframework.services.LKApiBusGetListService;").append("\n");
		sb.append("").append("\n");
		sb.append("@Service(\"#Service\")").append("\n");
		sb.append("public class S extends LKApiBusGetListService<I, O, #entityEntity> {").append("\n");
		sb.append("").append("\n");
		sb.append("	@Override").append("\n");
		sb.append("	protected void initSQL(I sin, QuerySQL sql) {").append("\n");
		sb.append("		String compId = sin.getCompId();").append("\n");
		sb.append("\n");
		sb.append("		// 主表").append("\n");
		sb.append("#Rs");
		sb.append("\n");
		sb.append("		// 关联表").append("\n");
		sb.append("//		sql.innerJoin(BusXEntity.class, new Condition(BusXR.y, #entityR.z));").append("\n");
		sb.append("//		sql.select(BusXR.w);").append("\n");
		sb.append("\n");
		sb.append("		// 字典表").append("\n");
		sb.append("//		int i = 0;").append("\n");
		sb.append("//		LKDictUtils.x(sql, #entityR.y, i++);").append("\n");
		sb.append("\n");
		sb.append("		// 筛选条件（必填项）").append("\n");
		sb.append("		sql.eq(#entityR.compId, compId);").append("\n");
		sb.append("		sql.eq(#entityR.usingStatus, LKUsingStatusEnum.USING);").append("\n");
		sb.append("\n");
		sb.append("		// 筛选条件（业务项）").append("\n");
		sb.append("\n");
		sb.append("		// 排序条件").append("\n");
		sb.append("		sql.addOrders(new Order(#entityR.id, false));").append("\n");
		sb.append("	}").append("\n");
		sb.append("").append("\n");
		sb.append("}").append("\n");
		S = sb.toString();
	}

}
