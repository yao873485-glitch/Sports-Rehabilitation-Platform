package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rehabilitation.entity.ContentApplicationConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 内容应用配置Mapper接口
 */
@Mapper
public interface ContentApplicationConfigMapper extends BaseMapper<ContentApplicationConfig> {

    /**
     * 根据内容ID和类型查询配置
     */
    ContentApplicationConfig selectByContentIdAndType(@Param("contentId") String contentId,
                                                       @Param("contentType") String contentType);

    /**
     * 根据内容ID和类型删除配置
     */
    int deleteByContentIdAndType(@Param("contentId") String contentId,
                                  @Param("contentType") String contentType);
}
