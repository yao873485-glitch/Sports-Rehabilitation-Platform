package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.dto.FollowupProjectConfigDTO;
import com.rehabilitation.dto.FollowupProjectConfigQueryDTO;
import com.rehabilitation.entity.FollowupProjectConfig;

/**
 * 随访项目配置服务接口
 */
public interface FollowupProjectConfigService extends IService<FollowupProjectConfig> {

    /**
     * 保存随访项目配置
     *
     * @param dto 项目配置DTO
     * @return 保存的项目配置
     */
    FollowupProjectConfig saveProjectConfig(FollowupProjectConfigDTO dto);

    /**
     * 分页查询项目配置列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<FollowupProjectConfig> getProjectConfigPage(FollowupProjectConfigQueryDTO queryDTO);

    /**
     * 更新随访项目配置
     *
     * @param dto 项目配置DTO
     * @return 更新的项目配置
     */
    FollowupProjectConfig updateProjectConfig(FollowupProjectConfigDTO dto);
}
