package com.github.iminifly.cloud.plugin.feign;

import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import com.github.iminifly.cloud.plugin.feign.core.model.FeignClassAndModel;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignClientClass;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignModel;
import com.github.iminifly.cloud.plugin.feign.core.util.DynamicClasspath;

/**
 * FeignAutoGenerator
 *
 * @author XGF
 * @date 2020/11/17 8:22
 */
@Mojo(name = "generate", requiresDependencyResolution = ResolutionScope.COMPILE)
public class FeignAutoGenerator extends AbstractMojo {

	@org.apache.maven.plugins.annotations.Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	@org.apache.maven.plugins.annotations.Parameter(defaultValue = "${generate.scanPackage}", readonly = true)
	private String scanPackage;

	@org.apache.maven.plugins.annotations.Parameter(defaultValue = "${generate.modelScanPackage}", readonly = true)
	private String modelScanPackage;

	@org.apache.maven.plugins.annotations.Parameter(defaultValue = "${generate.manageServerUrl}", readonly = true, required = true)
	private String manageServerUrl;

	@org.apache.maven.plugins.annotations.Parameter(defaultValue = "${generate.local}", readonly = true)
	private String local;

	private static final String DEFAULT_SCAN_PACKAGE = "org.net5ijy.cloud";
	private static final String FEIGN_GENERATOR_PLUGIN = "feign-generator-plugin";
	private static final String JAR = ".jar";

	static Log log;
	static String modelScanPath;
	static String manageServer;
	static MavenProject mavenProject;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		if (scanPackage == null || "".equals(scanPackage)) {
			scanPackage = DEFAULT_SCAN_PACKAGE;
		}
		if (modelScanPackage == null || "".equals(modelScanPackage)) {
			modelScanPackage = DEFAULT_SCAN_PACKAGE;
		}
		if (local == null || "".equals(local)) {
			local = "false";
		}

		log = getLog();
		modelScanPath = modelScanPackage;
		manageServer = manageServerUrl;
		mavenProject = project;

		try {

			List<String> classpathElements = project.getCompileClasspathElements();

			String classes = null;

			DynamicClasspath dynamicClasspath = DynamicClasspath.getInstance();

			for (String classpathElement : classpathElements) {
				try {
					if (classpathElement.contains(FEIGN_GENERATOR_PLUGIN)) {
						continue;
					}
					if (classpathElement.endsWith(JAR)) {
						dynamicClasspath.loadJar(classpathElement);
					} else {
						classes = classpathElement;
					}
				} catch (Exception e) {
					getLog().error(e);
				}
			}

			// 加载classes
			if (classes != null) {
				dynamicClasspath.loadDir(classes);
			}

			// 扫描所有controller
			FeignClassAndModel feignClassAndModel = FeignClassGenerateUtil.getFeignClassAndModel(scanPackage,
					dynamicClasspath);

			getLog().info("Feign client list:");

			List<FeignClientClass> feignClientClassList = feignClassAndModel.getFeignClientClasses();
			for (FeignClientClass feignClientClass : feignClientClassList) {
				getLog().info(feignClientClass.toString());
			}

			getLog().info("Feign model list:");

			List<FeignModel> feignModels = feignClassAndModel.getFeignModels();
			for (FeignModel feignModel : feignModels) {
				getLog().info(feignModel.toString());
			}

			// 序列化、发送
			if (local.equals("true")) {
				FeignClassSendUtil.localSave(feignClassAndModel);
			} else {
				FeignClassSendUtil.serializeAndSend(feignClassAndModel);
			}

		} catch (Exception e) {
			getLog().error(e);
		}
	}
}
