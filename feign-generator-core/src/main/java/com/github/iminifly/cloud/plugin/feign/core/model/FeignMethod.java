package com.github.iminifly.cloud.plugin.feign.core.model;

import java.util.List;
import lombok.Data;

/**
 * FeignMethod
 *
 * @author XGF
 * @date 2020/11/12 22:17
 */
@Data
public class FeignMethod {

	private String name;

	private String returnType;

	private List<FeignMethodArgument> arguments;

	private String url;

	private String httpMethod;
}
