package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.FollowupProgramCreateDTO;
import com.rehabilitation.dto.FollowupProgramQueryDTO;
import com.rehabilitation.dto.FollowupProgramUpdateDTO;
import com.rehabilitation.dto.FollowupProgramVO;
import com.rehabilitation.entity.FollowupProgramMaster;

/**
 * 随访项目主数据Service接口
 */
public interface FollowupProgramMasterService extends IService<FollowupProgramMaster> {

    /**
     * 分页查询随访项目
     */
    PageResult<FollowupProgramVO> getFollowupProgramPage(FollowupProgramQueryDTO queryDTO);

    /**
     * 根据ID获取随访项目详情
     */
    FollowupProgramVO getFollowupProgramById(Long id);

    /**
     * 更新随访项目
     */
    boolean updateFollowupProgram(FollowupProgramUpdateDTO updateDTO);

    /**
     * 根据计划名称自动创建随访项目
     * @param dto 创建信息
     * @return 项目ID
     */
    Long createProgramFromPlan(FollowupProgramCreateDTO dto);

    /**
     * 获取指定模板的项目数量
     * @param templateName 模板名称
     * @return 数量
     */
    int countByTemplate(String templateName);
}
