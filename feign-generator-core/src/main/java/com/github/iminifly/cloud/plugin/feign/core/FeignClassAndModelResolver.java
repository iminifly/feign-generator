package com.github.iminifly.cloud.plugin.feign.core;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.iminifly.cloud.plugin.feign.core.model.FeignClientClass;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignModel;
import com.github.iminifly.cloud.plugin.feign.core.util.ZipUtil.Text;

/**
 * FeignClassAndModelResolver
 *
 * @author XGF
 * @date 2020/11/20 14:06
 */
public class FeignClassAndModelResolver {

	public static List<Text> resolveFeignClientClassList(List<FeignClientClass> feignClientClasses)
			throws IOException, TemplateException {

		List<Text> pathAndFileContents = new ArrayList<>();

		for (FeignClientClass feignClientClass : feignClientClasses) {

			// 生成feign接口
			String feignClient = TemplateProcessor.parseFeignClient(feignClientClass);

			Text pathAndFileContent = new Text();

			pathAndFileContent.setText(feignClient);
			pathAndFileContent.setFileName(feignClientClass.getFeignClassName() + ".java");

			pathAndFileContents.add(pathAndFileContent);

			// 生成fallback
			String feignClientFallBack = TemplateProcessor.parseFeignClientFallBack(feignClientClass);

			Text pathAndFileContent2 = new Text();

			pathAndFileContent2.setText(feignClientFallBack);
			pathAndFileContent2
					.setFileName("fallback/" + feignClientClass.getFeignClassName() + "FallBackFactory.java");

			pathAndFileContents.add(pathAndFileContent2);
		}

		return pathAndFileContents;
	}

	public static List<Text> resolveFeignModelList(List<FeignModel> feignModels) throws IOException, TemplateException {

		List<Text> pathAndFileContents = new ArrayList<>();

		for (FeignModel feignModel : feignModels) {

			String model = TemplateProcessor.parseFeignModel(feignModel);

			Text pathAndFileContent = new Text();

			pathAndFileContent.setText(model);

			pathAndFileContent.setFileName(
					feignModel.getPackageName().replace('.', '/') + "/" + feignModel.getModelName() + ".java");

			pathAndFileContents.add(pathAndFileContent);
		}

		return pathAndFileContents;
	}
}
