package com.github.iminifly.cloud.plugin.feign.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

/**
 * FeignProjectEntity
 *
 * @author XGF
 * @date 2020/11/19 17:35
 */
@Data
public class FeignProjectEntity {

	private Integer id;

	private String groupId;

	private String projectName;

	private String serviceVersion;

	private String description;

	private byte[] feignZipFile;

	private long size;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	private Integer version;
}
