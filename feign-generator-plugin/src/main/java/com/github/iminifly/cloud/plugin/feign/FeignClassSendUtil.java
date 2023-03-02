package com.github.iminifly.cloud.plugin.feign;

import com.alibaba.fastjson.JSON;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignClassAndModel;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignClientClass;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignModel;
import com.github.iminifly.cloud.plugin.feign.core.util.ZipUtil;
import com.github.iminifly.cloud.plugin.feign.core.util.ZipUtil.LocalFileOutputStreamHandler;
import com.github.iminifly.cloud.plugin.feign.core.util.ZipUtil.Text;

import freemarker.template.TemplateException;

import static com.github.iminifly.cloud.plugin.feign.core.FeignClassAndModelResolver.resolveFeignClientClassList;
import static com.github.iminifly.cloud.plugin.feign.core.FeignClassAndModelResolver.resolveFeignModelList;
import static com.github.iminifly.cloud.plugin.feign.core.util.HttpUtil.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FeignClassSendUtil
 *
 * @author XGF
 * @date 2020/11/17 16:10
 */
class FeignClassSendUtil {

	static void serializeAndSend(FeignClassAndModel feignClassAndModel) throws IOException {

		FeignAutoGenerator.log.info("Start send feign class and model metadata");

		String jsonString = JSON.toJSONString(feignClassAndModel);

		Map<String, String> headers = new HashMap<>(1);

		String response = request(FeignAutoGenerator.manageServer, jsonString, headers);

		FeignAutoGenerator.log.info("Send feign client metadata response: " + response);
	}

	static void localSave(FeignClassAndModel feignClassAndModel) throws IOException, TemplateException {

		FeignAutoGenerator.log.info("Start save feign class and model metadata to project/target");

		List<Text> pathAndFileContents = new ArrayList<>();

		List<FeignClientClass> feignClientClasses = feignClassAndModel.getFeignClientClasses();

		List<FeignModel> feignModels = feignClassAndModel.getFeignModels();

		pathAndFileContents.addAll(resolveFeignClientClassList(feignClientClasses));
		pathAndFileContents.addAll(resolveFeignModelList(feignModels));

		ZipUtil.zipTexts(pathAndFileContents,
				new LocalFileOutputStreamHandler(FeignAutoGenerator.mavenProject.getBasedir() + "/target/"
						+ FeignAutoGenerator.mavenProject.getArtifactId() + "-feign-src.zip"));

	}
}
