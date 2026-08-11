package com.rehabilitation.service.impl;

import com.rehabilitation.entity.EnrollmentAssessment;
import com.rehabilitation.mapper.EnrollmentAssessmentMapper;
import com.rehabilitation.service.EnrollmentAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 入组评估服务实现类
 */
@Service
public class EnrollmentAssessmentServiceImpl implements EnrollmentAssessmentService {

    @Autowired
    private EnrollmentAssessmentMapper enrollmentAssessmentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EnrollmentAssessment saveOrUpdate(EnrollmentAssessment enrollmentAssessment) {
        LocalDateTime now = LocalDateTime.now();

        // 使用手动 getter 方法获取 patientId
        Integer patientId = enrollmentAssessment.getPatientId();

        if (patientId == null) {
            throw new RuntimeException("患者ID不能为空");
        }

        // 检查该患者是否已有入组评估
        EnrollmentAssessment existing = enrollmentAssessmentMapper.selectByPatientId(patientId);

        if (existing != null) {
            // 更新现有评估
            Long existingId = getFieldValue(existing, "id");
            LocalDateTime createdAt = getFieldValue(existing, "createdAt");

            setFieldValue(enrollmentAssessment, "id", existingId);
            setFieldValue(enrollmentAssessment, "createdAt", createdAt);
            setFieldValue(enrollmentAssessment, "updatedAt", now);
            enrollmentAssessmentMapper.updateById(enrollmentAssessment);
        } else {
            // 创建新评估
            setFieldValue(enrollmentAssessment, "createdAt", now);
            setFieldValue(enrollmentAssessment, "updatedAt", now);
            enrollmentAssessmentMapper.insert(enrollmentAssessment);
        }

        return enrollmentAssessment;
    }

    @Override
    public EnrollmentAssessment getByPatientId(Integer patientId) {
        return enrollmentAssessmentMapper.selectByPatientId(patientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByPatientId(Integer patientId) {
        int rows = enrollmentAssessmentMapper.deleteByPatientId(patientId);
        return rows > 0;
    }

    /**
     * 使用反射获取字段值（解决 Lombok 编译问题）
     */
    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field: " + fieldName, e);
        }
    }

    /**
     * 使用反射设置字段值（解决 Lombok 编译问题）
     */
    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }
}
