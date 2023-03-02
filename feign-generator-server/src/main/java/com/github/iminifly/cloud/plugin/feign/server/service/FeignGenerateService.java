package com.github.iminifly.cloud.plugin.feign.server.service;

import java.util.List;
import java.util.Map;

import com.github.iminifly.cloud.plugin.feign.core.model.FeignClassAndModel;
import com.github.iminifly.cloud.plugin.feign.server.dto.FeignClientEntityPageDto;
import com.github.iminifly.cloud.plugin.feign.server.dto.FeignModelEntityPageDto;
import com.github.iminifly.cloud.plugin.feign.server.dto.FeignProjectEntityPageDto;
import com.github.iminifly.cloud.plugin.feign.server.entity.FeignClientEntity;
import com.github.iminifly.cloud.plugin.feign.server.entity.FeignModelEntity;
import com.github.iminifly.cloud.plugin.feign.server.entity.FeignProjectEntity;

/**
 * FeignGenerateService
 *
 * @author xuguofeng
 * @date 2020/11/19 17:48
 */
public interface FeignGenerateService {

	/**
	 * 保存FeignClassAndModel数据
	 *
	 * @param feignClassAndModel 客户端发来的feign接口元数据信息
	 * @author xuguofeng
	 * @date 2020/11/19 17:48
	 */
	void save(FeignClassAndModel feignClassAndModel);

	/**
	 * 根据ID查询FeignProjectEntity对象
	 *
	 * @param id id
	 * @return FeignProjectEntity对象
	 * @author xuguofeng
	 * @date 2020/11/19 17:48
	 */
	FeignProjectEntity findById(Integer id);

	/**
	 * 根据project名称模糊分页查询
	 *
	 * @param groupId     groupId
	 * @param projectName 名称
	 * @param page        页码
	 * @param size        行数
	 * @return FeignProjectEntityPageDto
	 * @author XGF
	 * @date 2020/11/19 21:46
	 */
	FeignProjectEntityPageDto search(String groupId, String projectName, Integer page, Integer size);

	/**
	 * 根据project id分页查询feign client
	 *
	 * @param projectId project id
	 * @param page      页码
	 * @param size      行数
	 * @return FeignClientEntityPageDto
	 * @author XGF
	 * @date 2020/11/19 21:46
	 */
	FeignClientEntityPageDto searchFeignClient(Integer projectId, Integer page, Integer size);

	/**
	 * 根据clientid查询client
	 *
	 * @param id id
	 * @return FeignClientEntity
	 * @author xuguofeng
	 * @date 2020/11/23 15:30
	 */
	FeignClientEntity findFeignClientById(Integer id);

	/**
	 * 根据project id分页查询feign model
	 *
	 * @param projectId project id
	 * @param page      页码
	 * @param size      行数
	 * @return FeignModelEntityPageDto
	 * @author XGF
	 * @date 2020/11/19 21:46
	 */
	FeignModelEntityPageDto searchFeignModel(Integer projectId, Integer page, Integer size);

	/**
	 * 根据modelid查询model
	 *
	 * @param id id
	 * @return FeignModelEntity
	 * @author xuguofeng
	 * @date 2020/11/23 15:30
	 */
	FeignModelEntity findFeignModelById(Integer id);

	/**
	 * 返回全部project集合，使用map封装每一个project的id->name值
	 *
	 * @return List<Map < Integer , String>>
	 * @author XGF
	 * @date 2020/11/19 21:53
	 */
	List<Map<String, Object>> allFeignProjectEntity();
}
