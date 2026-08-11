package com.rehabilitation.service.impl;

import com.rehabilitation.entity.HealthRecord;
import com.rehabilitation.mapper.HealthRecordMapper;
import com.rehabilitation.service.HealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 健康档案服务实现类
 */
@Service
public class HealthRecordServiceImpl implements HealthRecordService {

    @Autowired
    private HealthRecordMapper healthRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HealthRecord saveOrUpdate(HealthRecord healthRecord) {
        LocalDateTime now = LocalDateTime.now();

        // 使用反射获取 patientId，因为 Lombok 编译问题
        Integer patientId = getFieldValue(healthRecord, "patientId");

        // 检查该患者是否已有健康档案
        HealthRecord existing = healthRecordMapper.selectByPatientId(patientId);

        if (existing != null) {
            // 更新现有档案
            Long existingId = getFieldValue(existing, "id");
            LocalDateTime createdAt = getFieldValue(existing, "createdAt");

            setFieldValue(healthRecord, "id", existingId);
            setFieldValue(healthRecord, "createdAt", createdAt);
            setFieldValue(healthRecord, "updatedAt", now);
            healthRecordMapper.updateById(healthRecord);
        } else {
            // 创建新档案
            setFieldValue(healthRecord, "createdAt", now);
            setFieldValue(healthRecord, "updatedAt", now);
            healthRecordMapper.insert(healthRecord);
        }

        return healthRecord;
    }

    @Override
    public HealthRecord getByPatientId(Integer patientId) {
        return healthRecordMapper.selectByPatientId(patientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByPatientId(Integer patientId) {
        int rows = healthRecordMapper.deleteByPatientId(patientId);
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
