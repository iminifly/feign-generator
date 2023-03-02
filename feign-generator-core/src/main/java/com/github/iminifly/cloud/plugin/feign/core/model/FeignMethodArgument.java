package com.github.iminifly.cloud.plugin.feign.core.model;

import java.util.List;
import lombok.Data;

/**
 * FeignMethodArgument
 *
 * @author XGF
 * @date 2020/11/12 22:16
 */
@Data
public class FeignMethodArgument {

	private String argName;

	private String argType;

	private List<String> annotations;
}
