package com.rehabilitation.service.impl;

import com.rehabilitation.entity.DiseaseRecord;
import com.rehabilitation.mapper.DiseaseRecordMapper;
import com.rehabilitation.service.DiseaseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 专病档案服务实现类
 */
@Service
public class DiseaseRecordServiceImpl implements DiseaseRecordService {

    @Autowired
    private DiseaseRecordMapper diseaseRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DiseaseRecord saveOrUpdate(DiseaseRecord diseaseRecord) {
        LocalDateTime now = LocalDateTime.now();

        // 使用手动 getter 方法获取 patientId
        Integer patientId = diseaseRecord.getPatientId();

        if (patientId == null) {
            throw new RuntimeException("患者ID不能为空");
        }

        // 检查该患者是否已有专病档案
        DiseaseRecord existing = diseaseRecordMapper.selectByPatientId(patientId);

        if (existing != null) {
            // 更新现有档案
            Long existingId = getFieldValue(existing, "id");
            LocalDateTime createdAt = getFieldValue(existing, "createdAt");

            setFieldValue(diseaseRecord, "id", existingId);
            setFieldValue(diseaseRecord, "createdAt", createdAt);
            setFieldValue(diseaseRecord, "updatedAt", now);
            diseaseRecordMapper.updateById(diseaseRecord);
        } else {
            // 创建新档案
            setFieldValue(diseaseRecord, "createdAt", now);
            setFieldValue(diseaseRecord, "updatedAt", now);
            diseaseRecordMapper.insert(diseaseRecord);
        }

        return diseaseRecord;
    }

    @Override
    public DiseaseRecord getByPatientId(Integer patientId) {
        return diseaseRecordMapper.selectByPatientId(patientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByPatientId(Integer patientId) {
        int rows = diseaseRecordMapper.deleteByPatientId(patientId);
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
