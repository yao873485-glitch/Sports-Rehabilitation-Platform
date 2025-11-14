package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备展示VO
 */
@Data
public class EquipmentVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 序号
     */
    private Integer serialNumber;

    /**
     * 区域
     */
    private String region;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备类型
     */
    private String equipmentType;

    /**
     * 设备编号
     */
    private String equipmentCode;

    /**
     * 状态
     */
    private String status;

    /**
     * 上次操作时间
     */
    private LocalDateTime lastOperationTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
}