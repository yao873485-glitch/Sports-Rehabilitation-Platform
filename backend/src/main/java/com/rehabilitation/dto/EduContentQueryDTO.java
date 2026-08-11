package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教育内容查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EduContentQueryDTO extends PageQuery {

    /**
     * 发布开始时间
     */
    private String publishStartTime;

    /**
     * 发布结束时间
     */
    private String publishEndTime;

    /**
     * 内容标题
     */
    private String contentTitle;

    /**
     * 作者
     */
    private String author;

    /**
     * 所属板块
     */
    private String categorySection;

    /**
     * 内容状态
     */
    private String contentStatus;
}