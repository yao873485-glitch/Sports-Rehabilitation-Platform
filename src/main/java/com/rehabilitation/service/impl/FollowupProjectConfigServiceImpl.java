package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.dto.FollowupProjectConfigDTO;
import com.rehabilitation.dto.FollowupProjectConfigQueryDTO;
import com.rehabilitation.entity.FollowupProjectConfig;
import com.rehabilitation.mapper.FollowupProjectConfigMapper;
import com.rehabilitation.service.FollowupProjectConfigService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 随访项目配置服务实现
 */
@Service
public class FollowupProjectConfigServiceImpl extends ServiceImpl<FollowupProjectConfigMapper, FollowupProjectConfig>
        implements FollowupProjectConfigService {

    @Override
    public FollowupProjectConfig saveProjectConfig(FollowupProjectConfigDTO dto) {
        FollowupProjectConfig config = new FollowupProjectConfig();

        // 手动设置字段值（避免Lombok问题）
        config.setProjectCode(dto.getProjectCode());
        config.setProjectName(dto.getProjectName());
        config.setProjectBrief(dto.getProjectBrief());
        config.setProjectDescription(dto.getProjectDescription());
        config.setDiseaseType(dto.getDiseaseType());
        config.setProjectType(dto.getProjectType());
        config.setConsultationTimes(dto.getConsultationTimes());
        config.setSingleDuration(dto.getSingleDuration());
        config.setBindPlan(dto.getBindPlan());
        config.setOperator(dto.getOperator());

        // 转换布尔值为整数
        config.setIsPublished(dto.getIsPublished() != null && dto.getIsPublished() ? 1 : 0);

        // 手动设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        config.setCreatedTime(now);
        config.setUpdatedTime(now);

        // 保存到数据库
        this.save(config);

        return config;
    }

    @Override
    public Page<FollowupProjectConfig> getProjectConfigPage(FollowupProjectConfigQueryDTO queryDTO) {
        // 创建分页对象
        Page<FollowupProjectConfig> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 构建查询条件
        QueryWrapper<FollowupProjectConfig> queryWrapper = new QueryWrapper<>();

        // 项目名称模糊查询
        if (StringUtils.hasText(queryDTO.getProjectName())) {
            queryWrapper.like("project_name", queryDTO.getProjectName());
        }

        // 是否发布
        if (queryDTO.getIsPublished() != null) {
            queryWrapper.eq("is_published", queryDTO.getIsPublished());
        }

        // 日期范围查询
        if (StringUtils.hasText(queryDTO.getStartDate()) && StringUtils.hasText(queryDTO.getEndDate())) {
            queryWrapper.between("created_time", queryDTO.getStartDate() + " 00:00:00", queryDTO.getEndDate() + " 23:59:59");
        }

        // 按创建时间倒序排列
        queryWrapper.orderByDesc("created_time");

        // 执行分页查询
        return this.page(page, queryWrapper);
    }

    @Override
    public FollowupProjectConfig updateProjectConfig(FollowupProjectConfigDTO dto) {
        // 获取现有配置
        FollowupProjectConfig config = this.getById(dto.getId());
        if (config == null) {
            throw new RuntimeException("项目配置不存在");
        }

        // 更新字段值（避免Lombok问题）
        config.setProjectName(dto.getProjectName());
        config.setProjectBrief(dto.getProjectBrief());
        config.setProjectDescription(dto.getProjectDescription());
        config.setDiseaseType(dto.getDiseaseType());
        config.setProjectType(dto.getProjectType());
        config.setConsultationTimes(dto.getConsultationTimes());
        config.setSingleDuration(dto.getSingleDuration());
        config.setBindPlan(dto.getBindPlan());
        config.setOperator(dto.getOperator());

        // 转换布尔值为整数
        config.setIsPublished(dto.getIsPublished() != null && dto.getIsPublished() ? 1 : 0);

        // 更新修改时间
        config.setUpdatedTime(LocalDateTime.now());

        // 更新到数据库
        this.updateById(config);

        return config;
    }
}
