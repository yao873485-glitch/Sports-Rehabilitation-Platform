package com.rehabilitation.dto;

import lombok.Data;

/**
 * 随访项目创建DTO
 */
@Data
public class FollowupProgramCreateDTO {

    /**
     * 项目编号（随机生成）
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目介绍
     */
    private String projectDescription;

    /**
     * 绑定随访计划
     */
    private String linkedFollowupPlan;

    /**
     * 是否发布
     */
    private Integer isPublished;

    /**
     * 操作人
     */
    private String operator;
}
