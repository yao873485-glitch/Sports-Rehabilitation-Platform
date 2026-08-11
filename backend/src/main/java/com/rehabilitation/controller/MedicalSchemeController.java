package com.rehabilitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.AssessmentPlanSaveDTO;
import com.rehabilitation.dto.MedicalSchemeQueryDTO;
import com.rehabilitation.dto.MedicalSchemeVO;
import com.rehabilitation.dto.SchemeCreateDTO;
import com.rehabilitation.service.MedicalSchemeService;
import com.rehabilitation.service.PatientAssessmentPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医疗方案控制器
 */
@Tag(name = "医疗方案管理", description = "医疗方案管理接口")
@RestController
@RequestMapping("/api/medical-scheme")
public class MedicalSchemeController {

    @Autowired
    private MedicalSchemeService medicalSchemeService;

    @Autowired
    private PatientAssessmentPlanService patientAssessmentPlanService;

    /**
     * 分页查询医疗方案列表
     */
    @Operation(summary = "分页查询医疗方案列表")
    @GetMapping("/list")
    public Result<IPage<MedicalSchemeVO>> getMedicalSchemePage(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime,
            @RequestParam(required = false) String diseaseType,
            @RequestParam(required = false) String doctorName,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        MedicalSchemeQueryDTO query = new MedicalSchemeQueryDTO();
        query.setPageNum(pageNum != null ? pageNum : 1);
        query.setPageSize(pageSize != null ? pageSize : 10);
        query.setStartTime(startTime);
        query.setEndTime(endTime);
        query.setDiseaseType(diseaseType);
        query.setDoctorName(doctorName);
        query.setSearchKeyword(searchKeyword);
        query.setStatus(status);
        query.setStartDate(startDate);
        query.setEndDate(endDate);

        IPage<MedicalSchemeVO> result = medicalSchemeService.getMedicalSchemePage(query);
        return Result.success(result);
    }

    /**
     * 获取医疗方案详情
     */
    @Operation(summary = "获取医疗方案详情")
    @GetMapping("/{id}")
    public Result<MedicalSchemeVO> getMedicalSchemeDetail(@PathVariable Long id) {
        MedicalSchemeVO result = medicalSchemeService.getMedicalSchemeDetail(id);
        return Result.success(result);
    }

    /**
     * 结束医疗方案
     */
    @Operation(summary = "结束医疗方案")
    @PutMapping("/{id}/end")
    public Result<Boolean> endMedicalScheme(@PathVariable Long id, @RequestBody(required = false) java.util.Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        boolean result = medicalSchemeService.endMedicalScheme(id, reason);
        return Result.success(result);
    }

    /**
     * 检查患者是否已有执行中的方案
     */
    @Operation(summary = "检查患者是否已有执行中的方案（通过档案号）")
    @GetMapping("/check-patient")
    public Result<Boolean> checkPatientHasActiveScheme(@RequestParam String medicalRecordNo) {
        boolean hasScheme = medicalSchemeService.checkPatientHasActiveScheme(medicalRecordNo);
        return Result.success(hasScheme);
    }

    /**
     * 创建方案
     */
    @Operation(summary = "创建方案")
    @PostMapping("/create")
    public Result<String> createScheme(@RequestBody SchemeCreateDTO schemeCreateDTO) {
        String schemeId = medicalSchemeService.createScheme(schemeCreateDTO);
        return Result.success(schemeId);
    }

    /**
     * 保存评定计划
     */
    @Operation(summary = "保存评定计划")
    @PostMapping("/save-assessment")
    public Result<Long> saveAssessment(@RequestBody AssessmentPlanSaveDTO assessmentPlanSaveDTO) {
        Long assessmentPlanId = patientAssessmentPlanService.saveAssessmentPlan(assessmentPlanSaveDTO);
        return Result.success(assessmentPlanId);
    }
}