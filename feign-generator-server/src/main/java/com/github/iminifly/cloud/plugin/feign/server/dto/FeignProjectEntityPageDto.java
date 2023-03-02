package com.github.iminifly.cloud.plugin.feign.server.dto;

import java.util.List;

import com.github.iminifly.cloud.plugin.feign.server.entity.FeignProjectEntity;

import lombok.Data;

/**
 * FeignProjectEntityPageDto
 *
 * @author XGF
 * @date 2020/11/19 21:36
 */
@Data
public class FeignProjectEntityPageDto {

	private Integer total;

	private List<FeignProjectEntity> list;
}
