package com.lichkin.springframework.generator;

import java.io.File;
import java.io.FileOutputStream;

import com.lichkin.framework.defines.annotations.ClassGenerator;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.framework.utils.LKFieldUtils;
import com.lichkin.framework.utils.LKStringUtils;

import lombok.RequiredArgsConstructor;

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

	@RequiredArgsConstructor
	static class GenerateInfo {

		final String apiType;

		final String userType;

		final String dir;

		final String packageName;

		final String entity;

		final int index;

		final int errorCode;

		final Type type;

		final Class<?> entityClass;

		final ClassGenerator classGenerator;

	}


	@SuppressWarnings("resource")
	public static void generate(String apiType, String userType, String projectDir, String entity, int index, int errorCode, Type type, String descContent) {
		try {
			entity = entity.replaceAll("Entity", "");
			Class<?> entityClass = Class.forName("com.lichkin.springframework.entities.impl." + entity + "Entity");
			if (type.equals(Type.UpdateUsingStatus) && !LKClassUtils.checkImplementsInterface(entityClass, I_UsingStatus.class)) {
				throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR);
			}
			String packageName = "apis.api" + LKStringUtils.fillZero(LKFieldUtils.getSerialVersionUID(entityClass).intValue(), 5) + "." + type.getName() + ".n" + LKStringUtils.fillZero(index, 2);
			File packageDir = new File(projectDir + "/src/main/java/com/lichkin/application/" + packageName.replaceAll("\\.", "/"));
			packageDir.mkdirs();
			String dir = packageDir.getAbsolutePath();
			new FileOutputStream(new File(dir + "/description")).write((entity + "\n" + descContent).getBytes());
			type.getGenerator().getDeclaredMethod("generate", GenerateInfo.class).invoke(null, new GenerateInfo(apiType, userType, dir, packageName, entity, index, errorCode, type, entityClass, entityClass.getAnnotation(ClassGenerator.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
