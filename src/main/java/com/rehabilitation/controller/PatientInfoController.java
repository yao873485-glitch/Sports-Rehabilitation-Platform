package com.rehabilitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.PatientCreateDTO;
import com.rehabilitation.dto.PatientInfoDetailVO;
import com.rehabilitation.dto.PatientInfoQueryDTO;
import com.rehabilitation.entity.PatientDetail;
import com.rehabilitation.entity.PatientInfo;
import com.rehabilitation.service.PatientInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 患者信息控制器
 */
@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientInfoController {

    private final PatientInfoService patientInfoService;

    /**
     * 分页查询患者信息列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getPatientList(PatientInfoQueryDTO queryDTO) {
        IPage<PatientInfo> page = patientInfoService.getPatientInfoPage(
                queryDTO.getPageNum(),
                queryDTO.getPageSize(),
                queryDTO.getName(),
                queryDTO.getStartTime(),
                queryDTO.getEndTime()
        );

        Map<String, Object> result = new HashMap<>();
        result.put("records", page.getRecords());
        result.put("total", page.getTotal());
        result.put("current", page.getCurrent());
        result.put("size", page.getSize());

        return Result.success("获取患者列表成功", result);
    }

    /**
     * 获取患者详细信息
     */
    @GetMapping("/detail/{id}")
    public Result<PatientInfoDetailVO> getPatientDetail(@PathVariable Integer id) {
        PatientInfo basicInfo = patientInfoService.getPatientInfoById(id);
        PatientDetail detailInfo = patientInfoService.getPatientDetailByBasicId(id);

        PatientInfoDetailVO vo = new PatientInfoDetailVO();
        vo.setBasicInfo(basicInfo);
        vo.setDetailInfo(detailInfo);

        return Result.success("获取患者详细信息成功", vo);
    }

    /**
     * 新增患者信息（包含基本信息和入组信息）
     */
    @PostMapping("/add")
    public Result<Map<String, Object>> addPatient(@RequestBody PatientCreateDTO patientCreateDTO) {
        try {
            PatientInfo patientInfo = patientInfoService.createPatient(patientCreateDTO);
            Map<String, Object> result = new HashMap<>();
            result.put("id", patientInfo.getId());
            result.put("medicalRecordNo", patientInfo.getMedicalRecordNo());
            return Result.success("患者信息添加成功", result);
        } catch (Exception e) {
            return Result.error("添加失败: " + e.getMessage());
        }
    }

    /**
     * 更新患者基本信息
     */
    @PutMapping("/update")
    public Result<String> updatePatient(@RequestBody PatientInfo patientInfo) {
        boolean success = patientInfoService.updatePatientInfo(patientInfo);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 保存或更新患者详细信息
     */
    @PostMapping("/save-detail")
    public Result<String> savePatientDetail(@RequestBody PatientDetail patientDetail) {
        boolean success = patientInfoService.saveOrUpdatePatientDetail(patientDetail);
        if (success) {
            return Result.success("保存成功");
        } else {
            return Result.error("保存失败");
        }
    }

    /**
     * 删除患者信息
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deletePatient(@PathVariable Integer id) {
        boolean success = patientInfoService.deletePatientInfo(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 更新患者病种
     */
    @PutMapping("/update-disease-type")
    public Result<String> updatePatientDiseaseType(@RequestBody Map<String, Object> params) {
        try {
            Integer patientId = (Integer) params.get("patientId");
            String diseaseType = (String) params.get("diseaseType");

            if (patientId == null || diseaseType == null || diseaseType.trim().isEmpty()) {
                return Result.error("参数不完整");
            }

            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setId(patientId);
            patientInfo.setDiseaseType(diseaseType);

            boolean success = patientInfoService.updatePatientInfo(patientInfo);
            if (success) {
                return Result.success("病种修改成功");
            } else {
                return Result.error("病种修改失败");
            }
        } catch (Exception e) {
            return Result.error("修改失败: " + e.getMessage());
        }
    }
}