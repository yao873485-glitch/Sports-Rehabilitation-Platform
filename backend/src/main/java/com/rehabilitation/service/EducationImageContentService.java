package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.dto.EducationImageContentDTO;
import com.rehabilitation.entity.EducationImageContent;

/**
 * 医教图文内容服务接口
 */
public interface EducationImageContentService extends IService<EducationImageContent> {

    /**
     * 保存图文内容
     * @param dto 图文内容DTO
     * @return 保存后的实体
     */
    EducationImageContent saveContent(EducationImageContentDTO dto);

    /**
     * 分页查询图文内容列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param contentTitle 内容标题
     * @param author 作者
     * @param categorySection 所属板块
     * @param contentStatus 内容状态
     * @param publishStartTime 发布开始时间
     * @param publishEndTime 发布结束时间
     * @return 分页结果
     */
    Page<EducationImageContent> getContentPage(Integer pageNum, Integer pageSize,
                                                 String contentTitle, String author,
                                                 String categorySection, String contentStatus,
                                                 String publishStartTime, String publishEndTime);
}
