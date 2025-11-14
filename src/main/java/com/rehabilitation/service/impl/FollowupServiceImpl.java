package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.FollowupEnrollmentDTO;
import com.rehabilitation.dto.FollowupEnrollmentVO;
import com.rehabilitation.dto.FollowupQueryDTO;
import com.rehabilitation.entity.FollowupPatientEnrollment;
import com.rehabilitation.entity.FollowupPlanRepository;
import com.rehabilitation.entity.FollowupProgramMaster;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.FollowupPatientEnrollmentMapper;
import com.rehabilitation.mapper.FollowupPlanRepositoryMapper;
import com.rehabilitation.mapper.FollowupProgramMasterMapper;
import com.rehabilitation.service.FollowupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 随访患者入组记录服务实现
 */
@Service
public class FollowupServiceImpl
    extends ServiceImpl<FollowupPatientEnrollmentMapper, FollowupPatientEnrollment>
    implements FollowupService {

    @Autowired
    private FollowupPlanRepositoryMapper planMapper;

    @Autowired
    private FollowupProgramMasterMapper programMapper;

    @Override
    public PageResult<FollowupEnrollmentVO> getFollowupPage(FollowupQueryDTO query) {
        Integer pageNum = query.getPageNum() != null ? query.getPageNum() : 1;
        Integer pageSize = query.getPageSize() != null ? query.getPageSize() : 10;

        Page<FollowupEnrollmentVO> page = new Page<>(pageNum, pageSize);
        IPage<FollowupEnrollmentVO> result = baseMapper.selectFollowupPage(page, query);

        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public FollowupEnrollmentVO getFollowupDetail(Long id) {
        FollowupEnrollmentVO vo = baseMapper.selectFollowupById(id);
        if (vo == null) {
            throw new BusinessException("随访患者记录不存在");
        }

        return vo;
    }

    @Override
    public void removeFromProject(Long id) {
        FollowupPatientEnrollment entity = getById(id);
        if (entity == null) {
            throw new BusinessException("随访患者记录不存在");
        }

        // 检查是否已经退出
        String currentStatus = getFieldValue(entity, "followupStatus");
        if ("已退出".equals(currentStatus)) {
            throw new BusinessException("患者已退出项目，无需重复操作");
        }

        // 使用UpdateWrapper更新状态
        baseMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<FollowupPatientEnrollment>()
            .eq("id", id)
            .set("followup_status", "已退出")
            .set("updated_time", LocalDateTime.now()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long enrollPatient(FollowupEnrollmentDTO dto) {
        // 直接使用getter方法获取DTO字段
        Integer patientId = dto.getPatientId();
        String followupPlanName = dto.getFollowupPlanName();
        String followupProjectName = dto.getFollowupProjectName();
        String followupTeam = dto.getFollowupTeam();
        String primaryDoctor = dto.getPrimaryDoctor();
        String healthManager = dto.getHealthManager();
        LocalDateTime applicationTime = dto.getApplicationTime();
        LocalDateTime enrollmentTime = dto.getEnrollmentTime();
        String followupStatus = dto.getFollowupStatus();
        String enrollmentRemark = dto.getEnrollmentRemark();
        String createdBy = dto.getCreatedBy();

        // 添加调试日志
        System.out.println("=== 入组调试信息 ===");
        System.out.println("patientId: " + patientId);
        System.out.println("followupTeam: " + followupTeam);
        System.out.println("followupStatus: " + followupStatus);

        // 检查patientId是否为null
        if (patientId == null) {
            throw new BusinessException("患者ID不能为空");
        }

        // 检查是否已经入组
        QueryWrapper<FollowupPatientEnrollment> checkQuery = new QueryWrapper<>();
        checkQuery.eq("patient_id", patientId);
        long count = baseMapper.selectCount(checkQuery);
        if (count > 0) {
            // 已存在，返回已有记录的ID
            FollowupPatientEnrollment existing = baseMapper.selectOne(checkQuery);
            return getFieldValue(existing, "id");
        }

        // 查询随访计划
        Long planId = null;
        if (followupPlanName != null && !followupPlanName.isEmpty()) {
            QueryWrapper<FollowupPlanRepository> planQuery = new QueryWrapper<>();
            planQuery.eq("plan_name", followupPlanName).last("LIMIT 1");
            FollowupPlanRepository plan = planMapper.selectOne(planQuery);
            if (plan != null) {
                planId = plan.getId();
            }
        }

        // 查询随访项目
        Long projectId = null;
        if (followupProjectName != null && !followupProjectName.isEmpty()) {
            QueryWrapper<FollowupProgramMaster> projectQuery = new QueryWrapper<>();
            projectQuery.eq("project_name", followupProjectName).last("LIMIT 1");
            FollowupProgramMaster project = programMapper.selectOne(projectQuery);
            if (project != null) {
                projectId = project.getId();
            }
        }

        // 生成序号
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String serialNumber = "SF" + LocalDateTime.now().format(formatter);

        // 创建实体对象并使用反射设置值
        FollowupPatientEnrollment entity = new FollowupPatientEnrollment();
        setFieldValue(entity, "serialNumber", serialNumber);
        setFieldValue(entity, "patientId", patientId);
        setFieldValue(entity, "followupPlanId", planId);
        setFieldValue(entity, "followupProjectId", projectId);
        setFieldValue(entity, "followupTeam", followupTeam != null ? followupTeam : "西南医科大学附属医院");
        setFieldValue(entity, "primaryDoctor", primaryDoctor);
        setFieldValue(entity, "healthManager", healthManager);
        setFieldValue(entity, "applicationTime", applicationTime != null ? applicationTime : LocalDateTime.now());
        setFieldValue(entity, "enrollmentTime", enrollmentTime != null ? enrollmentTime : LocalDateTime.now());
        setFieldValue(entity, "followupStatus", followupStatus != null ? followupStatus : "待入组");
        setFieldValue(entity, "enrollmentRemark", enrollmentRemark);
        setFieldValue(entity, "createdBy", createdBy);
        setFieldValue(entity, "createdTime", LocalDateTime.now());
        setFieldValue(entity, "updatedTime", LocalDateTime.now());

        System.out.println("准备插入的实体: patientId=" + getFieldValue(entity, "patientId") +
                         ", team=" + getFieldValue(entity, "followupTeam"));

        // 使用MyBatis-Plus的insert方法
        baseMapper.insert(entity);

        System.out.println("插入成功，ID=" + getFieldValue(entity, "id"));

        return getFieldValue(entity, "id");
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

    /**
     * 使用反射设置字段值，避免Lombok问题
     */
    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            System.err.println("设置字段失败: " + fieldName + ", 错误: " + e.getMessage());
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }
}
