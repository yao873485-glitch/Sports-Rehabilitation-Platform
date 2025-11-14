package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.FollowupPlanCreateDTO;
import com.rehabilitation.dto.FollowupPlanQueryDTO;
import com.rehabilitation.dto.FollowupPlanVO;
import com.rehabilitation.entity.FollowupPlanRepository;
import com.rehabilitation.mapper.FollowupPlanRepositoryMapper;
import com.rehabilitation.service.FollowupPlanRepositoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 随访计划仓库Service实现类
 */
@Service
public class FollowupPlanRepositoryServiceImpl extends ServiceImpl<FollowupPlanRepositoryMapper, FollowupPlanRepository>
        implements FollowupPlanRepositoryService {

    @Resource
    private FollowupPlanRepositoryMapper followupPlanRepositoryMapper;

    @Override
    public PageResult<FollowupPlanVO> getFollowupPlanPage(FollowupPlanQueryDTO queryDTO) {
        Page<FollowupPlanVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<FollowupPlanVO> result = followupPlanRepositoryMapper.selectFollowupPlanPage(page, queryDTO);

        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public FollowupPlanVO getFollowupPlanById(Long id) {
        return followupPlanRepositoryMapper.selectFollowupPlanById(id);
    }

    @Override
    public boolean deleteFollowupPlan(Long id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPlanFromTemplate(FollowupPlanCreateDTO dto) {
        // 创建实体并插入
        FollowupPlanRepository plan = new FollowupPlanRepository();

        // 使用反射获取DTO字段值
        String planName = getFieldValue(dto, "planName");
        Long linkedProjectId = getFieldValue(dto, "linkedProjectId");
        String linkedProject = getFieldValue(dto, "linkedProject");
        String versionNumber = getFieldValue(dto, "versionNumber");
        String status = getFieldValue(dto, "status");
        String versionRemark = getFieldValue(dto, "versionRemark");

        // 直接设置实体属性
        plan.setPlanName(planName);
        plan.setLinkedProject(linkedProject != null ? linkedProject : "-");
        plan.setVersionNumber(versionNumber != null ? versionNumber : "1.0");
        plan.setStatus(status != null ? status : "未开始");
        plan.setVersionRemark(versionRemark != null ? versionRemark : "-");
        plan.setCreatedTime(LocalDateTime.now());
        plan.setLastModifiedTime(LocalDateTime.now());

        // 如果linkedProjectId不为null，则设置
        if (linkedProjectId != null) {
            plan.setLinkedProjectId(linkedProjectId);
        }

        // 插入数据
        baseMapper.insert(plan);

        // 返回自动生成的ID
        return plan.getId();
    }

    /**
     * 使用反射获取字段值，避免Lombok问题
     */
    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }
}