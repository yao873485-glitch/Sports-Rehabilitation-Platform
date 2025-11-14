package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备查询条件DTO
 */
@Data
public class EquipmentQueryDTO {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 设备名称搜索
     */
    private String equipmentName;

    /**
     * 设备类型筛选
     */
    private String equipmentType;

    /**
     * 设备状态筛选
     */
    private String status;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}