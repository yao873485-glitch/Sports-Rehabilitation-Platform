package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.EduContentApplicationQueryDTO;
import com.rehabilitation.dto.EduContentApplicationVO;
import com.rehabilitation.entity.EduContentApplication;

/**
 * 内容应用管理Service接口
 * 注意：数据现在通过联合查询从图文素材表和视频素材表获取，不再维护edu_content_application表
 */
public interface EduContentApplicationService extends IService<EduContentApplication> {

    /**
     * 分页查询内容应用管理列表（联合图文和视频素材表）
     */
    PageResult<EduContentApplicationVO> getContentApplicationPage(EduContentApplicationQueryDTO queryDTO);

    /**
     * 根据ID获取内容应用配置详情（已废弃，保留用于兼容性）
     */
    EduContentApplicationVO getContentApplicationById(Long id);
}
