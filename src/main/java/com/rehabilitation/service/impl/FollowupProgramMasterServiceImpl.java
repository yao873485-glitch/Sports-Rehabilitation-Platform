package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.FollowupProgramCreateDTO;
import com.rehabilitation.dto.FollowupProgramQueryDTO;
import com.rehabilitation.dto.FollowupProgramUpdateDTO;
import com.rehabilitation.dto.FollowupProgramVO;
import com.rehabilitation.entity.FollowupProgramMaster;
import com.rehabilitation.mapper.FollowupProgramMasterMapper;
import com.rehabilitation.service.FollowupProgramMasterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 随访项目主数据Service实现类
 */
@Service
public class FollowupProgramMasterServiceImpl extends ServiceImpl<FollowupProgramMasterMapper, FollowupProgramMaster>
        implements FollowupProgramMasterService {

    @Resource
    private FollowupProgramMasterMapper followupProgramMasterMapper;

    @Override
    public PageResult<FollowupProgramVO> getFollowupProgramPage(FollowupProgramQueryDTO queryDTO) {
        Page<FollowupProgramVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<FollowupProgramVO> result = followupProgramMasterMapper.selectFollowupProgramPage(page, queryDTO);

        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public FollowupProgramVO getFollowupProgramById(Long id) {
        return followupProgramMasterMapper.selectFollowupProgramById(id);
    }

    @Override
    public boolean updateFollowupProgram(FollowupProgramUpdateDTO updateDTO) {
        UpdateWrapper<FollowupProgramMaster> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", updateDTO.getId())
                .set("project_name", updateDTO.getProjectName())
                .set("project_description", updateDTO.getProjectDescription())
                .set("linked_followup_plan", updateDTO.getLinkedFollowupPlan())
                .set("is_published", updateDTO.getIsPublished())
                .set("last_modified_time", LocalDateTime.now());

        return this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProgramFromPlan(FollowupProgramCreateDTO dto) {
        // 创建实体并插入
        FollowupProgramMaster program = new FollowupProgramMaster();

        // 使用反射获取DTO字段值
        String projectCode = getFieldValue(dto, "projectCode");
        String projectName = getFieldValue(dto, "projectName");
        String projectDescription = getFieldValue(dto, "projectDescription");
        String linkedFollowupPlan = getFieldValue(dto, "linkedFollowupPlan");
        Integer isPublished = getFieldValue(dto, "isPublished");
        String operator = getFieldValue(dto, "operator");

        // 直接设置实体属性
        program.setProjectCode(projectCode);
        program.setProjectName(projectName);
        program.setProjectDescription(projectDescription != null ? projectDescription : "-");
        program.setLinkedFollowupPlan(linkedFollowupPlan != null ? linkedFollowupPlan : "-");
        program.setIsPublished(isPublished != null ? isPublished : 1);
        program.setOperator(operator != null ? operator : "系统");
        program.setCreatedTime(LocalDateTime.now());
        program.setLastModifiedTime(LocalDateTime.now());

        // 插入数据
        baseMapper.insert(program);

        // 返回自动生成的ID
        return program.getId();
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

    @Override
    public int countByTemplate(String templateName) {
        QueryWrapper<FollowupProgramMaster> query = new QueryWrapper<>();
        query.like("project_name", templateName);
        return Math.toIntExact(baseMapper.selectCount(query));
    }
}
