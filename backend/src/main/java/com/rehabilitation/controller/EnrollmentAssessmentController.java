package com.rehabilitation.controller;

import com.rehabilitation.common.Result;
import com.rehabilitation.constant.CacheNames;
import com.rehabilitation.entity.EnrollmentAssessment;
import com.rehabilitation.service.EnrollmentAssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

/**
 * 入组评估控制器
 */
@Tag(name = "入组评估管理")
@RestController
@RequestMapping("/api/enrollment-assessment")
public class EnrollmentAssessmentController {

    @Autowired
    private EnrollmentAssessmentService enrollmentAssessmentService;

    /**
     * 保存或更新入组评估
     */
    @Operation(summary = "保存或更新入组评估")
    @CacheEvict(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#enrollmentAssessment.patientId", condition = "#enrollmentAssessment != null && #enrollmentAssessment.patientId != null")
    @PostMapping("/save")
    public Result<EnrollmentAssessment> saveOrUpdate(@RequestBody EnrollmentAssessment enrollmentAssessment) {
        try {
            EnrollmentAssessment saved = enrollmentAssessmentService.saveOrUpdate(enrollmentAssessment);
            return Result.success(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("保存入组评估失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID查询入组评估
     */
    @Operation(summary = "根据患者ID查询入组评估")
    @GetMapping("/{patientId}")
    public Result<EnrollmentAssessment> getByPatientId(@PathVariable Integer patientId) {
        try {
            EnrollmentAssessment assessment = enrollmentAssessmentService.getByPatientId(patientId);
            return Result.success(assessment);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询入组评估失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID删除入组评估
     */
    @Operation(summary = "根据患者ID删除入组评估")
    @CacheEvict(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#patientId")
    @DeleteMapping("/{patientId}")
    public Result<Boolean> deleteByPatientId(@PathVariable Integer patientId) {
        try {
            boolean deleted = enrollmentAssessmentService.deleteByPatientId(patientId);
            if (deleted) {
                return Result.success(true);
            } else {
                return Result.error("未找到要删除的入组评估");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除入组评估失败: " + e.getMessage());
        }
    }
}
