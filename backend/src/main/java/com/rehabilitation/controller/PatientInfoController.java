package com.rehabilitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.EnrollmentAuditDTO;
import com.rehabilitation.dto.PatientCreateDTO;
import com.rehabilitation.dto.PatientInfoDetailVO;
import com.rehabilitation.dto.PatientInfoQueryDTO;
import com.rehabilitation.entity.PatientDetail;
import com.rehabilitation.entity.PatientEnrollmentAudit;
import com.rehabilitation.entity.PatientInfo;
import com.rehabilitation.constant.CacheNames;
import com.rehabilitation.service.DiseaseRecordService;
import com.rehabilitation.service.EnrollmentAssessmentService;
import com.rehabilitation.service.HealthRecordService;
import com.rehabilitation.service.PatientEnrollmentAuditService;
import com.rehabilitation.service.PatientInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 患者信息控制器
 */
@RestController
@RequestMapping("/api/patient")
public class PatientInfoController {

    @Autowired
    private PatientInfoService patientInfoService;

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired
    private DiseaseRecordService diseaseRecordService;

    @Autowired
    private EnrollmentAssessmentService enrollmentAssessmentService;

    @Autowired
    private PatientEnrollmentAuditService patientEnrollmentAuditService;

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
     * 未入组患者中心详情
     */
    @Cacheable(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#id")
    @GetMapping("/center-detail/{id}")
    public Result<PatientInfoDetailVO> getPatientCenterDetail(@PathVariable Integer id) {
        PatientInfo basicInfo = patientInfoService.getPatientInfoById(id);
        if (basicInfo == null) {
            return Result.error("患者不存在");
        }
        PatientDetail detailInfo = patientInfoService.getPatientDetailByBasicId(id);

        PatientInfoDetailVO vo = new PatientInfoDetailVO();
        vo.setBasicInfo(basicInfo);
        vo.setDetailInfo(detailInfo);
        vo.setHealthRecord(healthRecordService.getByPatientId(id));
        vo.setDiseaseRecord(diseaseRecordService.getByPatientId(id));
        vo.setEnrollmentAssessment(enrollmentAssessmentService.getByPatientId(id));
        PatientEnrollmentAudit audit = patientEnrollmentAuditService.getByPatientId(id);
        if (audit != null) {
            vo.setEnrollmentStatus(convertStatusToText(audit.getStatus()));
            vo.setEnrollmentRejectReason(audit.getRejectReason());
        } else {
            vo.setEnrollmentStatus("PENDING");
            vo.setEnrollmentRejectReason(null);
        }

        return Result.success("获取患者中心详情成功", vo);
    }

    @CacheEvict(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#auditDTO.patientId")
    @PostMapping("/enrollment-audit")
    public Result<String> updateEnrollmentAudit(@RequestBody EnrollmentAuditDTO auditDTO) {
        if (auditDTO.getPatientId() == null) {
            return Result.error("患者ID不能为空");
        }
        Integer statusCode = convertStatusToCode(auditDTO.getStatus());
        if (statusCode == null) {
            return Result.error("审核状态不合法");
        }
        String reason = auditDTO.getRejectReason();
        if (statusCode == 2) {
            if (reason == null || reason.trim().isEmpty()) {
                return Result.error("请输入拒绝原因，最多200字");
            }
            reason = reason.trim();
            if (reason.length() > 200) {
                return Result.error("拒绝原因不能超过200字");
            }
        }
        boolean success = patientEnrollmentAuditService.saveOrUpdateStatus(
                auditDTO.getPatientId(),
                statusCode,
                statusCode == 2 ? reason : null
        );
        if (success) {
            return Result.success("修改审核状态成功");
        }
        return Result.error("修改失败，请稍后重试");
    }

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
    @CacheEvict(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#patientInfo.id", condition = "#patientInfo != null && #patientInfo.id != null")
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
    @CacheEvict(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#patientDetail.patientId", condition = "#patientDetail != null && #patientDetail.patientId != null")
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
    @CacheEvict(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#id")
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
    @CacheEvict(cacheNames = CacheNames.PATIENT_CENTER_DETAIL, key = "#params['patientId']", condition = "#params != null && #params['patientId'] != null")
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

    private String convertStatusToText(Integer status) {
        if (status == null) {
            return "PENDING";
        }
        switch (status) {
            case 1:
                return "PASSED";
            case 2:
                return "REJECTED";
            default:
                return "PENDING";
        }
    }

    private Integer convertStatusToCode(String text) {
        if (text == null) {
            return null;
        }
        switch (text) {
            case "PASSED":
                return 1;
            case "REJECTED":
                return 2;
            case "PENDING":
                return 0;
            default:
                return null;
        }
    }
}
