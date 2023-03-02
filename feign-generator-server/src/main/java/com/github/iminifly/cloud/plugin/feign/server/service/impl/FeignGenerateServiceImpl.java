package com.github.iminifly.cloud.plugin.feign.server.service.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.iminifly.cloud.plugin.feign.core.TemplateProcessor;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignClassAndModel;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignClientClass;
import com.github.iminifly.cloud.plugin.feign.core.model.FeignModel;
import com.github.iminifly.cloud.plugin.feign.core.util.ZipUtil;
import com.github.iminifly.cloud.plugin.feign.core.util.ZipUtil.Text;
import com.github.iminifly.cloud.plugin.feign.server.dto.FeignClientEntityPageDto;
import com.github.iminifly.cloud.plugin.feign.server.dto.FeignModelEntityPageDto;
import com.github.iminifly.cloud.plugin.feign.server.dto.FeignProjectEntityPageDto;
import com.github.iminifly.cloud.plugin.feign.server.entity.FeignClientEntity;
import com.github.iminifly.cloud.plugin.feign.server.entity.FeignModelEntity;
import com.github.iminifly.cloud.plugin.feign.server.entity.FeignProjectEntity;
import com.github.iminifly.cloud.plugin.feign.server.mapper.FeignClientMapper;
import com.github.iminifly.cloud.plugin.feign.server.mapper.FeignModelMapper;
import com.github.iminifly.cloud.plugin.feign.server.mapper.FeignProjectMapper;
import com.github.iminifly.cloud.plugin.feign.server.service.FeignGenerateService;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

/**
 * FeignGenerateServiceImpl
 *
 * @author xuguofeng
 * @date 2020/11/19 17:50
 */
@Slf4j
@Service
public class FeignGenerateServiceImpl implements FeignGenerateService {

	@Resource
	private FeignProjectMapper feignProjectMapper;

	@Resource
	private FeignClientMapper feignClientMapper;

	@Resource
	private FeignModelMapper feignModelMapper;

	@Override
	public void save(FeignClassAndModel feignClassAndModel) {

		List<Text> pathAndFileContents = new ArrayList<>();

		List<FeignClientClass> feignClientClasses = feignClassAndModel.getFeignClientClasses();

		List<FeignModel> feignModels = feignClassAndModel.getFeignModels();

		List<Text> feignClientClassList = resolveFeignClientClassList(feignClientClasses);
		List<Text> feignModelList = resolveFeignModelList(feignModels);

		pathAndFileContents.addAll(feignClientClassList);
		pathAndFileContents.addAll(feignModelList);

		byte[] bytes = ZipUtil.zipTexts(pathAndFileContents);

		// 保存feign project
		int projectId = saveProject(feignClassAndModel, bytes);

		// 保存feign client
		saveFeignClient(projectId, feignClientClasses, feignClientClassList);

		// 保存feign model
		saveFeignModel(projectId, feignModels, feignModelList);
	}

	@Override
	public FeignProjectEntity findById(Integer id) {
		return feignProjectMapper.selectById(id);
	}

	@Override
	public FeignProjectEntityPageDto search(String groupId, String projectName, Integer page, Integer size) {

		int count = feignProjectMapper.countByGroupAndNameLike(groupId, projectName);

		FeignProjectEntityPageDto dto = new FeignProjectEntityPageDto();
		dto.setTotal(count);

		if (count == 0) {
			dto.setList(new ArrayList<>());
		} else {
			int start = (page - 1) * size;
			dto.setList(feignProjectMapper.selectByGroupAndNameLikeAndPage(groupId, projectName, start, size));
		}

		return dto;
	}

	@Override
	public FeignClientEntityPageDto searchFeignClient(Integer projectId, Integer page, Integer size) {

		int count = feignClientMapper.countByProjectId(projectId);

		FeignClientEntityPageDto dto = new FeignClientEntityPageDto();
		dto.setTotal(count);

		if (count == 0) {
			dto.setList(new ArrayList<>());
		} else {
			int start = (page - 1) * size;
			dto.setList(feignClientMapper.selectByProjectIdAndPage(projectId, start, size));
		}

		return dto;
	}

	@Override
	public FeignClientEntity findFeignClientById(Integer id) {
		return feignClientMapper.selectById(id);
	}

	@Override
	public FeignModelEntityPageDto searchFeignModel(Integer projectId, Integer page, Integer size) {

		int count = feignModelMapper.countByProjectId(projectId);

		FeignModelEntityPageDto dto = new FeignModelEntityPageDto();
		dto.setTotal(count);

		if (count == 0) {
			dto.setList(new ArrayList<>());
		} else {
			int start = (page - 1) * size;
			dto.setList(feignModelMapper.selectByProjectIdAndPage(projectId, start, size));
		}

		return dto;
	}

	@Override
	public FeignModelEntity findFeignModelById(Integer id) {
		return feignModelMapper.selectById(id);
	}

	@Override
	public List<Map<String, Object>> allFeignProjectEntity() {
		List<FeignProjectEntity> feignProjectEntities = feignProjectMapper.selectAll();
		List<Map<String, Object>> result = new ArrayList<>();
		for (FeignProjectEntity projectEntity : feignProjectEntities) {
			result.add(new HashMap<String, Object>(1) {

				private static final long serialVersionUID = -1739728229649076630L;

				{
					put("id", projectEntity.getId());
					put("name", projectEntity.getGroupId() + " " + projectEntity.getProjectName());
				}
			});
		}
		return result;
	}

	private int saveProject(FeignClassAndModel feignClassAndModel, byte[] bytes) {

		FeignProjectEntity projectEntity = new FeignProjectEntity();
		projectEntity.setFeignZipFile(bytes);
		projectEntity.setSize(bytes.length);
		projectEntity.setDescription(feignClassAndModel.getDescription());
		projectEntity.setProjectName(feignClassAndModel.getServiceName());
		projectEntity.setGroupId(feignClassAndModel.getGroupId());
		projectEntity.setServiceVersion(feignClassAndModel.getVersion());
		projectEntity.setUpdateTime(new Date());

		FeignProjectEntity entity = feignProjectMapper.selectByGroupAndName(feignClassAndModel.getGroupId(),
				feignClassAndModel.getServiceName());

		if (entity == null) {
			projectEntity.setCreateTime(new Date());
			projectEntity.setVersion(1);
			feignProjectMapper.insert(projectEntity);
		} else {
			projectEntity.setId(entity.getId());
			projectEntity.setVersion(entity.getVersion());
			feignProjectMapper.update(projectEntity);
		}
		return projectEntity.getId();
	}

	private List<Text> resolveFeignClientClassList(List<FeignClientClass> feignClientClasses) {

		List<Text> pathAndFileContents = new ArrayList<>();

		for (FeignClientClass feignClientClass : feignClientClasses) {

			try {

				// 生成feign接口
				String feignClient = TemplateProcessor.parseFeignClient(feignClientClass);

				Text pathAndFileContent = new Text();

				pathAndFileContent.setText(feignClient);
				pathAndFileContent.setFileName(feignClientClass.getFeignClassName() + ".java");

				pathAndFileContents.add(pathAndFileContent);

				// 生成fallback
				String feignClientFallBack = TemplateProcessor.parseFeignClientFallBack(feignClientClass);

				Text pathAndFileContent2 = new Text();

				pathAndFileContent2.setText(feignClientFallBack);
				pathAndFileContent2
						.setFileName("fallback/" + feignClientClass.getFeignClassName() + "FallBackFactory.java");

				pathAndFileContents.add(pathAndFileContent2);

			} catch (IOException | TemplateException e) {
				log.error(e.getMessage(), e);
			}
		}

		return pathAndFileContents;
	}

	private List<Text> resolveFeignModelList(List<FeignModel> feignModels) {

		List<Text> pathAndFileContents = new ArrayList<>();

		for (FeignModel feignModel : feignModels) {

			try {

				String model = TemplateProcessor.parseFeignModel(feignModel);

				Text pathAndFileContent = new Text();

				pathAndFileContent.setText(model);

				pathAndFileContent.setFileName(
						feignModel.getPackageName().replace('.', '/') + "/" + feignModel.getModelName() + ".java");

				pathAndFileContents.add(pathAndFileContent);

			} catch (IOException | TemplateException e) {
				log.error(e.getMessage(), e);
			}
		}

		return pathAndFileContents;
	}

	private void saveFeignModel(int projectId, List<FeignModel> feignModels, List<Text> feignModelList) {

		feignModelMapper.deleteByProjectId(projectId);

		Map<String, Text> map = new HashMap<>(8);

		for (Text text : feignModelList) {
			map.put(text.getFileName().replace('/', '.'), text);
		}

		List<FeignModelEntity> list = new ArrayList<>();

		for (FeignModel feignModel : feignModels) {

			FeignModelEntity entity = new FeignModelEntity();

			entity.setCreateTime(new Date());
			entity.setModelName(feignModel.getModelName());
			entity.setPackageName(feignModel.getPackageName());
			entity.setProjectId(projectId);
			entity.setUpdateTime(new Date());

			String content = map.get(feignModel.getPackageName() + "." + feignModel.getModelName() + ".java").getText();

			entity.setModelClassContent(content);
			entity.setSize(content.getBytes(Charset.forName("UTF-8")).length);

			list.add(entity);
		}

		feignModelMapper.insertBatch(list);
	}

	private void saveFeignClient(int projectId, List<FeignClientClass> feignClientClasses,
			List<Text> feignClientClassList) {

		feignClientMapper.deleteByProjectId(projectId);

		Map<String, Text> map = new HashMap<>(8);

		for (Text text : feignClientClassList) {
			map.put(text.getFileName().replace('/', '.'), text);
		}

		List<FeignClientEntity> list = new ArrayList<>();

		for (FeignClientClass feignClientClass : feignClientClasses) {

			FeignClientEntity entity = new FeignClientEntity();

			entity.setCreateTime(new Date());
			entity.setUpdateTime(new Date());
			entity.setProjectId(projectId);
			entity.setUrl(feignClientClass.getUrl());
			entity.setFeignClassName(feignClientClass.getFeignClassName());
			entity.setFallbackFactoryName(feignClientClass.getFeignClassName() + "FallBackFactory");

			String content1 = map.get(feignClientClass.getFeignClassName() + ".java").getText();

			entity.setSize(content1.getBytes(Charset.forName("UTF-8")).length);
			entity.setFeignClassContent(content1);

			String content2 = map.get("fallback." + entity.getFallbackFactoryName() + ".java").getText();
			entity.setFallbackFactoryContent(content2);

			list.add(entity);
		}

		feignClientMapper.insertBatch(list);
	}
}
