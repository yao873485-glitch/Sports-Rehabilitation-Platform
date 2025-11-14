package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.PrescriptionQueryDTO;
import com.rehabilitation.dto.PrescriptionVO;
import com.rehabilitation.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 运动处方控制器
 */
@Tag(name = "运动处方管理", description = "运动处方管理接口")
@RestController
@RequestMapping("/prescription")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    /**
     * 分页查询运动处方列表
     */
    @Operation(summary = "分页查询运动处方列表")
    @GetMapping("/list")
    public Result<PageResult<PrescriptionVO>> getPrescriptionPage(PrescriptionQueryDTO query) {
        PageResult<PrescriptionVO> result = prescriptionService.getPrescriptionPage(query);
        return Result.success(result);
    }

    /**
     * 获取运动处方详情
     */
    @Operation(summary = "获取运动处方详情")
    @GetMapping("/{id}")
    public Result<PrescriptionVO> getPrescriptionDetail(@PathVariable Integer id) {
        PrescriptionVO result = prescriptionService.getPrescriptionDetail(id);
        return Result.success(result);
    }

    /**
     * 结束运动处方
     */
    @Operation(summary = "结束运动处方")
    @PutMapping("/{id}/end")
    public Result<Void> endPrescription(@PathVariable Integer id) {
        prescriptionService.endPrescription(id);
        return Result.success();
    }

    /**
     * 执行运动处方
     */
    @Operation(summary = "执行运动处方")
    @PutMapping("/{id}/execute")
    public Result<Void> executePrescription(@PathVariable Integer id) {
        prescriptionService.executePrescription(id);
        return Result.success();
    }
}
