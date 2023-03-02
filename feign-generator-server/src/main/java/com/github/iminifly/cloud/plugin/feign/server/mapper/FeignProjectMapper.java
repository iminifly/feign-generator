package com.github.iminifly.cloud.plugin.feign.server.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.github.iminifly.cloud.plugin.feign.server.entity.FeignProjectEntity;

/**
 * FeignProjectMapper
 *
 * @author xuguofeng
 * @date 2020/11/19 17:57
 */
public interface FeignProjectMapper {

	/**
	 * FeignProjectMapper.insert
	 *
	 * @param feignProject 对象
	 * @author xuguofeng
	 * @date 2020/11/19 17:57
	 */
	void insert(FeignProjectEntity feignProject);

	/**
	 * FeignProjectMapper.update
	 *
	 * @param feignProject 对象
	 * @author xuguofeng
	 * @date 2020/11/19 17:57
	 */
	void update(FeignProjectEntity feignProject);

	/**
	 * FeignProjectMapper.selectById
	 *
	 * @param id id
	 * @return FeignProjectEntity对象
	 * @author xuguofeng
	 * @date 2020/11/19 18:20
	 */
	FeignProjectEntity selectById(@Param("id") Integer id);

	/**
	 * FeignProjectMapper.selectByGroupAndName
	 *
	 * @param groupId     groupId
	 * @param projectName projectName
	 * @return FeignProjectEntity对象
	 * @author xuguofeng
	 * @date 2020/11/19 18:20
	 */
	FeignProjectEntity selectByGroupAndName(@Param("groupId") String groupId, @Param("projectName") String projectName);

	/**
	 * FeignProjectMapper.selectByGroupAndNameLikeAndPage
	 *
	 * @param groupId     groupId
	 * @param projectName projectName
	 * @param start       start
	 * @param size        size
	 * @return List<FeignProjectEntity>
	 * @author XGF
	 * @date 2020/11/19 21:59
	 */
	List<FeignProjectEntity> selectByGroupAndNameLikeAndPage(@Param("groupId") String groupId,
			@Param("projectName") String projectName, @Param("start") Integer start, @Param("size") Integer size);

	/**
	 * FeignProjectMapper.countByGroupAndNameLike
	 *
	 * @param groupId     groupId
	 * @param projectName projectName
	 * @return 行数
	 * @author XGF
	 * @date 2020/11/19 21:59
	 */
	int countByGroupAndNameLike(@Param("groupId") String groupId, @Param("projectName") String projectName);

	/**
	 * FeignProjectMapper.selectAll
	 *
	 * @return List<FeignProjectEntity>
	 * @author XGF
	 * @date 2020/11/19 21:59
	 */
	List<FeignProjectEntity> selectAll();
}
