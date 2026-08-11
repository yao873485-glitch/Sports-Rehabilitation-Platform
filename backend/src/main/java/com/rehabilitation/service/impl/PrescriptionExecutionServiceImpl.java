package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.dto.PrescriptionExecutionDTO;
import com.rehabilitation.entity.PatientExercisePrescription;
import com.rehabilitation.entity.PrescriptionExecutionRecord;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.PatientExercisePrescriptionMapper;
import com.rehabilitation.mapper.PatientPrescribedExerciseMapper;
import com.rehabilitation.mapper.PrescriptionExecutionRecordMapper;
import com.rehabilitation.messaging.BusinessEventPublisher;
import com.rehabilitation.service.PrescriptionExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PrescriptionExecutionServiceImpl
    extends ServiceImpl<PrescriptionExecutionRecordMapper, PrescriptionExecutionRecord>
    implements PrescriptionExecutionService {

    private static final Logger log = LoggerFactory.getLogger(PrescriptionExecutionServiceImpl.class);

    @Autowired
    private PatientExercisePrescriptionMapper prescriptionMapper;
    @Autowired
    private PatientPrescribedExerciseMapper exerciseMapper;
    @Autowired
    private BusinessEventPublisher businessEventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executePrescription(PrescriptionExecutionDTO dto) {
        log.info("======== 开始执行处方 ========");
        log.info("处方ID={}, 患者ID={}, 执行人员={}", dto.getPrescriptionId(), dto.getPatientId(), dto.getExecutor());

        PatientExercisePrescription prescription = prescriptionMapper.selectById(dto.getPrescriptionId().longValue());
        if (prescription == null) {
            throw new BusinessException("运动处方不存在");
        }

        if (dto.getExerciseId() != null) {
            log.info("单个执行运动项目: exerciseId={}", dto.getExerciseId());
            createExecutionRecord(dto, dto.getExerciseId());
        } else if (dto.getExerciseIds() != null && !dto.getExerciseIds().isEmpty()) {
            log.info("批量执行运动项目: exerciseIds={}", dto.getExerciseIds());
            for (Integer exerciseId : dto.getExerciseIds()) {
                createExecutionRecord(dto, exerciseId);
            }
        } else {
            throw new BusinessException("请选择要执行的运动项目");
        }

        prescription.setPrescriptionStatus(3);
        prescription.setUpdatedTime(LocalDateTime.now());
        prescriptionMapper.updateById(prescription);

        exerciseMapper.updateExercisesInstitutionAndStatus(
                dto.getPrescriptionId(),
                prescription.getExecutionInstitution(),
                3
        );

        log.info("======== 处方执行完成 ========");
        businessEventPublisher.publishPrescriptionEvent(
                "PRESCRIPTION_EXECUTED",
                dto.getPrescriptionId(),
                dto.getPatientId(),
                dto.getExecutor()
        );
    }

    private void createExecutionRecord(PrescriptionExecutionDTO dto, Integer exerciseId) {
        PrescriptionExecutionRecord record = new PrescriptionExecutionRecord();
        record.setPatientId(dto.getPatientId());
        record.setPrescriptionId(dto.getPrescriptionId());
        record.setExerciseId(exerciseId);
        record.setExecutionCount(dto.getExecutionCount());
        record.setExecutor(dto.getExecutor());
        record.setStartTime(dto.getStartTime());
        record.setEndTime(dto.getEndTime());
        record.setExecutionRecord(dto.getExecutionRecord());
        record.setStatus(3);
        record.setCreatedTime(LocalDateTime.now());
        record.setUpdatedTime(LocalDateTime.now());

        baseMapper.insert(record);
        log.info("创建执行记录成功: exerciseId={}, executor={}", exerciseId, dto.getExecutor());
    }
}
