package com.github.iminifly.cloud.plugin.feign.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

/**
 * FeignModelEntity
 *
 * @author XGF
 * @date 2020/11/19 17:35
 */
@Data
public class FeignModelEntity {

	private Integer id;

	private Integer projectId;

	private String packageName;

	private String modelName;

	private String modelClassContent;

	private long size;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
}
