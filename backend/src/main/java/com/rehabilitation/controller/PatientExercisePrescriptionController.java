package com.rehabilitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.PatientExercisePrescriptionQueryDTO;
import com.rehabilitation.dto.PatientExercisePrescriptionVO;
import com.rehabilitation.dto.PrescriptionSaveDTO;
import com.rehabilitation.entity.PatientExercisePrescription;
import com.rehabilitation.constant.CacheNames;
import com.rehabilitation.service.PatientExercisePrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者运动处方管理控制器
 */
@Tag(name = "患者运动处方管理", description = "患者运动处方管理接口")
@RestController
@RequestMapping("/api/patient-exercise-prescription")
public class PatientExercisePrescriptionController {

    @Autowired
    private PatientExercisePrescriptionService patientExercisePrescriptionService;

    /**
     * 分页查询患者运动处方列表
     */
    @Operation(summary = "分页查询患者运动处方列表")
    @GetMapping("/list")
    public Result<IPage<PatientExercisePrescriptionVO>> getPatientExercisePrescriptionPage(PatientExercisePrescriptionQueryDTO query) {
        IPage<PatientExercisePrescriptionVO> result = patientExercisePrescriptionService.getPatientExercisePrescriptionPage(query);
        return Result.success(result);
    }

    /**
     * 获取患者运动处方详情
     */
    @Operation(summary = "获取患者运动处方详情")
    @GetMapping("/{id}")
    public Result<PatientExercisePrescriptionVO> getPatientExercisePrescriptionById(@PathVariable Long id) {
        PatientExercisePrescriptionVO result = patientExercisePrescriptionService.getPatientExercisePrescriptionById(id);
        return Result.success(result);
    }

    /**
     * 新增患者运动处方
     */
    @Operation(summary = "新增患者运动处方")
    @PostMapping
    public Result<Void> addPatientExercisePrescription(@RequestBody PatientExercisePrescription patientExercisePrescription) {
        patientExercisePrescriptionService.addPatientExercisePrescription(patientExercisePrescription);
        return Result.success();
    }

    /**
     * 更新患者运动处方
     */
    @Operation(summary = "更新患者运动处方")
    @PutMapping("/{id}")
    public Result<Void> updatePatientExercisePrescription(@PathVariable Long id, @RequestBody PatientExercisePrescription patientExercisePrescription) {
        patientExercisePrescription.setId(id);
        patientExercisePrescriptionService.updatePatientExercisePrescription(patientExercisePrescription);
        return Result.success();
    }

    /**
     * 结束患者运动处方
     */
    @Operation(summary = "结束患者运动处方")
    @PutMapping("/{id}/end")
    public Result<Void> endPatientExercisePrescription(@PathVariable Long id) {
        patientExercisePrescriptionService.endPatientExercisePrescription(id);
        return Result.success();
    }

    /**
     * 执行患者运动处方
     */
    @Operation(summary = "执行患者运动处方")
    @PutMapping("/{id}/execute")
    public Result<Void> executePatientExercisePrescription(@PathVariable Long id) {
        patientExercisePrescriptionService.executePatientExercisePrescription(id);
        return Result.success();
    }

    /**
     * 获取所有病种类型
     */
    @Operation(summary = "获取所有病种类型")
    @Cacheable(cacheNames = CacheNames.PATIENT_EXERCISE_DISEASE_TYPES)
    @GetMapping("/disease-types")
    public Result<List<String>> getAllDiseaseTypes() {
        List<String> result = patientExercisePrescriptionService.getAllDiseaseTypes();
        return Result.success(result);
    }

    /**
     * 获取所有机构列表
     */
    @Operation(summary = "获取所有机构列表")
    @Cacheable(cacheNames = CacheNames.PATIENT_EXERCISE_INSTITUTIONS)
    @GetMapping("/institutions")
    public Result<List<String>> getAllInstitutions() {
        List<String> result = patientExercisePrescriptionService.getAllInstitutions();
        return Result.success(result);
    }

    /**
     * 保存运动处方（从方案配置页面）
     */
    @Operation(summary = "保存运动处方")
    @PostMapping("/save-prescription")
    public Result<Long> savePrescription(@RequestBody PrescriptionSaveDTO prescriptionSaveDTO) {
        Long prescriptionId = patientExercisePrescriptionService.savePrescription(prescriptionSaveDTO);
        return Result.success(prescriptionId);
    }
}
