package com.rehabilitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.PrescriptionExecutionQueryDTO;
import com.rehabilitation.dto.PrescriptionExecutionVO;
import com.rehabilitation.service.PrescriptionExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 处方执行管理控制器
 */
@Tag(name = "处方执行管理", description = "处方执行管理接口")
@RestController
@RequestMapping("/api/prescription-execution")
@RequiredArgsConstructor
public class PrescriptionExecutionController {

    private final PrescriptionExecutionService prescriptionExecutionService;

    /**
     * 分页查询处方执行列表
     */
    @Operation(summary = "分页查询处方执行列表")
    @GetMapping("/list")
    public Result<IPage<PrescriptionExecutionVO>> getPrescriptionExecutionPage(PrescriptionExecutionQueryDTO query) {
        IPage<PrescriptionExecutionVO> result = prescriptionExecutionService.getPrescriptionExecutionPage(query);
        return Result.success(result);
    }

    /**
     * 获取处方执行详情
     */
    @Operation(summary = "获取处方执行详情")
    @GetMapping("/{id}")
    public Result<PrescriptionExecutionVO> getPrescriptionExecutionById(@PathVariable Integer id) {
        PrescriptionExecutionVO result = prescriptionExecutionService.getPrescriptionExecutionById(id);
        return Result.success(result);
    }

    /**
     * 结束处方执行
     */
    @Operation(summary = "结束处方执行")
    @PutMapping("/{id}/end")
    public Result<Void> endPrescriptionExecution(@PathVariable Integer id) {
        prescriptionExecutionService.endPrescriptionExecution(id);
        return Result.success();
    }

    /**
     * 执行处方
     */
    @Operation(summary = "执行处方")
    @PutMapping("/{id}/execute")
    public Result<Void> executePrescription(@PathVariable Integer id) {
        prescriptionExecutionService.executePrescription(id);
        return Result.success();
    }

    /**
     * 获取所有病种类型
     */
    @Operation(summary = "获取所有病种类型")
    @GetMapping("/disease-types")
    public Result<List<String>> getAllDiseaseTypes() {
        List<String> result = prescriptionExecutionService.getAllDiseaseTypes();
        return Result.success(result);
    }

    /**
     * 获取所有机构列表
     */
    @Operation(summary = "获取所有机构列表")
    @GetMapping("/institutions")
    public Result<List<String>> getAllInstitutions() {
        List<String> result = prescriptionExecutionService.getAllInstitutions();
        return Result.success(result);
    }
}