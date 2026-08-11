package com.rehabilitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.AssessmentPlanDetailVO;
import com.rehabilitation.dto.PatientAssessmentPlanQueryDTO;
import com.rehabilitation.dto.PatientAssessmentPlanVO;
import com.rehabilitation.entity.PatientAssessmentPlan;
import com.rehabilitation.constant.CacheNames;
import com.rehabilitation.service.PatientAssessmentPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者评定方案管理控制器
 */
@Tag(name = "患者评定方案管理", description = "患者评定方案管理接口")
@RestController
@RequestMapping("/patient-assessment-plan")
public class PatientAssessmentPlanController {

    private final PatientAssessmentPlanService patientAssessmentPlanService;

    @Autowired
    public PatientAssessmentPlanController(PatientAssessmentPlanService patientAssessmentPlanService) {
        this.patientAssessmentPlanService = patientAssessmentPlanService;
    }

    /**
     * 分页查询患者评定方案列表
     */
    @Operation(summary = "分页查询患者评定方案列表")
    @GetMapping("/list")
    public Result<IPage<PatientAssessmentPlanVO>> getPatientAssessmentPlanPage(PatientAssessmentPlanQueryDTO query) {
        IPage<PatientAssessmentPlanVO> result = patientAssessmentPlanService.getPatientAssessmentPlanPage(query);
        return Result.success(result);
    }

    /**
     * 获取患者评定方案详情
     */
    @Operation(summary = "获取患者评定方案详情")
    @GetMapping("/{id}")
    public Result<PatientAssessmentPlanVO> getPatientAssessmentPlanById(@PathVariable Integer id) {
        PatientAssessmentPlanVO result = patientAssessmentPlanService.getPatientAssessmentPlanById(id);
        return Result.success(result);
    }

    /**
     * 获取评定计划查看详情（包含患者信息）
     */
    @Operation(summary = "获取评定计划查看详情（包含患者信息）")
    @GetMapping("/{id}/view")
    public Result<AssessmentPlanDetailVO> getAssessmentPlanDetailById(@PathVariable Integer id) {
        AssessmentPlanDetailVO result = patientAssessmentPlanService.getAssessmentPlanDetailById(id);
        return Result.success(result);
    }

    /**
     * 新增患者评定方案
     */
    @Operation(summary = "新增患者评定方案")
    @PostMapping
    public Result<Void> addPatientAssessmentPlan(@RequestBody PatientAssessmentPlan patientAssessmentPlan) {
        patientAssessmentPlanService.addPatientAssessmentPlan(patientAssessmentPlan);
        return Result.success();
    }

    /**
     * 更新患者评定方案
     */
    @Operation(summary = "更新患者评定方案")
    @PutMapping("/{id}")
    public Result<Void> updatePatientAssessmentPlan(@PathVariable Integer id, @RequestBody PatientAssessmentPlan patientAssessmentPlan) {
        patientAssessmentPlan.setId(id);
        patientAssessmentPlanService.updatePatientAssessmentPlan(patientAssessmentPlan);
        return Result.success();
    }

    /**
     * 结束患者评定方案
     */
    @Operation(summary = "结束患者评定方案")
    @PutMapping("/{id}/end")
    public Result<Void> endPatientAssessmentPlan(@PathVariable Integer id) {
        patientAssessmentPlanService.endPatientAssessmentPlan(id);
        return Result.success();
    }

    /**
     * 删除患者评定方案
     */
    @Operation(summary = "删除患者评定方案")
    @DeleteMapping("/{id}")
    public Result<Void> deletePatientAssessmentPlan(@PathVariable Integer id) {
        patientAssessmentPlanService.deletePatientAssessmentPlan(id);
        return Result.success();
    }

    /**
     * 获取所有病种类型
     */
    @Operation(summary = "获取所有病种类型")
    @Cacheable(cacheNames = CacheNames.PATIENT_ASSESSMENT_DISEASE_TYPES)
    @GetMapping("/disease-types")
    public Result<List<String>> getAllDiseaseTypes() {
        List<String> result = patientAssessmentPlanService.getAllDiseaseTypes();
        return Result.success(result);
    }

    /**
     * 获取所有机构列表
     */
    @Operation(summary = "获取所有机构列表")
    @GetMapping("/institutions")
    public Result<List<String>> getAllInstitutions() {
        List<String> result = patientAssessmentPlanService.getAllInstitutions();
        return Result.success(result);
    }
}
