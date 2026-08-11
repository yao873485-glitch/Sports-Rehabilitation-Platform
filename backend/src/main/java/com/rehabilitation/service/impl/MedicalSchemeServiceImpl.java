package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.MedicalSchemeQueryDTO;
import com.rehabilitation.dto.MedicalSchemeVO;
import com.rehabilitation.dto.SchemeCreateDTO;
import com.rehabilitation.entity.AssessmentExecutionRecord;
import com.rehabilitation.entity.MedicalScheme;
import com.rehabilitation.entity.PatientAssessmentPlan;
import com.rehabilitation.entity.PatientExercisePrescription;
import com.rehabilitation.entity.PatientInfo;
import com.rehabilitation.entity.PatientPrescribedExercise;
import com.rehabilitation.entity.PatientSchemeInfo;
import com.rehabilitation.mapper.AssessmentExecutionRecordMapper;
import com.rehabilitation.mapper.MedicalSchemeMapper;
import com.rehabilitation.mapper.PatientAssessmentPlanMapper;
import com.rehabilitation.mapper.PatientExercisePrescriptionMapper;
import com.rehabilitation.mapper.PatientInfoMapper;
import com.rehabilitation.mapper.PatientPrescribedExerciseMapper;
import com.rehabilitation.mapper.PatientSchemeInfoMapper;
import com.rehabilitation.service.MedicalSchemeService;
import com.rehabilitation.service.support.DistributedLockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class MedicalSchemeServiceImpl implements MedicalSchemeService {

    @Autowired
    private MedicalSchemeMapper medicalSchemeMapper;
    @Autowired
    private PatientSchemeInfoMapper patientSchemeInfoMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private PatientExercisePrescriptionMapper patientExercisePrescriptionMapper;
    @Autowired
    private PatientAssessmentPlanMapper patientAssessmentPlanMapper;
    @Autowired
    private PatientPrescribedExerciseMapper patientPrescribedExerciseMapper;
    @Autowired
    private AssessmentExecutionRecordMapper assessmentExecutionRecordMapper;
    @Autowired
    private DistributedLockService distributedLockService;

    @Override
    public IPage<MedicalSchemeVO> getMedicalSchemePage(MedicalSchemeQueryDTO query) {
        Page<MedicalSchemeVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return medicalSchemeMapper.selectMedicalSchemePage(page, query);
    }

    @Override
    public MedicalSchemeVO getMedicalSchemeDetail(Long id) {
        MedicalScheme medicalScheme = medicalSchemeMapper.selectById(id);
        return convertToVO(medicalScheme);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean endMedicalScheme(Long id, String reason) {
        MedicalScheme existingScheme = medicalSchemeMapper.selectById(id);
        if (existingScheme == null) {
            throw new RuntimeException("方案不存在");
        }
        String schemeId = getFieldValue(existingScheme, "schemeId");

        MedicalScheme medicalScheme = new MedicalScheme();
        setFieldValue(medicalScheme, "id", id);
        setFieldValue(medicalScheme, "status", 2);
        setFieldValue(medicalScheme, "endTime", LocalDateTime.now());
        int result1 = medicalSchemeMapper.updateById(medicalScheme);

        PatientSchemeInfo querySchemeInfo = new PatientSchemeInfo();
        querySchemeInfo.setSchemeId(schemeId);
        List<PatientSchemeInfo> schemeInfoList = patientSchemeInfoMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>(querySchemeInfo)
        );
        if (!schemeInfoList.isEmpty()) {
            PatientSchemeInfo patientSchemeInfo = schemeInfoList.get(0);
            patientSchemeInfo.setStatus(2);
            patientSchemeInfo.setEndTime(LocalDateTime.now());
            patientSchemeInfoMapper.updateById(patientSchemeInfo);
        }

        UpdateWrapper<PatientExercisePrescription> prescriptionWrapper = new UpdateWrapper<>();
        prescriptionWrapper.eq("medical_scheme_id", id);
        PatientExercisePrescription prescriptionUpdate = new PatientExercisePrescription();
        prescriptionUpdate.setPrescriptionStatus(4);
        patientExercisePrescriptionMapper.update(prescriptionUpdate, prescriptionWrapper);

        List<PatientExercisePrescription> prescriptions = patientExercisePrescriptionMapper.selectList(prescriptionWrapper);
        if (!prescriptions.isEmpty()) {
            for (PatientExercisePrescription prescription : prescriptions) {
                UpdateWrapper<PatientPrescribedExercise> exerciseWrapper = new UpdateWrapper<>();
                try {
                    java.lang.reflect.Field idField = prescription.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    Long prescriptionId = (Long) idField.get(prescription);
                    exerciseWrapper.eq("exercise_prescription_id", prescriptionId);
                    PatientPrescribedExercise exerciseUpdate = new PatientPrescribedExercise();
                    exerciseUpdate.setPrescriptionStatus(4);
                    patientPrescribedExerciseMapper.update(exerciseUpdate, exerciseWrapper);
                } catch (Exception ignored) {
                }
            }
        }

        UpdateWrapper<PatientAssessmentPlan> assessmentWrapper = new UpdateWrapper<>();
        assessmentWrapper.eq("medical_scheme_id", id);
        PatientAssessmentPlan assessmentUpdate = new PatientAssessmentPlan();
        assessmentUpdate.setStatus(4);
        patientAssessmentPlanMapper.update(assessmentUpdate, assessmentWrapper);

        List<PatientAssessmentPlan> assessments = patientAssessmentPlanMapper.selectList(assessmentWrapper);
        if (!assessments.isEmpty()) {
            for (PatientAssessmentPlan assessment : assessments) {
                UpdateWrapper<AssessmentExecutionRecord> recordWrapper = new UpdateWrapper<>();
                try {
                    java.lang.reflect.Field idField = assessment.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    Integer assessmentId = (Integer) idField.get(assessment);
                    recordWrapper.eq("assessment_plan_id", assessmentId);
                    AssessmentExecutionRecord recordUpdate = new AssessmentExecutionRecord();
                    recordUpdate.setStatus(4);
                    assessmentExecutionRecordMapper.update(recordUpdate, recordWrapper);
                } catch (Exception ignored) {
                }
            }
        }

        return result1 > 0;
    }

    private <T> T getFieldValue(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field: " + fieldName, e);
        }
    }

    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }

    @Override
    public boolean checkPatientHasActiveScheme(String medicalRecordNo) {
        Integer count = medicalSchemeMapper.countActiveSchemeByMedicalRecordNo(medicalRecordNo);
        return count != null && count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createScheme(SchemeCreateDTO schemeCreateDTO) {
        return distributedLockService.executeWithLock("lock:scheme:create:" + schemeCreateDTO.getPatientId(), 3, 10, () -> {
            PatientInfo patientInfo = patientInfoMapper.selectById(schemeCreateDTO.getPatientId());
            if (patientInfo == null) {
                throw new RuntimeException("患者信息不存在");
            }

            String schemeId = generateUniqueSchemeId();
            LocalDateTime createTime = LocalDateTime.now();
            LocalDateTime endTime = createTime.plusMonths(schemeCreateDTO.getSchemeCycle());
            LocalDate createDate = createTime.toLocalDate();

            PatientSchemeInfo patientSchemeInfo = new PatientSchemeInfo();
            patientSchemeInfo.setPatientId(schemeCreateDTO.getPatientId());
            patientSchemeInfo.setSchemeId(schemeId);
            patientSchemeInfo.setStatus(1);
            patientSchemeInfo.setCreateTime(createTime);
            patientSchemeInfo.setEndTime(endTime);

            int insertResult1 = patientSchemeInfoMapper.insert(patientSchemeInfo);
            if (insertResult1 <= 0) {
                throw new RuntimeException("创建患者方案信息失败");
            }

            MedicalScheme medicalScheme = new MedicalScheme();
            medicalScheme.setPatientId(schemeCreateDTO.getPatientId());
            medicalScheme.setSchemeId(schemeId);
            medicalScheme.setDoctorName("系统");
            medicalScheme.setStatus(1);
            medicalScheme.setCreateTime(createTime);
            medicalScheme.setEndTime(endTime);
            medicalScheme.setCreateDate(createDate);

            int insertResult2 = medicalSchemeMapper.insert(medicalScheme);
            if (insertResult2 <= 0) {
                throw new RuntimeException("创建医疗方案失败");
            }

            return schemeId;
        });
    }

    private String generateUniqueSchemeId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        return "SC" + timestamp + String.format("%04d", randomNum);
    }

    private MedicalSchemeVO convertToVO(MedicalScheme medicalScheme) {
        if (medicalScheme == null) {
            return null;
        }

        MedicalSchemeVO vo = new MedicalSchemeVO();
        BeanUtils.copyProperties(medicalScheme, vo);

        PatientInfo patientInfo = patientInfoMapper.selectById(medicalScheme.getPatientId());
        if (patientInfo != null) {
            vo.setPatientName(patientInfo.getName());
            vo.setGenderDesc(patientInfo.getGender());
            vo.setArchiveNo(patientInfo.getMedicalRecordNo());
            vo.setDiseaseType(patientInfo.getDiseaseType());

            if (patientInfo.getBirthDate() != null) {
                vo.setAge(LocalDate.now().getYear() - patientInfo.getBirthDate().getYear());
            }

            vo.setGender("男".equals(patientInfo.getGender()) ? 1 : 2);
        }

        vo.setStatusDesc(medicalScheme.getStatus() == 1 ? "执行中" : "已结束");
        return vo;
    }
}
