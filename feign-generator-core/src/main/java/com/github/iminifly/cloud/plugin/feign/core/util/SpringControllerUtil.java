package com.github.iminifly.cloud.plugin.feign.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SpringControllerUtil
 *
 * @author XGF
 * @date 2020/11/20 14:22
 */
public class SpringControllerUtil {

	public static String resolveUrlFromAnnotation(Annotation requestMapping) {
		try {
			Method valueMethod = requestMapping.getClass().getDeclaredMethod("value");
			Method pathMethod = requestMapping.getClass().getDeclaredMethod("path");
			String[] value = (String[]) valueMethod.invoke(requestMapping);
			if (value.length == 0) {
				String[] paths = (String[]) pathMethod.invoke(requestMapping);
				if (paths.length == 0) {
					return "";
				}
				return paths[0];
			}
			return value[0];
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static String resolveHttpMethodFromRequestMapping(Annotation requestMapping) {
		try {
			Method method = requestMapping.getClass().getDeclaredMethod("method");
			Object[] methods = (Object[]) method.invoke(requestMapping);
			if (methods.length == 0) {
				return "GET";
			}
			return methods[0].toString();
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
