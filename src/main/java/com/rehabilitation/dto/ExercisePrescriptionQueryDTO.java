package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 运动处方查询条件DTO
 */
@Data
public class ExercisePrescriptionQueryDTO {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 病种类型筛选
     */
    private String diseaseType;

    /**
     * 状态筛选
     */
    private String status;

    /**
     * 运动方式搜索
     */
    private String exerciseMethod;

    /**
     * 运动类型筛选
     */
    private String exerciseType;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}