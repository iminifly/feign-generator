package com.github.iminifly.cloud.plugin.feign.server.dto;

import java.util.List;

import com.github.iminifly.cloud.plugin.feign.server.entity.FeignClientEntity;

import lombok.Data;

/**
 * FeignClientEntityPageDto
 *
 * @author XGF
 * @date 2020/11/19 21:36
 */
@Data
public class FeignClientEntityPageDto {

	private Integer total;

	private List<FeignClientEntity> list;
}
