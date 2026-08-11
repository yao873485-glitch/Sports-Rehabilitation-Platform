package com.rehabilitation.controller;

import com.rehabilitation.common.Result;
import com.rehabilitation.constant.CacheNames;
import com.rehabilitation.entity.HealthRecord;
import com.rehabilitation.service.HealthRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

/**
 * 健康档案管理控制器
 */
@Tag(name = "健康档案管理")
@RestController
@RequestMapping("/api/health-record")
public class HealthRecordController {

    @Autowired
    private HealthRecordService healthRecordService;

    /**
     * 保存或更新健康档案
     */
    @Operation(summary = "保存或更新健康档案")
    @CacheEvict(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#healthRecord.patientId", condition = "#healthRecord != null && #healthRecord.patientId != null")
    @PostMapping("/save")
    public Result<HealthRecord> saveOrUpdate(@RequestBody HealthRecord healthRecord) {
        try {
            HealthRecord saved = healthRecordService.saveOrUpdate(healthRecord);
            return Result.success(saved);
        } catch (Exception e) {
            return Result.error("保存健康档案失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID查询健康档案
     */
    @Operation(summary = "根据患者ID查询健康档案")
    @GetMapping("/{patientId}")
    public Result<HealthRecord> getByPatientId(
            @Parameter(description = "患者ID") @PathVariable Integer patientId) {
        try {
            HealthRecord healthRecord = healthRecordService.getByPatientId(patientId);
            return Result.success(healthRecord);
        } catch (Exception e) {
            return Result.error("查询健康档案失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID删除健康档案
     */
    @Operation(summary = "根据患者ID删除健康档案")
    @CacheEvict(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#patientId")
    @DeleteMapping("/{patientId}")
    public Result<Boolean> deleteByPatientId(
            @Parameter(description = "患者ID") @PathVariable Integer patientId) {
        try {
            boolean success = healthRecordService.deleteByPatientId(patientId);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("删除失败，未找到该患者的健康档案");
            }
        } catch (Exception e) {
            return Result.error("删除健康档案失败: " + e.getMessage());
        }
    }
}
