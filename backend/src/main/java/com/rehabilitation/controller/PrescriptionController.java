package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.PrescriptionQueryDTO;
import com.rehabilitation.dto.PrescriptionVO;
import com.rehabilitation.dto.PrescriptionDetailVO;
import com.rehabilitation.dto.PrescriptionSaveDTO;
import com.rehabilitation.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 运动处方控制器
 */
@Tag(name = "运动处方管理", description = "运动处方管理接口")
@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

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
     * 获取运动处方详情（包含患者信息和运动项目列表）
     */
    @Operation(summary = "获取运动处方详情（包含患者信息和运动项目列表）")
    @GetMapping("/{id}/detail")
    public Result<PrescriptionDetailVO> getPrescriptionDetailWithExercises(@PathVariable Integer id) {
        PrescriptionDetailVO result = prescriptionService.getPrescriptionDetailWithExercises(id);
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

    /**
     * 保存运动处方
     */
    @Operation(summary = "保存运动处方")
    @PostMapping("/save")
    public Result<Void> savePrescription(@RequestBody PrescriptionSaveDTO dto) {
        prescriptionService.savePrescription(dto);
        return Result.success();
    }

    /**
     * 提交运动处方
     */
    @Operation(summary = "提交运动处方")
    @PostMapping("/submit")
    public Result<Void> submitPrescription(@RequestBody PrescriptionSaveDTO dto) {
        prescriptionService.submitPrescription(dto);
        return Result.success();
    }
}
