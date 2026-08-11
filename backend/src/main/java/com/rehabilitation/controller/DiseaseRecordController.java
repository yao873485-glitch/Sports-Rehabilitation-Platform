package com.rehabilitation.controller;

import com.rehabilitation.common.Result;
import com.rehabilitation.constant.CacheNames;
import com.rehabilitation.entity.DiseaseRecord;
import com.rehabilitation.service.DiseaseRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

/**
 * 专病档案管理控制器
 */
@Tag(name = "专病档案管理")
@RestController
@RequestMapping("/api/disease-record")
public class DiseaseRecordController {

    @Autowired
    private DiseaseRecordService diseaseRecordService;

    /**
     * 保存或更新专病档案
     */
    @Operation(summary = "保存或更新专病档案")
    @CacheEvict(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#diseaseRecord.patientId", condition = "#diseaseRecord != null && #diseaseRecord.patientId != null")
    @PostMapping("/save")
    public Result<DiseaseRecord> saveOrUpdate(@RequestBody DiseaseRecord diseaseRecord) {
        try {
            DiseaseRecord saved = diseaseRecordService.saveOrUpdate(diseaseRecord);
            return Result.success(saved);
        } catch (Exception e) {
            return Result.error("保存专病档案失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID查询专病档案
     */
    @Operation(summary = "根据患者ID查询专病档案")
    @GetMapping("/{patientId}")
    public Result<DiseaseRecord> getByPatientId(
            @Parameter(description = "患者ID") @PathVariable Integer patientId) {
        try {
            DiseaseRecord diseaseRecord = diseaseRecordService.getByPatientId(patientId);
            return Result.success(diseaseRecord);
        } catch (Exception e) {
            return Result.error("查询专病档案失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID删除专病档案
     */
    @Operation(summary = "根据患者ID删除专病档案")
    @CacheEvict(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#patientId")
    @DeleteMapping("/{patientId}")
    public Result<Boolean> deleteByPatientId(
            @Parameter(description = "患者ID") @PathVariable Integer patientId) {
        try {
            boolean success = diseaseRecordService.deleteByPatientId(patientId);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("删除失败，未找到该患者的专病档案");
            }
        } catch (Exception e) {
            return Result.error("删除专病档案失败: " + e.getMessage());
        }
    }
}
