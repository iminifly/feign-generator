package com.github.iminifly.cloud.plugin.feign.core;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import com.github.iminifly.cloud.plugin.feign.core.model.FeignClientClass;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignModel;

/**
 * TemplateProcessor
 *
 * @author XGF
 * @date 2020/11/13 12:00
 */
public class TemplateProcessor {

	private static Configuration freeMarkerConfig = new Configuration(new Version("2.3.0"));

	private static Template feignClientTemplate;

	private static Template feignFallbackTemplate;

	private static Template modelTemplate;

	static {
		try {
			initTemplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initTemplete() throws IOException {

		StringTemplateLoader loader = new StringTemplateLoader();

		String templateStr = getTemplate("feignClient.ftl");
		loader.putTemplate("feignClient", templateStr);
		freeMarkerConfig.setTemplateLoader(loader);
		feignClientTemplate = freeMarkerConfig.getTemplate("feignClient");

		String fallBackTemplateStr = getTemplate("feignFallBack.ftl");
		loader.putTemplate("feignFallBack", fallBackTemplateStr);
		freeMarkerConfig.setTemplateLoader(loader);
		feignFallbackTemplate = freeMarkerConfig.getTemplate("feignFallBack");

		String modelTemplateStr = getTemplate("feignModel.ftl");
		loader.putTemplate("feignModel", modelTemplateStr);
		freeMarkerConfig.setTemplateLoader(loader);
		modelTemplate = freeMarkerConfig.getTemplate("feignModel");
	}

	public static String parseFeignClient(FeignClientClass feignClientClass) throws IOException, TemplateException {
		StringWriter sw = new StringWriter();
		feignClientTemplate.process(feignClientClass, sw);
		return sw.toString();
	}

	public static String parseFeignClientFallBack(FeignClientClass feignClientClass)
			throws IOException, TemplateException {
		StringWriter sw = new StringWriter();
		feignFallbackTemplate.process(feignClientClass, sw);
		return sw.toString();
	}

	public static String parseFeignModel(FeignModel feignModel) throws IOException, TemplateException {
		StringWriter sw = new StringWriter();
		modelTemplate.process(feignModel, sw);
		return sw.toString();
	}

	private static String getTemplate(String path) {

		StringBuilder builder = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(TemplateProcessor.class.getClassLoader().getResourceAsStream(path), "UTF-8"))) {

			String line = reader.readLine();

			while (line != null) {
				builder.append(line).append("\n");
				line = reader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return builder.toString();
	}
}
