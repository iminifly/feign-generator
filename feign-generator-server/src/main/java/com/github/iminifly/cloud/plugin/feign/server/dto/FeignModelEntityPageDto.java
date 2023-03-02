package com.github.iminifly.cloud.plugin.feign.server.dto;

import java.util.List;

import com.github.iminifly.cloud.plugin.feign.server.entity.FeignModelEntity;

import lombok.Data;

/**
 * FeignModelEntityPageDto
 *
 * @author XGF
 * @date 2020/11/19 21:36
 */
@Data
public class FeignModelEntityPageDto {

	private Integer total;

	private List<FeignModelEntity> list;
}
