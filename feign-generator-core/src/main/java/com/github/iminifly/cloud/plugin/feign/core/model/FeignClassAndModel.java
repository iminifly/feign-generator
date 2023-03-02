package com.github.iminifly.cloud.plugin.feign.core.model;

import java.util.List;
import lombok.Data;

/**
 * FeignClassAndModel
 *
 * @author XGF
 * @date 2020/11/17 20:09
 */
@Data
public class FeignClassAndModel {

	private String serviceName;

	private String groupId;

	private String description;

	private String version;

	private List<FeignClientClass> feignClientClasses;

	private List<FeignModel> feignModels;
}
