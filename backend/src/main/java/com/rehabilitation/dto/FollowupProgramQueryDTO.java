package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 随访项目查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FollowupProgramQueryDTO extends PageQuery {

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 是否发布
     */
    private Integer isPublished;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;
}