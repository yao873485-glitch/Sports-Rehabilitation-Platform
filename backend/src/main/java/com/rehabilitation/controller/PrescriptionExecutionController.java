package com.rehabilitation.controller;

import com.rehabilitation.common.Result;
import com.rehabilitation.dto.PrescriptionExecutionDTO;
import com.rehabilitation.service.PrescriptionExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 处方执行控制器
 */
@Tag(name = "处方执行管理")
@RestController
@RequestMapping("/api/prescription-execution")
public class PrescriptionExecutionController {

    @Autowired
    private PrescriptionExecutionService prescriptionExecutionService;

    /**
     * 执行处方（单个或批量）
     */
    @Operation(summary = "执行处方")
    @PostMapping("/execute")
    public Result<Void> executePrescription(@RequestBody PrescriptionExecutionDTO dto) {
        prescriptionExecutionService.executePrescription(dto);
        return Result.success();
    }
}
