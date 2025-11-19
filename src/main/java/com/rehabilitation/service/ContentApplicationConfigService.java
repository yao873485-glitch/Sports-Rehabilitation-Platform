package com.rehabilitation.service;

import com.rehabilitation.dto.ContentApplicationConfigDTO;
import com.rehabilitation.entity.ContentApplicationConfig;

/**
 * 内容应用配置Service接口
 */
public interface ContentApplicationConfigService {

    /**
     * 根据内容ID和类型查询配置
     */
    ContentApplicationConfig getByContentIdAndType(String contentId, String contentType);

    /**
     * 保存或更新配置
     */
    void saveOrUpdate(ContentApplicationConfigDTO dto);

    /**
     * 删除配置
     */
    void deleteByContentIdAndType(String contentId, String contentType);
}
