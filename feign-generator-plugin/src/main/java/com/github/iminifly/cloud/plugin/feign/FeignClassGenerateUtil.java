package com.github.iminifly.cloud.plugin.feign;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.github.iminifly.cloud.plugin.feign.core.FeignClientScanner;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignClassAndModel;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignClientClass;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignModel;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignModelField;
import com.github.iminifly.cloud.plugin.feign.core.util.DynamicClasspath;

/**
 * FeignClassGenerateUtil
 *
 * @author XGF
 * @date 2020/11/17 14:47
 */
class FeignClassGenerateUtil {

	static FeignClassAndModel getFeignClassAndModel(String scanPackage, DynamicClasspath dynamicClasspath) {

		FeignClientScanner scanner = new FeignClientScanner(FeignAutoGenerator.modelScanPath, dynamicClasspath);

		List<FeignClientClass> list = scanner.scan(scanPackage);

		FeignClassAndModel feignClassAndModel = new FeignClassAndModel();
		feignClassAndModel.setFeignClientClasses(list);

		List<FeignModel> models = new ArrayList<>();

		for (String modelClassName : scanner.getModelClassNames()) {

			try {

				Class<?> clazz = Class.forName(modelClassName, false, dynamicClasspath.getClassLoader());

				FeignModel model = new FeignModel();
				model.setPackageName(modelClassName.substring(0, modelClassName.lastIndexOf(".")));
				model.setModelName(modelClassName.substring(modelClassName.lastIndexOf(".") + 1));

				Field[] fields = clazz.getDeclaredFields();
				Field[] parentFields = null;
				Class<?> superclass = clazz.getSuperclass();
				if (superclass != null && superclass.getName().startsWith(FeignAutoGenerator.modelScanPath)) {
					parentFields = superclass.getDeclaredFields();
				}

				List<FeignModelField> feignModelFields = new ArrayList<>();

				for (Field field : fields) {
					FeignModelField modelField = new FeignModelField();
					modelField.setName(field.getName());
					modelField.setType(field.getGenericType().getTypeName().replaceAll("([a-z0-9]+\\.)*", ""));
					feignModelFields.add(modelField);
				}

				if (parentFields != null) {
					for (Field field : parentFields) {
						FeignModelField modelField = new FeignModelField();
						modelField.setName(field.getName());
						modelField.setType(field.getGenericType().getTypeName().replaceAll("([a-z0-9]+\\.)*", ""));
						feignModelFields.add(modelField);
					}
				}

				model.setModelFields(feignModelFields);

				models.add(model);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		feignClassAndModel.setFeignModels(models);
		feignClassAndModel.setServiceName(FeignAutoGenerator.mavenProject.getArtifactId());
		feignClassAndModel.setDescription(FeignAutoGenerator.mavenProject.getDescription());
		feignClassAndModel.setVersion(FeignAutoGenerator.mavenProject.getVersion());

		String groupId = FeignAutoGenerator.mavenProject.getGroupId();
		if (groupId == null) {
			groupId = FeignAutoGenerator.mavenProject.getParent().getGroupId();
		}
		feignClassAndModel.setGroupId(groupId);

		return feignClassAndModel;
	}
}
