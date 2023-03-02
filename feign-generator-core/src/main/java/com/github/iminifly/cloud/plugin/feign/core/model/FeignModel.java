package com.github.iminifly.cloud.plugin.feign.core.model;

import java.util.List;
import lombok.Data;

/**
 * FeignModel
 *
 * @author XGF
 * @date 2020/11/17 16:13
 */
@Data
public class FeignModel {

	private String packageName;

	private String modelName;

	private List<FeignModelField> modelFields;
}
