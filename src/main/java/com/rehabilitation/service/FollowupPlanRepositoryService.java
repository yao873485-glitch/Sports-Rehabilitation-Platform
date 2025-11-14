package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.FollowupPlanCreateDTO;
import com.rehabilitation.dto.FollowupPlanQueryDTO;
import com.rehabilitation.dto.FollowupPlanVO;
import com.rehabilitation.entity.FollowupPlanRepository;

/**
 * 随访计划仓库Service接口
 */
public interface FollowupPlanRepositoryService extends IService<FollowupPlanRepository> {

    /**
     * 分页查询随访计划
     */
    PageResult<FollowupPlanVO> getFollowupPlanPage(FollowupPlanQueryDTO queryDTO);

    /**
     * 根据ID获取随访计划详情
     */
    FollowupPlanVO getFollowupPlanById(Long id);

    /**
     * 删除随访计划
     */
    boolean deleteFollowupPlan(Long id);

    /**
     * 根据模板创建随访计划
     * @param dto 创建信息
     * @return 计划ID
     */
    Long createPlanFromTemplate(FollowupPlanCreateDTO dto);
}