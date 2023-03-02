package com.github.iminifly.cloud.plugin.feign.server.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.github.iminifly.cloud.plugin.feign.server.entity.FeignClientEntity;

/**
 * FeignClientMapper
 *
 * @author XGF
 * @date 2020/11/19 22:16
 */
public interface FeignClientMapper {

	/**
	 * FeignClientMapper.insert
	 *
	 * @param feignClientEntity 对象
	 * @author xuguofeng
	 * @date 2020/11/19 17:57
	 */
	void insert(FeignClientEntity feignClientEntity);

	/**
	 * FeignClientMapper.insertBatch
	 *
	 * @param list 集合
	 * @author xuguofeng
	 * @date 2020/11/19 17:57
	 */
	void insertBatch(@Param("list") List<FeignClientEntity> list);

	/**
	 * FeignClientMapper.deleteByProjectId
	 *
	 * @param projectId projectId
	 * @author XGF
	 * @date 2020/11/19 22:18
	 */
	void deleteByProjectId(@Param("projectId") Integer projectId);

	/**
	 * FeignClientMapper.selectById
	 *
	 * @param id id
	 * @return FeignClientEntity对象
	 * @author xuguofeng
	 * @date 2020/11/19 18:20
	 */
	FeignClientEntity selectById(@Param("id") Integer id);

	/**
	 * FeignClientMapper.selectByProjectIdAndPage
	 *
	 * @param projectId projectId
	 * @param start     start
	 * @param size      size
	 * @return List<FeignClientEntity>
	 * @author XGF
	 * @date 2020/11/19 21:59
	 */
	List<FeignClientEntity> selectByProjectIdAndPage(@Param("projectId") Integer projectId,
			@Param("start") Integer start, @Param("size") Integer size);

	/**
	 * FeignClientMapper.countByProjectId
	 *
	 * @param projectId projectId
	 * @return 行数
	 * @author XGF
	 * @date 2020/11/19 21:59
	 */
	int countByProjectId(@Param("projectId") Integer projectId);
}
