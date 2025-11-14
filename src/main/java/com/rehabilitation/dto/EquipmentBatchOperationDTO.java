package com.rehabilitation.dto;

import lombok.Data;

import java.util.List;

/**
 * 设备批量操作DTO
 */
@Data
public class EquipmentBatchOperationDTO {

    /**
     * 设备ID列表
     */
    private List<Long> ids;

    /**
     * 操作类型：上架/下架
     */
    private String operationType;
}