package com.github.iminifly.cloud.plugin.feign.server.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.github.iminifly.cloud.plugin.feign.server.entity.FeignModelEntity;

/**
 * FeignClientMapper
 *
 * @author XGF
 * @date 2020/11/19 22:16
 */
public interface FeignModelMapper {

	/**
	 * FeignModelMapper.insert
	 *
	 * @param feignModelEntity 对象
	 * @author xuguofeng
	 * @date 2020/11/19 17:57
	 */
	void insert(FeignModelEntity feignModelEntity);

	/**
	 * FeignModelMapper.insertBatch
	 *
	 * @param list 集合
	 * @author xuguofeng
	 * @date 2020/11/19 17:57
	 */
	void insertBatch(List<FeignModelEntity> list);

	/**
	 * FeignModelMapper.deleteByProjectId
	 *
	 * @param projectId projectId
	 * @author XGF
	 * @date 2020/11/19 22:18
	 */
	void deleteByProjectId(@Param("projectId") Integer projectId);

	/**
	 * FeignModelMapper.selectById
	 *
	 * @param id id
	 * @return FeignClientEntity对象
	 * @author xuguofeng
	 * @date 2020/11/19 18:20
	 */
	FeignModelEntity selectById(@Param("id") Integer id);

	/**
	 * FeignModelMapper.selectByProjectIdAndPage
	 *
	 * @param projectId projectId
	 * @param start     start
	 * @param size      size
	 * @return List<FeignModelEntity>
	 * @author XGF
	 * @date 2020/11/19 21:59
	 */
	List<FeignModelEntity> selectByProjectIdAndPage(@Param("projectId") Integer projectId,
			@Param("start") Integer start, @Param("size") Integer size);

	/**
	 * FeignModelMapper.countByProjectId
	 *
	 * @param projectId projectId
	 * @return 行数
	 * @author XGF
	 * @date 2020/11/19 21:59
	 */
	int countByProjectId(@Param("projectId") Integer projectId);
}
