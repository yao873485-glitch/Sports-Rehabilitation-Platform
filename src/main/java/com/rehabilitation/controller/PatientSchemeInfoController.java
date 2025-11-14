package com.rehabilitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.PatientSchemeInfoQueryDTO;
import com.rehabilitation.dto.PatientSchemeInfoVO;
import com.rehabilitation.dto.PrescriptionListDTO;
import com.rehabilitation.service.PatientSchemeInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者方案信息控制器
 */
@Tag(name = "患者方案查询", description = "患者方案查询管理接口")
@RestController
@RequestMapping("/api/patient-scheme-info")
@RequiredArgsConstructor
public class PatientSchemeInfoController {

    private final PatientSchemeInfoService patientSchemeInfoService;

    /**
     * 分页查询患者方案信息列表
     */
    @Operation(summary = "分页查询患者方案信息列表")
    @GetMapping("/list")
    public Result<IPage<PatientSchemeInfoVO>> getPatientSchemeInfoPage(PatientSchemeInfoQueryDTO query) {
        IPage<PatientSchemeInfoVO> result = patientSchemeInfoService.getPatientSchemeInfoPage(query);
        return Result.success(result);
    }

    /**
     * 获取患者方案信息详情
     */
    @Operation(summary = "获取患者方案信息详情")
    @GetMapping("/{id}")
    public Result<PatientSchemeInfoVO> getPatientSchemeInfoDetail(@PathVariable Long id) {
        PatientSchemeInfoVO result = patientSchemeInfoService.getPatientSchemeInfoDetail(id);
        return Result.success(result);
    }

    /**
     * 获取患者处方清单
     */
    @Operation(summary = "获取患者处方清单")
    @GetMapping("/prescription-list/{schemeId}")
    public Result<List<PrescriptionListDTO>> getPatientPrescriptionList(@PathVariable String schemeId) {
        List<PrescriptionListDTO> result = patientSchemeInfoService.getPatientPrescriptionList(schemeId);
        return Result.success(result);
    }
}
