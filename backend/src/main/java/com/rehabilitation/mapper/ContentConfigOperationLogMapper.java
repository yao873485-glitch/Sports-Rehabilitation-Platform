package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rehabilitation.entity.ContentConfigOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 内容配置操作日志Mapper接口
 */
@Mapper
public interface ContentConfigOperationLogMapper extends BaseMapper<ContentConfigOperationLog> {

    /**
     * 根据内容ID和类型查询操作日志
     */
    List<ContentConfigOperationLog> selectByContentIdAndType(
            @Param("contentId") String contentId,
            @Param("contentType") String contentType
    );
}
