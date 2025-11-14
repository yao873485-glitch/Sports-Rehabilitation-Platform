package com.rehabilitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.ExercisePrescriptionBatchOperationDTO;
import com.rehabilitation.dto.ExercisePrescriptionQueryDTO;
import com.rehabilitation.dto.ExercisePrescriptionVO;
import com.rehabilitation.entity.ExercisePrescription;
import com.rehabilitation.service.ExercisePrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运动处方管理控制器
 */
@Tag(name = "运动处方管理", description = "运动处方管理接口")
@RestController
@RequestMapping("/api/exercise-prescription")
@RequiredArgsConstructor
public class ExercisePrescriptionController {

    private final ExercisePrescriptionService exercisePrescriptionService;

    /**
     * 分页查询运动处方列表
     */
    @Operation(summary = "分页查询运动处方列表")
    @GetMapping("/list")
    public Result<IPage<ExercisePrescriptionVO>> getExercisePrescriptionPage(ExercisePrescriptionQueryDTO query) {
        IPage<ExercisePrescriptionVO> result = exercisePrescriptionService.getExercisePrescriptionPage(query);
        return Result.success(result);
    }

    /**
     * 获取运动处方详情
     */
    @Operation(summary = "获取运动处方详情")
    @GetMapping("/{id}")
    public Result<ExercisePrescriptionVO> getExercisePrescriptionById(@PathVariable Long id) {
        ExercisePrescriptionVO result = exercisePrescriptionService.getExercisePrescriptionById(id);
        return Result.success(result);
    }

    /**
     * 新增运动处方
     */
    @Operation(summary = "新增运动处方")
    @PostMapping
    public Result<Void> addExercisePrescription(@RequestBody ExercisePrescription exercisePrescription) {
        exercisePrescriptionService.addExercisePrescription(exercisePrescription);
        return Result.success();
    }

    /**
     * 更新运动处方
     */
    @Operation(summary = "更新运动处方")
    @PutMapping("/{id}")
    public Result<Void> updateExercisePrescription(@PathVariable Long id, @RequestBody ExercisePrescription exercisePrescription) {
        exercisePrescription.setId(id);
        exercisePrescriptionService.updateExercisePrescription(exercisePrescription);
        return Result.success();
    }

    /**
     * 获取所有病种类型
     */
    @Operation(summary = "获取所有病种类型")
    @GetMapping("/disease-types")
    public Result<List<String>> getAllDiseaseTypes() {
        List<String> result = exercisePrescriptionService.getAllDiseaseTypes();
        return Result.success(result);
    }

    /**
     * 批量操作运动处方
     */
    @Operation(summary = "批量操作运动处方")
    @PostMapping("/batch-operation")
    public Result<Void> batchOperation(@RequestBody ExercisePrescriptionBatchOperationDTO batchOperation) {
        exercisePrescriptionService.batchOperation(batchOperation);
        return Result.success();
    }

    /**
     * 导入运动处方数据
     */
    @Operation(summary = "导入运动处方数据")
    @PostMapping("/import")
    public Result<Void> importExercisePrescription(@RequestBody List<ExercisePrescription> exercisePrescriptionList) {
        exercisePrescriptionService.importExercisePrescription(exercisePrescriptionList);
        return Result.success();
    }
}