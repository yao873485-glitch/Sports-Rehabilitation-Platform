package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 患者信息查询DTO
 */
@Data
public class PatientInfoQueryDTO {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 姓名/电话
     */
    private String name;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}