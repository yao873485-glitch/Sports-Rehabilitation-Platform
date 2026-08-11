package com.rehabilitation.service.impl;

import com.rehabilitation.dto.ContentApplicationConfigDTO;
import com.rehabilitation.entity.ContentApplicationConfig;
import com.rehabilitation.mapper.ContentApplicationConfigMapper;
import com.rehabilitation.messaging.BusinessEventPublisher;
import com.rehabilitation.service.ContentApplicationConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Field;

@Service
public class ContentApplicationConfigServiceImpl implements ContentApplicationConfigService {

    private final ContentApplicationConfigMapper configMapper;
    private final BusinessEventPublisher businessEventPublisher;

    public ContentApplicationConfigServiceImpl(ContentApplicationConfigMapper configMapper,
                                               BusinessEventPublisher businessEventPublisher) {
        this.configMapper = configMapper;
        this.businessEventPublisher = businessEventPublisher;
    }

    @Override
    public ContentApplicationConfig getByContentIdAndType(String contentId, String contentType) {
        return configMapper.selectByContentIdAndType(contentId, contentType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(ContentApplicationConfigDTO dto) {
        String contentId = getFieldValue(dto, "contentId");
        String contentType = getFieldValue(dto, "contentType");
        String operator = getFieldValue(dto, "operator");

        ContentApplicationConfig existing = configMapper.selectByContentIdAndType(contentId, contentType);
        String operationType;
        String operationContent;

        if (existing != null) {
            setFieldValue(existing, "applicationModule", getFieldValue(dto, "applicationModule"));
            setFieldValue(existing, "applicationProject", getFieldValue(dto, "applicationProject"));
            setFieldValue(existing, "channel", getFieldValue(dto, "channel"));
            setFieldValue(existing, "relatedDiseases", getFieldValue(dto, "relatedDiseases"));
            setFieldValue(existing, "relatedSymptoms", getFieldValue(dto, "relatedSymptoms"));
            setFieldValue(existing, "relatedProducts", getFieldValue(dto, "relatedProducts"));
            setFieldValue(existing, "relatedDepartments", getFieldValue(dto, "relatedDepartments"));
            setFieldValue(existing, "relatedDoctors", getFieldValue(dto, "relatedDoctors"));
            setFieldValue(existing, "customTags", getFieldValue(dto, "customTags"));
            setFieldValue(existing, "sortOrder", getFieldValue(dto, "sortOrder"));
            setFieldValue(existing, "isPinned", getFieldValue(dto, "isPinned"));
            setFieldValue(existing, "operator", operator);
            configMapper.updateById(existing);
            operationType = "修改配置";
            operationContent = String.format("修改内容配置: 应用项目=%s, 应用模块=%s, 所属频道=%s",
                    getFieldValue(dto, "applicationProject"),
                    getFieldValue(dto, "applicationModule"),
                    getFieldValue(dto, "channel"));
        } else {
            ContentApplicationConfig newConfig = new ContentApplicationConfig();
            BeanUtils.copyProperties(dto, newConfig);
            configMapper.insert(newConfig);
            operationType = "新增配置";
            operationContent = String.format("新增内容配置: 应用项目=%s, 应用模块=%s, 所属频道=%s",
                    getFieldValue(dto, "applicationProject"),
                    getFieldValue(dto, "applicationModule"),
                    getFieldValue(dto, "channel"));
        }

        publishContentLogAfterCommit(contentId, contentType, operationType, operationContent, operator);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByContentIdAndType(String contentId, String contentType) {
        configMapper.deleteByContentIdAndType(contentId, contentType);
        publishContentLogAfterCommit(contentId, contentType, "删除配置", "删除了内容配置", "系统管理员");
    }

    private void publishContentLogAfterCommit(String contentId, String contentType, String operationType,
                                              String operationContent, String operator) {
        Runnable publishTask = () -> businessEventPublisher.publishContentConfigLog(
                contentId, contentType, operationType, operationContent, operator
        );
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishTask.run();
                }
            });
        } else {
            publishTask.run();
        }
    }

    private <T> T getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

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
