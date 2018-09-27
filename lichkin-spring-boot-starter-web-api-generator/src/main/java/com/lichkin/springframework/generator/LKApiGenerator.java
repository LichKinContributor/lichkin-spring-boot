package com.lichkin.springframework.generator;

import java.io.File;
import java.io.FileOutputStream;

import com.lichkin.framework.defines.entities.I_Base;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.framework.utils.LKFieldUtils;
import com.lichkin.framework.utils.LKStringUtils;

public class LKApiGenerator {

	public static enum Type {

		GetPage("P", GeneratorGetPage.class),

		GetList("L", GeneratorGetList.class),

		GetOne("O", GeneratorGetOne.class),

		GetDroplist("LD", GeneratorGetDroplist.class),

		Insert("I", GeneratorInsert.class),

		Update("U", GeneratorUpdate.class),

		UpdateUsingStatus("US", GeneratorUpdateUsingStatus.class),

		Delete("D", GeneratorDelete.class),

		Special("S", GeneratorSpecial.class);

		Type(String name, Class<?> generator) {
			this.name = name;
			this.generator = generator;
		}


		private String name;

		private Class<?> generator;


		public String getName() {
			return name;
		}


		public Class<?> getGenerator() {
			return generator;
		}

	};


	@SuppressWarnings("resource")
	public static void generate(String projectDir, String entity, int index, Type type, String descContent) {
		try {
			Class<?> entityClass = Class.forName("com.lichkin.springframework.entities.impl." + entity + "Entity");
			switch (type) {
				case GetPage:
					if (!LKClassUtils.checkImplementsInterface(entityClass, I_ID.class)) {
						throw new LKFrameworkException("entity must implements I_ID when using LKApiBusGetPageService.");
					}
				break;
				case GetList:
					if (!LKClassUtils.checkImplementsInterface(entityClass, I_ID.class)) {
						throw new LKFrameworkException("entity must implements I_ID when using LKApiBusGetListService.");
					}
				break;
				case GetOne:
					if (!LKClassUtils.checkImplementsInterface(entityClass, I_ID.class)) {
						throw new LKFrameworkException("entity must implements I_ID when using LKApiBusGetOneService.");
					}
				break;
				case GetDroplist:
				break;
				case Insert:
					if (!LKClassUtils.checkImplementsInterface(entityClass, I_Base.class)) {
						throw new LKFrameworkException("entity must implements I_Base when using LKApiBusInsertService.");
					}
				break;
				case Update:
					if (!LKClassUtils.checkImplementsInterface(entityClass, I_Base.class)) {
						throw new LKFrameworkException("entity must implements I_Base when using LKApiBusUpdateService.");
					}
				break;
				case UpdateUsingStatus:
					if (!LKClassUtils.checkImplementsInterface(entityClass, I_UsingStatus.class)) {
						throw new LKFrameworkException("entity must implements I_UsingStatus when using LKApiBusUpdateUsingStatusService.");
					}
				break;
				case Delete:
					if (!LKClassUtils.checkImplementsInterface(entityClass, I_UsingStatus.class)) {
						throw new LKFrameworkException("entity must implements I_UsingStatus when using LKApiBusDeleteService.");
					}
				break;
				case Special:
				break;
				default:
				break;
			}

			String packageName = "apis.api" + LKStringUtils.fillZero(LKFieldUtils.getSerialVersionUID(entityClass).intValue(), 5) + "." + type.getName() + ".n" + LKStringUtils.fillZero(index, 2);
			File packageDir = new File(projectDir + "/src/main/java/com/lichkin/application/" + packageName.replaceAll("\\.", "/"));
			packageDir.mkdirs();
			String dir = packageDir.getAbsolutePath();
			new FileOutputStream(new File(dir + "/description")).write((entity + "\n" + descContent).getBytes());
			type.getGenerator().getDeclaredMethod("generate", String.class, String.class, String.class, int.class, Type.class).invoke(null, dir, packageName, entity, index, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void generate(String projectDir, String entity) {
		generate(projectDir, entity, 0);
	}


	private static void generate(String projectDir, String entity, int index) {
		LKApiGenerator.generate(projectDir, entity, index, Type.GetPage, "获取分页数据接口");
		LKApiGenerator.generate(projectDir, entity, index, Type.GetList, "获取列表数据接口");
		LKApiGenerator.generate(projectDir, entity, index, Type.GetOne, "获取单个数据接口");
		LKApiGenerator.generate(projectDir, entity, index, Type.GetDroplist, "获取下拉列表数据接口");
		LKApiGenerator.generate(projectDir, entity, index, Type.Insert, "新增数据接口");
		LKApiGenerator.generate(projectDir, entity, index, Type.Update, "编辑数据接口");
		LKApiGenerator.generate(projectDir, entity, index, Type.UpdateUsingStatus, "修改状态接口");
		LKApiGenerator.generate(projectDir, entity, index, Type.Delete, "删除数据接口");
		LKApiGenerator.generate(projectDir, entity, index, Type.Special, "特殊业务接口");
	}

}
