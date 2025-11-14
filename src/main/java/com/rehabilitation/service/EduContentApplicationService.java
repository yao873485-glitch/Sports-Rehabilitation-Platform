package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.EduContentApplicationQueryDTO;
import com.rehabilitation.dto.EduContentApplicationVO;
import com.rehabilitation.entity.EduContentApplication;

/**
 * 内容应用管理Service接口
 */
public interface EduContentApplicationService extends IService<EduContentApplication> {

    /**
     * 分页查询内容应用管理列表
     */
    PageResult<EduContentApplicationVO> getContentApplicationPage(EduContentApplicationQueryDTO queryDTO);

    /**
     * 根据ID获取内容应用配置详情
     */
    EduContentApplicationVO getContentApplicationById(Long id);

    /**
     * 更新内容应用配置
     */
    boolean updateContentApplication(EduContentApplicationVO vo);

    /**
     * 更新内容状态
     */
    boolean updateContentStatus(Long id, String status);

    /**
     * 删除内容应用记录
     */
    boolean deleteContentApplication(Long id);

    /**
     * 创建内容应用记录
     */
    boolean createContentApplication(String contentId, String contentType, String contentTitle,
                                     String contentDescription, String categorySection, String author,
                                     Integer viewCount, Integer likeCount);
}
