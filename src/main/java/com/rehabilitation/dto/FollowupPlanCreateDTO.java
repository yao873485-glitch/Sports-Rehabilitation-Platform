package com.rehabilitation.dto;

import lombok.Data;

/**
 * 随访计划创建DTO
 */
@Data
public class FollowupPlanCreateDTO {

    /**
     * 计划名称（从模板名称生成）
     */
    private String planName;

    /**
     * 关联项目ID
     */
    private Long linkedProjectId;

    /**
     * 随访项目名称
     */
    private String linkedProject;

    /**
     * 版本号
     */
    private String versionNumber;

    /**
     * 状态
     */
    private String status;

    /**
     * 版本备注
     */
    private String versionRemark;

    /**
     * 模板名称（用于前端传递）
     */
    private String templateName;
}
