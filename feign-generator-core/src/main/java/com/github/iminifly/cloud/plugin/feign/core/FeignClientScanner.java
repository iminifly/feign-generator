package com.github.iminifly.cloud.plugin.feign.core;

import static com.github.iminifly.cloud.plugin.feign.core.util.Constants.CONTROLLER_NAME;
import static com.github.iminifly.cloud.plugin.feign.core.util.Constants.DELETE_MAPPING_NAME;
import static com.github.iminifly.cloud.plugin.feign.core.util.Constants.GET_MAPPING_NAME;
import static com.github.iminifly.cloud.plugin.feign.core.util.Constants.PATH_VARIABLE_NAME;
import static com.github.iminifly.cloud.plugin.feign.core.util.Constants.POST_MAPPING_NAME;
import static com.github.iminifly.cloud.plugin.feign.core.util.Constants.PUT_MAPPING_NAME;
import static com.github.iminifly.cloud.plugin.feign.core.util.Constants.REQUEST_BODY_NAME;
import static com.github.iminifly.cloud.plugin.feign.core.util.Constants.REQUEST_MAPPING_NAME;
import static com.github.iminifly.cloud.plugin.feign.core.util.Constants.REQUEST_METHOD_NAME;
import static com.github.iminifly.cloud.plugin.feign.core.util.Constants.REQUEST_PARAM_NAME;
import static com.github.iminifly.cloud.plugin.feign.core.util.Constants.REST_CONTROLLER_NAME;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.iminifly.cloud.plugin.feign.core.model.FeignClientClass;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignMethod;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignMethodArgument;
import com.github.iminifly.cloud.plugin.feign.core.util.ClazzUtil;
import com.github.iminifly.cloud.plugin.feign.core.util.DynamicClasspath;
import com.github.iminifly.cloud.plugin.feign.core.util.SpringControllerUtil;

/**
 * FeignClientScanner
 *
 * @author XGF
 * @date 2020/11/20 14:30
 */
public class FeignClientScanner {

	private final Set<String> modelClassNames = new HashSet<>();

	private final String modelScanPath;

	private final DynamicClasspath dynamicClasspath;

	public final Class<Annotation> controller;
	public final Class<Annotation> restController;
	public final Class<Annotation> deleteMapping;
	public final Class<Annotation> getMapping;
	public final Class<Annotation> pathVariable;
	public final Class<Annotation> postMapping;
	public final Class<Annotation> putMapping;
	public final Class<Annotation> requestBody;
	public final Class<Annotation> requestMapping;
	public final Class<Annotation> requestParam;
	public final Class<Annotation> requestMethod;

	public FeignClientScanner(String modelScanPath, DynamicClasspath dynamicClasspath) {
		this.modelScanPath = modelScanPath;
		this.dynamicClasspath = dynamicClasspath;

		this.dynamicClasspath.getClassLoader();

		controller = this.dynamicClasspath.loadAnnotationClazz(CONTROLLER_NAME);
		restController = this.dynamicClasspath.loadAnnotationClazz(REST_CONTROLLER_NAME);
		deleteMapping = this.dynamicClasspath.loadAnnotationClazz(DELETE_MAPPING_NAME);
		getMapping = this.dynamicClasspath.loadAnnotationClazz(GET_MAPPING_NAME);
		pathVariable = this.dynamicClasspath.loadAnnotationClazz(PATH_VARIABLE_NAME);
		postMapping = this.dynamicClasspath.loadAnnotationClazz(POST_MAPPING_NAME);
		putMapping = this.dynamicClasspath.loadAnnotationClazz(PUT_MAPPING_NAME);
		requestBody = this.dynamicClasspath.loadAnnotationClazz(REQUEST_BODY_NAME);
		requestMapping = this.dynamicClasspath.loadAnnotationClazz(REQUEST_MAPPING_NAME);
		requestParam = this.dynamicClasspath.loadAnnotationClazz(REQUEST_PARAM_NAME);
		requestMethod = this.dynamicClasspath.loadAnnotationClazz(REQUEST_METHOD_NAME);
	}

	public List<FeignClientClass> scan(String scanPackage) {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(this.dynamicClasspath.getClassLoader());

		List<String> clazzList = ClazzUtil.getClazzName(scanPackage, true, this.dynamicClasspath.getClassLoader());

		List<FeignClientClass> list = new ArrayList<>();

		for (String clazzName : clazzList) {

			// 反射获取controller类
			try {

				Class<?> clazz = Class.forName(clazzName, false, this.dynamicClasspath.getClassLoader());

				// 判断是否是一个controller
				Annotation controller = clazz.getDeclaredAnnotation(this.controller);

				if (controller == null) {
					Annotation restController = clazz.getDeclaredAnnotation(this.restController);
					if (restController == null) {
						continue;
					}
				}

				FeignClientClass feignClientClass = resolveFeignClientClass(clazz);

				list.add(feignClientClass);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				Thread.currentThread().setContextClassLoader(classLoader);
			}
		}

		return list;
	}

	private FeignClientClass resolveFeignClientClass(Class<?> clazz) {

		Annotation requestMapping = clazz.getAnnotation(this.requestMapping);

		if (requestMapping == null) {
			return null;
		}

		// 创建一个FeignClientClass
		FeignClientClass feignClientClass = new FeignClientClass();

		// 获取类名
		String clazzSimpleName = clazz.getSimpleName();

		// 获取feign接口名
		String feignName = clazzSimpleName.replaceAll("Controller", "");
		// 设置feign类名
		feignClientClass.setFeignClassName(feignName + "Feign");

		// 获取url
		String url = SpringControllerUtil.resolveUrlFromAnnotation(requestMapping);

		feignClientClass.setUrl(url);

		// 获取所有方法
		Method[] methods = clazz.getMethods();

		// 保存这个feign下面的所有接口方法
		List<FeignMethod> feignMethodList = new ArrayList<>();

		for (Method method : methods) {
			FeignMethod feignMethod = resolveFeignMethodFromJavaMethod(method);
			if (feignMethod != null) {
				feignMethodList.add(feignMethod);
			}
		}

		feignClientClass.setMethods(feignMethodList);

		return feignClientClass;
	}

	private FeignMethod resolveFeignMethodFromJavaMethod(Method method) {

		Annotation requestMapping = method.getAnnotation(this.requestMapping);

		String url = null;
		String httpMethod = null;

		if (requestMapping == null) {
			if (method.getAnnotation(this.getMapping) != null) {
				Annotation getMapping = method.getAnnotation(this.getMapping);
				url = SpringControllerUtil.resolveUrlFromAnnotation(getMapping);
				httpMethod = "GET";
			} else if (method.getAnnotation(this.postMapping) != null) {
				Annotation postMapping = method.getAnnotation(this.postMapping);
				url = SpringControllerUtil.resolveUrlFromAnnotation(postMapping);
				httpMethod = "POST";
			} else if (method.getAnnotation(this.putMapping) != null) {
				Annotation putMapping = method.getAnnotation(this.putMapping);
				url = SpringControllerUtil.resolveUrlFromAnnotation(putMapping);
				httpMethod = "PUT";
			} else if (method.getAnnotation(deleteMapping) != null) {
				Annotation deleteMapping = method.getAnnotation(this.deleteMapping);
				url = SpringControllerUtil.resolveUrlFromAnnotation(deleteMapping);
				httpMethod = "DELETE";
			}
		} else {
			url = SpringControllerUtil.resolveUrlFromAnnotation(requestMapping);
			httpMethod = SpringControllerUtil.resolveHttpMethodFromRequestMapping(requestMapping);
		}

		if (url == null) {
			return null;
		}

		FeignMethod feignMethod = new FeignMethod();
		feignMethod.setUrl(url);
		feignMethod.setHttpMethod(httpMethod);

		feignMethod.setName(method.getName());

		Type genericReturnType = method.getGenericReturnType();

		feignMethod.setReturnType(genericReturnType.getTypeName().replaceAll("([a-z0-9]+\\.)*", ""));

		try {
			if (genericReturnType instanceof ParameterizedType) {
				getModelClassFromType((ParameterizedType) method.getGenericReturnType(), modelClassNames);
			} else {
				String name = genericReturnType.getTypeName();
				if (name.startsWith(modelScanPath)) {
					modelClassNames.add(name);
					getModelClassFromClassName(name, modelClassNames);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// 解析方法参数
		List<FeignMethodArgument> arguments = new ArrayList<>();

		Parameter[] parameters = method.getParameters();

		for (Parameter parameter : parameters) {
			FeignMethodArgument feignMethodArgument = resolveFeignMethodArgumentFromParameter(parameter);
			arguments.add(feignMethodArgument);
		}

		feignMethod.setArguments(arguments);

		return feignMethod;
	}

	private FeignMethodArgument resolveFeignMethodArgumentFromParameter(Parameter parameter) {

		FeignMethodArgument argument = new FeignMethodArgument();

		String name = parameter.getName();
		Class<?> type = parameter.getType();
		Type parameterizedType = parameter.getParameterizedType();

		Annotation[] annotations = parameter.getAnnotations();
		List<String> as = new ArrayList<>();
		for (Annotation annotation1 : annotations) {
			Class<? extends Annotation> clazz = annotation1.getClass();
			if (clazz == this.dynamicClasspath.loadClazz(REQUEST_BODY_NAME)
					|| clazz == this.dynamicClasspath.loadClazz(PATH_VARIABLE_NAME)
					|| clazz == this.dynamicClasspath.loadClazz(REQUEST_PARAM_NAME)) {
				as.add(annotation1.annotationType().getSimpleName());
			}
		}

		argument.setArgName(name);
		argument.setArgType(parameterizedType.getTypeName().replaceAll("([a-z0-9]+\\.)*", ""));
		argument.setAnnotations(as);

		try {
			if (parameterizedType instanceof ParameterizedType) {
				getModelClassFromType((ParameterizedType) parameterizedType, modelClassNames);
			} else {
				String name1 = parameterizedType.getTypeName();
				if (name1.startsWith(modelScanPath)) {
					getModelClassFromClassName(name1, modelClassNames);
					modelClassNames.add(name1);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String argumentClassName = type.getName();
		if (argumentClassName.startsWith(modelScanPath)) {
			modelClassNames.add(argumentClassName);
		}

		return argument;
	}

	private void getModelClassFromType(ParameterizedType type, Set<String> list) throws ClassNotFoundException {
		Type rawType = type.getRawType();
		Class<? extends Type> aClass = rawType.getClass();
		if (notClass(aClass)) {
			return;
		}
		String typeName = rawType.getTypeName();
		if (typeName.startsWith(modelScanPath)) {
			list.add(typeName);
		}
		Type[] actualTypeArguments = type.getActualTypeArguments();
		for (Type actualTypeArgument : actualTypeArguments) {
			if (actualTypeArgument instanceof ParameterizedType) {
				getModelClassFromType((ParameterizedType) actualTypeArgument, list);
			} else {
				String name = actualTypeArgument.getTypeName();
				if (name.startsWith(modelScanPath) && !list.contains(name)) {
					list.add(name);
					getModelClassFromClassName(name, list);
				}
			}
		}
	}

	private void getModelClassFromClassName(String className, Set<String> list) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(className, false, this.dynamicClasspath.getClassLoader());
		if (notClass(clazz)) {
			return;
		}
		if (list.contains(className)) {
			return;
		}
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			Type type = field.getGenericType();
			if (type instanceof ParameterizedType) {
				getModelClassFromType((ParameterizedType) type, list);
			} else {
				String name = type.getTypeName();
				if (name.startsWith(modelScanPath) && !list.contains(name)) {
					list.add(name);
					getModelClassFromClassName(name, list);
				}
			}
		}
	}

	private boolean notClass(Class<?> clazz) {
		return clazz.isAnnotation() || clazz.isAnonymousClass() || clazz.isArray() || clazz.isEnum()
				|| clazz.isInterface();
	}

	public Set<String> getModelClassNames() {
		return modelClassNames;
	}
}
