package com.github.iminifly.cloud.plugin.feign.core.model;

import java.util.List;
import lombok.Data;

/**
 * FeignClientClass
 *
 * @author XGF
 * @date 2020/11/12 22:21
 */
@Data
public class FeignClientClass {

	private String feignClassName;

	private String url;

	private List<FeignMethod> methods;
}
