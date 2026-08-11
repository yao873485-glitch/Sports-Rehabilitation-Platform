package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.FollowupEnrollmentDTO;
import com.rehabilitation.dto.FollowupEnrollmentVO;
import com.rehabilitation.dto.FollowupQueryDTO;
import com.rehabilitation.entity.FollowupPatientList;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.FollowupPatientListMapper;
import com.rehabilitation.service.FollowupService;
import com.rehabilitation.service.support.DistributedLockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FollowupServiceImpl
    extends ServiceImpl<FollowupPatientListMapper, FollowupPatientList>
    implements FollowupService {

    private final DistributedLockService distributedLockService;

    public FollowupServiceImpl(DistributedLockService distributedLockService) {
        this.distributedLockService = distributedLockService;
    }

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
        FollowupPatientList entity = getById(id);
        if (entity == null) {
            throw new BusinessException("随访患者记录不存在");
        }

        String currentStatus = getFieldValue(entity, "followupStatus");
        if ("已退出".equals(currentStatus)) {
            throw new BusinessException("患者已退出项目，无需重复操作");
        }

        baseMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<FollowupPatientList>()
            .eq("id", id)
            .set("followup_status", "已退出")
            .set("updated_time", LocalDateTime.now()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long enrollPatient(FollowupEnrollmentDTO dto) {
        Integer patientId = dto.getPatientId();
        if (patientId == null) {
            throw new BusinessException("患者ID不能为空");
        }
        return distributedLockService.executeWithLock("lock:followup:enroll:" + patientId, 3, 10, () -> doEnroll(dto));
    }

    private Long doEnroll(FollowupEnrollmentDTO dto) {
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
        String source = dto.getSource();

        FollowupPatientList existing = baseMapper.selectByPatientId(patientId);
        if (existing != null) {
            setFieldValue(existing, "followupPlanId", dto.getFollowupPlanId());
            setFieldValue(existing, "followupPlanName", followupPlanName);
            setFieldValue(existing, "followupProject", followupProjectName);
            setFieldValue(existing, "followupTeam", followupTeam != null ? followupTeam : getFieldValue(existing, "followupTeam"));
            setFieldValue(existing, "primaryDoctor", primaryDoctor != null ? primaryDoctor : getFieldValue(existing, "primaryDoctor"));
            setFieldValue(existing, "healthManager", healthManager != null ? healthManager : getFieldValue(existing, "healthManager"));
            setFieldValue(existing, "followupStatus", followupStatus != null ? followupStatus : getFieldValue(existing, "followupStatus"));
            setFieldValue(existing, "updatedTime", LocalDateTime.now());
            setFieldValue(existing, "updatedBy", createdBy);
            baseMapper.updateById(existing);
            return getFieldValue(existing, "id");
        }

        FollowupPatientList entity = new FollowupPatientList();
        setFieldValue(entity, "patientId", patientId);
        setFieldValue(entity, "source", source != null ? source : "医生推荐");
        setFieldValue(entity, "followupTeam", followupTeam != null ? followupTeam : "西南医科大学附属医院");
        setFieldValue(entity, "primaryDoctor", primaryDoctor);
        setFieldValue(entity, "healthManager", healthManager);
        setFieldValue(entity, "applicationTime", applicationTime != null ? applicationTime : LocalDateTime.now());
        setFieldValue(entity, "enrollmentTime", enrollmentTime != null ? enrollmentTime : LocalDateTime.now());
        setFieldValue(entity, "followupStatus", followupStatus != null ? followupStatus : "待入组");
        setFieldValue(entity, "followupPlanId", dto.getFollowupPlanId());
        setFieldValue(entity, "followupPlanName", followupPlanName);
        setFieldValue(entity, "followupProject", followupProjectName);
        setFieldValue(entity, "remark", enrollmentRemark);
        setFieldValue(entity, "createdBy", createdBy);
        setFieldValue(entity, "createdTime", LocalDateTime.now());
        setFieldValue(entity, "updatedTime", LocalDateTime.now());

        baseMapper.insert(entity);
        return getFieldValue(entity, "id");
    }

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

    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }
}
