package com.rehabilitation.dto;

import lombok.Data;

import java.util.List;

/**
 * 运动处方批量操作DTO
 */
@Data
public class ExercisePrescriptionBatchOperationDTO {

    /**
     * 运动处方ID列表
     */
    private List<Long> ids;

    /**
     * 操作类型：上架/下架
     */
    private String operationType;
}