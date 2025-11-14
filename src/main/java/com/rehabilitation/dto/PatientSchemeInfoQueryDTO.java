package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 患者方案信息查询DTO
 */
@Data
public class PatientSchemeInfoQueryDTO {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 搜索关键字（患者姓名/手机号/档案号）
     */
    private String searchKeyword;

    /**
     * 入组机构
     */
    private String orgName;

    /**
     * 方案状态：1-执行中，2-已结束
     */
    private Integer status;
}