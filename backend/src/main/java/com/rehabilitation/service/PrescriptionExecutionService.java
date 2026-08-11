package com.rehabilitation.service;

import com.rehabilitation.dto.PrescriptionExecutionDTO;

/**
 * 处方执行服务接口
 */
public interface PrescriptionExecutionService {

    /**
     * 执行处方（单个或批量）
     *
     * @param dto 执行信息
     */
    void executePrescription(PrescriptionExecutionDTO dto);
}
