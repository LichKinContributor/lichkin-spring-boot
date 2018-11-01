package com.lichkin.springframework.generator;

import com.lichkin.springframework.generator.LKApiGenerator.GenerateInfo;

class GeneratorGetList extends GeneratorCommon {

	static void generate(GenerateInfo info) throws Exception {
		GeneratorGetPage.generate(info, true);
	}

}
