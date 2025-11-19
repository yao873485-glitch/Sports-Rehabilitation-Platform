package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rehabilitation.dto.ContentApplicationConfigDTO;
import com.rehabilitation.entity.ContentApplicationConfig;
import com.rehabilitation.entity.EduContentApplication;
import com.rehabilitation.mapper.ContentApplicationConfigMapper;
import com.rehabilitation.mapper.EduContentApplicationMapper;
import com.rehabilitation.service.ContentApplicationConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;

/**
 * 内容应用配置Service实现类
 */
@Service
public class ContentApplicationConfigServiceImpl implements ContentApplicationConfigService {

    @Autowired
    private ContentApplicationConfigMapper configMapper;

    @Autowired
    private EduContentApplicationMapper eduContentApplicationMapper;

    @Override
    public ContentApplicationConfig getByContentIdAndType(String contentId, String contentType) {
        return configMapper.selectByContentIdAndType(contentId, contentType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(ContentApplicationConfigDTO dto) {
        // 先查询是否存在
        ContentApplicationConfig existing = configMapper.selectByContentIdAndType(
                getFieldValue(dto, "contentId"),
                getFieldValue(dto, "contentType")
        );

        if (existing != null) {
            // 更新
            setFieldValue(existing, "applicationModule", getFieldValue(dto, "applicationModule"));
            setFieldValue(existing, "applicationProject", getFieldValue(dto, "applicationProject"));
            setFieldValue(existing, "channel", getFieldValue(dto, "channel"));
            setFieldValue(existing, "relatedDiseases", getFieldValue(dto, "relatedDiseases"));
            setFieldValue(existing, "relatedSymptoms", getFieldValue(dto, "relatedSymptoms"));
            setFieldValue(existing, "relatedProducts", getFieldValue(dto, "relatedProducts"));
            setFieldValue(existing, "customTags", getFieldValue(dto, "customTags"));
            setFieldValue(existing, "sortOrder", getFieldValue(dto, "sortOrder"));
            setFieldValue(existing, "isPinned", getFieldValue(dto, "isPinned"));
            setFieldValue(existing, "remark", getFieldValue(dto, "remark"));
            setFieldValue(existing, "operator", getFieldValue(dto, "operator"));

            configMapper.updateById(existing);
        } else {
            // 新增
            ContentApplicationConfig newConfig = new ContentApplicationConfig();
            BeanUtils.copyProperties(dto, newConfig);
            configMapper.insert(newConfig);
        }

        // 同步更新edu_content_application表
        updateEduContentApplication(dto);
    }

    /**
     * 更新edu_content_application表中的配置字段
     */
    private void updateEduContentApplication(ContentApplicationConfigDTO dto) {
        try {
            // 根据contentId查询edu_content_application记录
            String contentId = getFieldValue(dto, "contentId");

            // 使用Map构建查询条件避免Lambda表达式中的Lombok问题
            LambdaQueryWrapper<EduContentApplication> wrapper = new LambdaQueryWrapper<>();
            wrapper.apply("content_id = {0}", contentId);

            EduContentApplication contentApp = eduContentApplicationMapper.selectOne(wrapper);

            if (contentApp != null) {
                // 使用反射更新配置字段
                setFieldValue(contentApp, "applicationModule", getFieldValue(dto, "applicationModule"));
                setFieldValue(contentApp, "applicationProject", getFieldValue(dto, "applicationProject"));
                setFieldValue(contentApp, "channel", getFieldValue(dto, "channel"));
                setFieldValue(contentApp, "relatedDisease", getFieldValue(dto, "relatedDiseases"));
                setFieldValue(contentApp, "relatedSymptom", getFieldValue(dto, "relatedSymptoms"));
                setFieldValue(contentApp, "relatedProduct", getFieldValue(dto, "relatedProducts"));
                setFieldValue(contentApp, "pinStatus", getFieldValue(dto, "isPinned"));

                // 更新操作人作为作者（如果原来没有作者）
                String author = getFieldValue(contentApp, "author");
                String operator = getFieldValue(dto, "operator");
                if (author == null || author.isEmpty()) {
                    setFieldValue(contentApp, "author", operator);
                }

                eduContentApplicationMapper.updateById(contentApp);
            }
        } catch (Exception e) {
            throw new RuntimeException("更新内容应用配置失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByContentIdAndType(String contentId, String contentType) {
        configMapper.deleteByContentIdAndType(contentId, contentType);
    }

    // 反射工具方法（Lombok workaround）
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
