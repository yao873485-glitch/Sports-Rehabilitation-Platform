package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.dto.FollowupPlanDTO;
import com.rehabilitation.dto.FollowupPlanQueryDTO;
import com.rehabilitation.entity.FollowupPlan;

/**
 * 随访计划服务接口
 */
public interface FollowupPlanService extends IService<FollowupPlan> {

    /**
     * 保存随访计划（草稿或发布）
     *
     * @param dto 计划DTO
     * @return 保存的计划
     */
    FollowupPlan savePlan(FollowupPlanDTO dto);

    /**
     * 分页查询随访计划列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<FollowupPlan> getPlanPage(FollowupPlanQueryDTO queryDTO);
}
