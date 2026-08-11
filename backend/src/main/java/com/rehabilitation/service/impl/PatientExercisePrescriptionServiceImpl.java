package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.PatientExercisePrescriptionQueryDTO;
import com.rehabilitation.dto.PatientExercisePrescriptionVO;
import com.rehabilitation.dto.PrescriptionSaveDTO;
import com.rehabilitation.entity.MedicalScheme;
import com.rehabilitation.entity.PatientExercisePrescription;
import com.rehabilitation.entity.PatientInfo;
import com.rehabilitation.entity.PatientPrescribedExercise;
import com.rehabilitation.mapper.MedicalSchemeMapper;
import com.rehabilitation.mapper.PatientExercisePrescriptionMapper;
import com.rehabilitation.mapper.PatientInfoMapper;
import com.rehabilitation.mapper.PatientPrescribedExerciseMapper;
import com.rehabilitation.service.PatientExercisePrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 患者运动处方Service实现类
 */
@Service
public class PatientExercisePrescriptionServiceImpl implements PatientExercisePrescriptionService {

    @Autowired
    private PatientExercisePrescriptionMapper patientExercisePrescriptionMapper;
    @Autowired
    private PatientPrescribedExerciseMapper patientPrescribedExerciseMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private MedicalSchemeMapper medicalSchemeMapper;

    @Override
    public IPage<PatientExercisePrescriptionVO> getPatientExercisePrescriptionPage(PatientExercisePrescriptionQueryDTO query) {
        Page<PatientExercisePrescriptionVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return patientExercisePrescriptionMapper.selectPatientExercisePrescriptionPage(page, query);
    }

    @Override
    public PatientExercisePrescriptionVO getPatientExercisePrescriptionById(Long id) {
        return patientExercisePrescriptionMapper.selectPatientExercisePrescriptionById(id);
    }

    @Override
    public void addPatientExercisePrescription(PatientExercisePrescription patientExercisePrescription) {
        patientExercisePrescription.setPrescriptionStatus(1); // 1-未开始
        patientExercisePrescription.setCreatedTime(LocalDateTime.now());
        patientExercisePrescription.setUpdatedTime(LocalDateTime.now());
        patientExercisePrescriptionMapper.insert(patientExercisePrescription);
    }

    @Override
    public void updatePatientExercisePrescription(PatientExercisePrescription patientExercisePrescription) {
        patientExercisePrescription.setUpdatedTime(LocalDateTime.now());
        patientExercisePrescriptionMapper.updateById(patientExercisePrescription);
    }

    @Override
    public void endPatientExercisePrescription(Long id) {
        PatientExercisePrescription prescription = new PatientExercisePrescription();
        prescription.setId(id);
        prescription.setPrescriptionStatus(4); // 4-已结束
        prescription.setUpdatedTime(LocalDateTime.now());
        patientExercisePrescriptionMapper.updateById(prescription);
    }

    @Override
    public void executePatientExercisePrescription(Long id) {
        PatientExercisePrescription prescription = new PatientExercisePrescription();
        prescription.setId(id);
        prescription.setPrescriptionStatus(2); // 2-执行中
        prescription.setUpdatedTime(LocalDateTime.now());
        patientExercisePrescriptionMapper.updateById(prescription);
    }

    @Override
    public List<String> getAllDiseaseTypes() {
        return Arrays.asList(
                "妊娠合并糖尿病",
                "肥胖症",
                "高血压",
                "糖尿病",
                "冠心病",
                "慢性阻塞性肺疾病",
                "骨关节炎",
                "颈椎病",
                "腰椎间盘突出",
                "脑卒中后遗症"
        );
    }

    @Override
    public List<String> getAllInstitutions() {
        return Arrays.asList(
                "西南医科大学附属医院",
                "江安县中医院",
                "泸州市人民医院",
                "合江县人民医院",
                "古蔺县人民医院",
                "叙永县人民医院"
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long savePrescription(PrescriptionSaveDTO prescriptionSaveDTO) {
        // 1. 获取患者信息
        PatientInfo patientInfo = patientInfoMapper.selectById(prescriptionSaveDTO.getPatientId());
        if (patientInfo == null) {
            throw new RuntimeException("患者信息不存在");
        }

        // 2. 查询该患者已有的处方数量，生成处方名称
        LambdaQueryWrapper<PatientExercisePrescription> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PatientExercisePrescription::getPatientId, prescriptionSaveDTO.getPatientId());
        Long count = patientExercisePrescriptionMapper.selectCount(queryWrapper);
        int nextNumber = count.intValue() + 1;
        String prescriptionName = prescriptionSaveDTO.getDiseaseType() + "运动处方" + nextNumber;

        // 3. 创建运动处方记录
        PatientExercisePrescription prescription = new PatientExercisePrescription();
        // 通过 schemeId（字符串）查询 medical_scheme 表获取主键 ID
        if (prescriptionSaveDTO.getSchemeId() != null && !prescriptionSaveDTO.getSchemeId().isEmpty()) {
            QueryWrapper<MedicalScheme> schemeWrapper = new QueryWrapper<>();
            schemeWrapper.eq("scheme_id", prescriptionSaveDTO.getSchemeId());
            MedicalScheme medicalScheme = medicalSchemeMapper.selectOne(schemeWrapper);
            if (medicalScheme != null) {
                try {
                    java.lang.reflect.Field idField = medicalScheme.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    Long schemeIdValue = (Long) idField.get(medicalScheme);
                    prescription.setMedicalSchemeId(schemeIdValue);
                } catch (Exception e) {
                    prescription.setMedicalSchemeId(null);
                }
            }
        }
        prescription.setPatientId(prescriptionSaveDTO.getPatientId());
        prescription.setPrescriptionName(prescriptionName);
        // 直接使用传入的执行机构（保存时为null，提交时有值）
        String executionInstitution = prescriptionSaveDTO.getExecutionInstitution();
        prescription.setExecutionInstitution(executionInstitution);
        // 根据执行机构是否为空设置状态：为空=1(已创建)，不为空=2(执行中)
        int status = (executionInstitution == null || executionInstitution.isEmpty()) ? 1 : 2;
        prescription.setPrescriptionStatus(status);
        prescription.setPrescribingDoctor("系统"); // TODO: 从登录用户信息获取
        prescription.setCreatedTime(LocalDateTime.now());
        prescription.setUpdatedTime(LocalDateTime.now());

        // 4. 插入运动处方记录
        int insertResult = patientExercisePrescriptionMapper.insert(prescription);
        if (insertResult <= 0) {
            throw new RuntimeException("创建运动处方失败");
        }

        Long prescriptionId = prescription.getId();

        // 5. 插入运动项目记录
        for (PrescriptionSaveDTO.ExerciseItemDTO exerciseItem : prescriptionSaveDTO.getExerciseList()) {
            PatientPrescribedExercise exercise = new PatientPrescribedExercise();
            exercise.setPatientId(prescriptionSaveDTO.getPatientId());
            exercise.setExercisePrescriptionId(prescriptionId.intValue());
            // 使用与主表相同的执行机构
            exercise.setExecutionInstitution(executionInstitution);
            // 使用与主表相同的状态
            exercise.setPrescriptionStatus(status);
            exercise.setPrescribingDoctor("系统");
            exercise.setExerciseScene(exerciseItem.getExerciseScene());
            exercise.setExerciseType(exerciseItem.getExerciseType());
            exercise.setExerciseMethod(exerciseItem.getExerciseMethod());
            exercise.setPrecautions(exerciseItem.getPrecautions());
            exercise.setDurationMinutes(exerciseItem.getDurationMinutes());
            exercise.setExerciseFrequency(exerciseItem.getExerciseFrequency() != null ? exerciseItem.getExerciseFrequency() : "1次/周");
            exercise.setExerciseIntensity(exerciseItem.getExerciseIntensity() != null ? exerciseItem.getExerciseIntensity() : "中等");
            exercise.setCycleWeeks(exerciseItem.getCycleWeeks() != null ? exerciseItem.getCycleWeeks() : 1);
            exercise.setTotalSessions(exerciseItem.getTotalSessions() != null ? exerciseItem.getTotalSessions() : 1);
            exercise.setWeeklyTotalMinutes(exerciseItem.getWeeklyTotalMinutes() != null ? exerciseItem.getWeeklyTotalMinutes() : 0);
            exercise.setProgression(exerciseItem.getProgression());
            exercise.setRemarks(exerciseItem.getRemarks());
            exercise.setExerciseImage(exerciseItem.getExerciseImage());
            exercise.setCreatedTime(LocalDateTime.now());
            exercise.setUpdatedTime(LocalDateTime.now());

            int exerciseInsertResult = patientPrescribedExerciseMapper.insert(exercise);
            if (exerciseInsertResult <= 0) {
                throw new RuntimeException("创建运动项目失败");
            }
        }

        return prescriptionId;
    }
}