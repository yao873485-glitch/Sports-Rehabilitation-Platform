package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.EduContentQueryDTO;
import com.rehabilitation.dto.EduContentVO;
import com.rehabilitation.entity.EduContentRepository;

/**
 * 教育内容资源库Service接口
 */
public interface EduContentRepositoryService extends IService<EduContentRepository> {

    /**
     * 分页查询教育内容
     */
    PageResult<EduContentVO> getEduContentPage(EduContentQueryDTO queryDTO);

    /**
     * 根据ID获取教育内容详情
     */
    EduContentVO getEduContentById(Long id);

    /**
     * 根据contentId获取教育内容详情
     */
    EduContentVO getEduContentByContentId(String contentId);

    /**
     * 添加教育内容
     */
    boolean addEduContent(EduContentVO contentVO);

    /**
     * 更新教育内容
     */
    boolean updateEduContent(EduContentVO contentVO);

    /**
     * 删除教育内容
     */
    boolean deleteEduContent(Long id);
}