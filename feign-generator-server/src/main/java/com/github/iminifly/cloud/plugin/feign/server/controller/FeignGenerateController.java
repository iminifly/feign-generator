package com.github.iminifly.cloud.plugin.feign.server.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.iminifly.cloud.plugin.feign.core.model.FeignClassAndModel;
import com.github.iminifly.cloud.plugin.feign.server.dto.FeignClientEntityPageDto;
import com.github.iminifly.cloud.plugin.feign.server.dto.FeignModelEntityPageDto;
import com.github.iminifly.cloud.plugin.feign.server.dto.FeignProjectEntityPageDto;
import com.github.iminifly.cloud.plugin.feign.server.entity.FeignClientEntity;
import com.github.iminifly.cloud.plugin.feign.server.entity.FeignModelEntity;
import com.github.iminifly.cloud.plugin.feign.server.entity.FeignProjectEntity;
import com.github.iminifly.cloud.plugin.feign.server.response.Response;
import com.github.iminifly.cloud.plugin.feign.server.service.FeignGenerateService;

/**
 * 接收feign-generator-plugin发送的feign接口元数据并把数据插入到对应的表里面
 *
 * @author XGF
 * @date 2020/11/7 11:13
 */
@RestController
@RequestMapping("/api/v1/feign")
public class FeignGenerateController {

	@Resource
	private FeignGenerateService feignGenerateService;

	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	@ResponseBody
	public Response<Object> generate(@RequestBody FeignClassAndModel feignClassAndModel) {
		feignGenerateService.save(feignClassAndModel);
		return Response.success(null);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Response<FeignProjectEntity> id(@PathVariable("id") Integer id) {
		return Response.success(feignGenerateService.findById(id));
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public Response<FeignProjectEntityPageDto> search(@RequestParam("groupId") String groupId,
			@RequestParam("projectName") String projectName, @RequestParam("page") Integer page,
			@RequestParam("size") Integer size) {
		return Response.success(feignGenerateService.search(groupId, projectName, page, size));
	}

	@RequestMapping(value = "/clients", method = RequestMethod.GET)
	@ResponseBody
	public Response<FeignClientEntityPageDto> searchFeignClient(@RequestParam("projectId") Integer projectId,
			@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		return Response.success(feignGenerateService.searchFeignClient(projectId, page, size));
	}

	@RequestMapping(value = "/client/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Response<FeignClientEntity> feignClient(@PathVariable("id") Integer id) {
		return Response.success(feignGenerateService.findFeignClientById(id));
	}

	@RequestMapping(value = "/models", method = RequestMethod.GET)
	@ResponseBody
	public Response<FeignModelEntityPageDto> searchFeignModel(@RequestParam("projectId") Integer projectId,
			@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		return Response.success(feignGenerateService.searchFeignModel(projectId, page, size));
	}

	@RequestMapping(value = "/model/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Response<FeignModelEntity> feignModel(@PathVariable("id") Integer id) {
		return Response.success(feignGenerateService.findFeignModelById(id));
	}

	@RequestMapping(value = "/download/{id}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<byte[]> downloadZipFile(@PathVariable("id") Integer id) {
		FeignProjectEntity project = feignGenerateService.findById(id);
		HttpHeaders headers = new HttpHeaders();
		headers.put("Content-Disposition",
				Collections.singletonList("attachment;filename=" + project.getProjectName() + ".zip"));
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(project.getFeignZipFile());
	}

	@RequestMapping(value = "/project/all", method = RequestMethod.GET)
	@ResponseBody
	public Response<List<Map<String, Object>>> allFeignProjectEntity() {
		return Response.success(feignGenerateService.allFeignProjectEntity());
	}
}
