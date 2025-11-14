package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.FollowupPlanQueryDTO;
import com.rehabilitation.dto.FollowupPlanVO;
import com.rehabilitation.entity.FollowupPlanRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 随访计划仓库Mapper接口
 */
@Mapper
public interface FollowupPlanRepositoryMapper extends BaseMapper<FollowupPlanRepository> {

    /**
     * 分页查询随访计划
     */
    IPage<FollowupPlanVO> selectFollowupPlanPage(Page<FollowupPlanVO> page, @Param("dto") FollowupPlanQueryDTO queryDTO);

    /**
     * 根据ID查询随访计划详情
     */
    FollowupPlanVO selectFollowupPlanById(@Param("id") Long id);
}