package com.rehabilitation.dto;

import java.util.List;

/**
 * 设备批量操作DTO
 */
public class EquipmentBatchOperationDTO {

    /**
     * 设备ID列表
     */
    private List<Long> ids;

    /**
     * 操作类型：上架/下架
     */
    private String operationType;

    // Getter and Setter methods
    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}