package com.rehabilitation.service.impl;

import com.rehabilitation.entity.SchemeCycleConfig;
import com.rehabilitation.mapper.SchemeCycleConfigMapper;
import com.rehabilitation.service.SchemeCycleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 方案周期配置服务实现类
 */
@Service
public class SchemeCycleConfigServiceImpl implements SchemeCycleConfigService {

    @Autowired
    private SchemeCycleConfigMapper schemeCycleConfigMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SchemeCycleConfig saveOrUpdate(SchemeCycleConfig config) {
        LocalDateTime now = LocalDateTime.now();

        // 使用手动 getter 方法获取关键字段
        Integer patientId = config.getPatientId();
        String schemeId = config.getSchemeId();
        Integer cycleMonths = config.getCycleMonths();

        if (patientId == null) {
            throw new RuntimeException("患者ID不能为空");
        }

        if (cycleMonths == null || cycleMonths < 1 || cycleMonths > 12) {
            throw new RuntimeException("方案周期必须在1-12个月之间");
        }

        // 检查该患者和方案是否已有配置
        SchemeCycleConfig existing = schemeCycleConfigMapper.selectByPatientIdAndSchemeId(patientId, schemeId);

        if (existing != null) {
            // 更新现有配置
            Long existingId = getFieldValue(existing, "id");
            LocalDateTime createdAt = getFieldValue(existing, "createdAt");

            setFieldValue(config, "id", existingId);
            setFieldValue(config, "createdAt", createdAt);
            setFieldValue(config, "updatedAt", now);

            // 如果有开始日期，自动计算结束日期
            LocalDate startDate = getFieldValue(config, "startDate");
            if (startDate != null && cycleMonths != null) {
                LocalDate endDate = startDate.plusMonths(cycleMonths);
                setFieldValue(config, "endDate", endDate);
            }

            schemeCycleConfigMapper.updateById(config);
        } else {
            // 创建新配置
            setFieldValue(config, "createdAt", now);
            setFieldValue(config, "updatedAt", now);
            setFieldValue(config, "status", 1); // 默认有效

            // 如果有开始日期，自动计算结束日期
            LocalDate startDate = getFieldValue(config, "startDate");
            if (startDate != null && cycleMonths != null) {
                LocalDate endDate = startDate.plusMonths(cycleMonths);
                setFieldValue(config, "endDate", endDate);
            }

            schemeCycleConfigMapper.insert(config);
        }

        return config;
    }

    @Override
    public SchemeCycleConfig getByPatientId(Integer patientId) {
        return schemeCycleConfigMapper.selectByPatientId(patientId);
    }

    @Override
    public SchemeCycleConfig getByPatientIdAndSchemeId(Integer patientId, String schemeId) {
        return schemeCycleConfigMapper.selectByPatientIdAndSchemeId(patientId, schemeId);
    }

    @Override
    public SchemeCycleConfig getBySchemeId(String schemeId) {
        return schemeCycleConfigMapper.selectBySchemeId(schemeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByPatientId(Integer patientId) {
        int rows = schemeCycleConfigMapper.deleteByPatientId(patientId);
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
