package com.rehabilitation.controller;

import com.rehabilitation.common.Result;
import com.rehabilitation.entity.SchemeCycleConfig;
import com.rehabilitation.service.SchemeCycleConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 方案周期配置控制器
 */
@Tag(name = "方案周期配置管理")
@RestController
@RequestMapping("/api/scheme-cycle")
public class SchemeCycleConfigController {

    @Autowired
    private SchemeCycleConfigService schemeCycleConfigService;

    /**
     * 保存或更新方案周期配置
     */
    @Operation(summary = "保存或更新方案周期配置")
    @PostMapping("/save")
    public Result<SchemeCycleConfig> saveOrUpdate(@RequestBody SchemeCycleConfig config) {
        try {
            SchemeCycleConfig saved = schemeCycleConfigService.saveOrUpdate(config);
            return Result.success(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("保存方案周期配置失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID查询方案周期配置
     */
    @Operation(summary = "根据患者ID查询方案周期配置")
    @GetMapping("/patient/{patientId}")
    public Result<SchemeCycleConfig> getByPatientId(@PathVariable Integer patientId) {
        try {
            SchemeCycleConfig config = schemeCycleConfigService.getByPatientId(patientId);
            return Result.success(config);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询方案周期配置失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID和方案ID查询方案周期配置
     */
    @Operation(summary = "根据患者ID和方案ID查询方案周期配置")
    @GetMapping("/patient/{patientId}/scheme/{schemeId}")
    public Result<SchemeCycleConfig> getByPatientIdAndSchemeId(
            @PathVariable Integer patientId,
            @PathVariable String schemeId) {
        try {
            SchemeCycleConfig config = schemeCycleConfigService.getByPatientIdAndSchemeId(patientId, schemeId);
            return Result.success(config);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询方案周期配置失败: " + e.getMessage());
        }
    }

    /**
     * 根据方案ID查询方案周期配置
     */
    @Operation(summary = "根据方案ID查询方案周期配置")
    @GetMapping("/scheme/{schemeId}")
    public Result<SchemeCycleConfig> getBySchemeId(@PathVariable String schemeId) {
        try {
            SchemeCycleConfig config = schemeCycleConfigService.getBySchemeId(schemeId);
            return Result.success(config);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询方案周期配置失败: " + e.getMessage());
        }
    }

    /**
     * 根据患者ID删除方案周期配置
     */
    @Operation(summary = "根据患者ID删除方案周期配置")
    @DeleteMapping("/patient/{patientId}")
    public Result<Boolean> deleteByPatientId(@PathVariable Integer patientId) {
        try {
            boolean deleted = schemeCycleConfigService.deleteByPatientId(patientId);
            if (deleted) {
                return Result.success(true);
            } else {
                return Result.error("未找到要删除的方案周期配置");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除方案周期配置失败: " + e.getMessage());
        }
    }
}
