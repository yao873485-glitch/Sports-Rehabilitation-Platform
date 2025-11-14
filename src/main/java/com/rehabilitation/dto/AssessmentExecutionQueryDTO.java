package com.rehabilitation.dto;

import lombok.Data;

/**
 * 评定执行查询DTO
 */
@Data
public class AssessmentExecutionQueryDTO {

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 评定状态
     */
    private String status;

    /**
     * 入组机构
     */
    private String enrollmentInstitution;

    /**
     * 执行机构
     */
    private String executionInstitution;

    /**
     * 搜索关键词（患者姓名、电话或档案号）
     */
    private String searchKeyword;
}