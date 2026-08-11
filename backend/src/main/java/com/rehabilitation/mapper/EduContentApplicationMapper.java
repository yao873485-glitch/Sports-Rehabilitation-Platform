package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.EduContentApplicationQueryDTO;
import com.rehabilitation.dto.EduContentApplicationVO;
import com.rehabilitation.entity.EduContentApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 内容应用管理Mapper接口
 */
@Mapper
public interface EduContentApplicationMapper extends BaseMapper<EduContentApplication> {

    /**
     * 分页查询内容应用管理列表（联合图文和视频素材表）
     */
    List<EduContentApplicationVO> selectContentApplicationPage(
            Page<EduContentApplicationVO> page,
            @Param("contentTitle") String contentTitle,
            @Param("author") String author,
            @Param("resourceType") String resourceType,
            @Param("categorySection") String categorySection,
            @Param("status") String status,
            @Param("channel") String channel,
            @Param("pinStatus") String pinStatus,
            @Param("publishStartTime") String publishStartTime,
            @Param("publishEndTime") String publishEndTime
    );
}
