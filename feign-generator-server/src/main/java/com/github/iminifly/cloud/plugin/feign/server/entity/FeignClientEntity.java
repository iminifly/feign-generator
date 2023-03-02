package com.github.iminifly.cloud.plugin.feign.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

/**
 * FeignClientEntity
 *
 * @author XGF
 * @date 2020/11/19 17:34
 */
@Data
public class FeignClientEntity {

	private Integer id;

	private Integer projectId;

	private String feignClassName;

	private String url;

	private String fallbackFactoryName;

	private String feignClassContent;

	private String fallbackFactoryContent;

	private long size;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
}
