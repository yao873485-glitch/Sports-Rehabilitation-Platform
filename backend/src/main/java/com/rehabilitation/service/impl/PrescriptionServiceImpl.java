package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.PrescriptionQueryDTO;
import com.rehabilitation.dto.PrescriptionVO;
import com.rehabilitation.dto.PrescriptionDetailVO;
import com.rehabilitation.dto.PrescriptionExerciseVO;
import com.rehabilitation.dto.PrescriptionSaveDTO;
import com.rehabilitation.entity.PatientPrescribedExercise;
import com.rehabilitation.entity.PatientExercisePrescription;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.PatientPrescribedExerciseMapper;
import com.rehabilitation.mapper.PatientExercisePrescriptionMapper;
import com.rehabilitation.messaging.BusinessEventPublisher;
import com.rehabilitation.service.PrescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 运动处方服务实现
 */
@Service
public class PrescriptionServiceImpl
    extends ServiceImpl<PatientPrescribedExerciseMapper, PatientPrescribedExercise>
    implements PrescriptionService {

    private static final Logger log = LoggerFactory.getLogger(PrescriptionServiceImpl.class);

    @Autowired
    private PatientExercisePrescriptionMapper prescriptionMapper;
    @Autowired
    private BusinessEventPublisher businessEventPublisher;

    @Override
    public PageResult<PrescriptionVO> getPrescriptionPage(PrescriptionQueryDTO query) {
        log.info("======== 开始查询运动处方列表 ========");
        log.info("查询参数: patientId={}, diseaseType={}, prescriptionStatus={}, enrollmentInstitution={}, executionInstitution={}, searchKeyword={}, pageNum={}, pageSize={}",
            query.getPatientId(), query.getDiseaseType(), query.getPrescriptionStatus(),
            query.getEnrollmentInstitution(), query.getExecutionInstitution(),
            query.getSearchKeyword(), query.getPageNum(), query.getPageSize());

        Page<PrescriptionVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<PrescriptionVO> result = baseMapper.selectPrescriptionPage(page, query);

        log.info("查询结果: 总记录数={}, 当前页={}, 每页条数={}, 返回记录数={}",
            result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords().size());

        if (result.getRecords().isEmpty()) {
            log.warn("!!!!! 查询结果为空，没有找到任何运动处方记录 !!!!!");
        } else {
            log.info("查询到的处方列表:");
            for (PrescriptionVO vo : result.getRecords()) {
                log.info("  - 处方ID={}, 处方名称={}, 患者姓名={}, 档案号={}",
                    vo.getId(), vo.getPrescriptionName(), vo.getPatientName(), vo.getMedicalRecordNumber());
            }
        }
        log.info("======== 运动处方列表查询完成 ========");

        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public PrescriptionVO getPrescriptionDetail(Integer id) {
        PatientPrescribedExercise entity = getById(id);
        if (entity == null) {
            throw new BusinessException("运动处方不存在");
        }

        PrescriptionVO vo = new PrescriptionVO();
        BeanUtils.copyProperties(entity, vo);

        return vo;
    }

    @Override
    public PrescriptionDetailVO getPrescriptionDetailWithExercises(Integer prescriptionId) {
        // 查询处方基本信息和患者信息
        PrescriptionDetailVO detailVO = baseMapper.selectPrescriptionDetailById(prescriptionId);
        if (detailVO == null) {
            throw new BusinessException("运动处方不存在");
        }

        // 查询处方运动项目列表
        List<PrescriptionExerciseVO> exerciseList = baseMapper.selectExerciseListByPrescriptionId(prescriptionId);
        detailVO.setExerciseList(exerciseList);

        return detailVO;
    }

    @Override
    public void endPrescription(Integer id) {
        PatientPrescribedExercise entity = getById(id);
        if (entity == null) {
            throw new BusinessException("运动处方不存在");
        }

        if (Integer.valueOf(4).equals(entity.getPrescriptionStatus())) {
            throw new BusinessException("运动处方已结束，无需重复操作");
        }

        entity.setPrescriptionStatus(4); // 4-已结束
        updateById(entity);
    }

    @Override
    public void executePrescription(Integer id) {
        PatientPrescribedExercise entity = getById(id);
        if (entity == null) {
            throw new BusinessException("运动处方不存在");
        }

        if (Integer.valueOf(2).equals(entity.getPrescriptionStatus())) {
            throw new BusinessException("运动处方已在执行中");
        }

        if (Integer.valueOf(4).equals(entity.getPrescriptionStatus())) {
            throw new BusinessException("运动处方已结束，无法执行");
        }

        entity.setPrescriptionStatus(2); // 2-执行中
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePrescription(PrescriptionSaveDTO dto) {
        log.info("======== 开始保存运动处方 ========");
        log.info("处方ID={}, 患者ID={}, 方案ID={}, 执行机构={}",
            dto.getPrescriptionId(), dto.getPatientId(), dto.getSchemeId(), dto.getExecutionInstitution());

        // 获取原处方信息（用于获取执行机构和开方医生）
        String executionInstitution = dto.getExecutionInstitution();
        String prescribingDoctor = "系统"; // 默认值
        Integer prescriptionStatus = 1; // 默认状态：已创建

        // 如果有prescriptionId，说明是编辑现有处方
        if (dto.getPrescriptionId() != null) {
            // 查询处方基本信息（从 patient_exercise_prescription 表）
            PatientExercisePrescription prescription = prescriptionMapper.selectById(dto.getPrescriptionId().longValue());
            if (prescription == null) {
                throw new BusinessException("运动处方不存在");
            }

            // 从原处方中获取开方医生和状态
            prescribingDoctor = prescription.getPrescribingDoctor();
            prescriptionStatus = prescription.getPrescriptionStatus();

            // 如果DTO中没有传入执行机构，则从原处方中获取
            if (executionInstitution == null || executionInstitution.isEmpty()) {
                executionInstitution = prescription.getExecutionInstitution();
                // 如果原处方中也没有，使用默认值
                if (executionInstitution == null || executionInstitution.isEmpty()) {
                    executionInstitution = "待分配";
                }
            }

            log.info("使用的执行机构: executionInstitution={}, prescribingDoctor={}, prescriptionStatus={}",
                executionInstitution, prescribingDoctor, prescriptionStatus);

            // 保存时如有传入执行机构则同步更新主表，避免执行列表筛选不到
            if (dto.getExecutionInstitution() != null && !dto.getExecutionInstitution().isEmpty()) {
                prescription.setExecutionInstitution(dto.getExecutionInstitution());
            }
            prescription.setUpdatedTime(LocalDateTime.now());
            prescriptionMapper.updateById(prescription);

            // 删除旧的运动项目
            baseMapper.deleteExercisesByPrescriptionId(dto.getPrescriptionId());
        }

        // 保存运动项目
        if (dto.getExerciseList() != null && !dto.getExerciseList().isEmpty()) {
            for (PrescriptionSaveDTO.ExerciseItemDTO item : dto.getExerciseList()) {
                PatientPrescribedExercise exercise = new PatientPrescribedExercise();

                // 使用反射设置字段值（解决Lombok编译问题）
                Integer patientId = dto.getPatientId();
                Integer prescriptionId = dto.getPrescriptionId();

                log.info("准备插入运动项目: patientId={}, prescriptionId={}", patientId, prescriptionId);

                setFieldValue(exercise, "exercisePrescriptionId", prescriptionId);
                setFieldValue(exercise, "patientId", patientId);
                setFieldValue(exercise, "executionInstitution", executionInstitution);
                setFieldValue(exercise, "prescribingDoctor", prescribingDoctor);
                setFieldValue(exercise, "prescriptionStatus", prescriptionStatus);
                setFieldValue(exercise, "exerciseScene", item.getExerciseScene());
                setFieldValue(exercise, "exerciseType", item.getExerciseType());
                setFieldValue(exercise, "exerciseMethod", item.getExerciseMethod());
                setFieldValue(exercise, "precautions", item.getPrecautions());
                setFieldValue(exercise, "durationMinutes", item.getDurationMinutes());
                setFieldValue(exercise, "exerciseFrequency", item.getExerciseFrequency());
                setFieldValue(exercise, "exerciseIntensity", item.getExerciseIntensity());
                setFieldValue(exercise, "cycleWeeks", item.getCycleWeeks());
                setFieldValue(exercise, "totalSessions", item.getTotalSessions());
                setFieldValue(exercise, "weeklyTotalMinutes", item.getWeeklyTotalMinutes());
                setFieldValue(exercise, "progression", item.getProgression());
                setFieldValue(exercise, "remarks", item.getRemarks());
                setFieldValue(exercise, "exerciseImage", item.getExerciseImage());
                setFieldValue(exercise, "createdTime", LocalDateTime.now());
                setFieldValue(exercise, "updatedTime", LocalDateTime.now());

                // 验证字段是否设置成功
                Integer verifyPatientId = getFieldValue(exercise, "patientId");
                log.info("验证设置后的patientId: {}", verifyPatientId);

                baseMapper.insert(exercise);
            }
        }

        log.info("======== 运动处方保存完成 ========");
    }

    /**
     * 使用反射获取字段值
     */
    private <T> T getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用反射设置字段值（解决Lombok编译问题）
     */
    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitPrescription(PrescriptionSaveDTO dto) {
        log.info("======== 开始提交运动处方 ========");
        log.info("处方ID={}, 患者ID={}, 方案ID={}, 执行机构={}",
            dto.getPrescriptionId(), dto.getPatientId(), dto.getSchemeId(), dto.getExecutionInstitution());

        // 先保存处方（会保存运动项目，并使用DTO中的执行机构）
        savePrescription(dto);

        // 更新执行机构和状态（在 patient_exercise_prescription 表）
        if (dto.getPrescriptionId() != null && dto.getExecutionInstitution() != null) {
            PatientExercisePrescription prescription = prescriptionMapper.selectById(dto.getPrescriptionId().longValue());
            if (prescription != null) {
                prescription.setExecutionInstitution(dto.getExecutionInstitution());
                prescription.setPrescriptionStatus(2); // 2-执行中
                prescription.setUpdatedTime(LocalDateTime.now());
                prescriptionMapper.updateById(prescription);

                log.info("已更新处方主表执行机构和状态: executionInstitution={}, status=2", dto.getExecutionInstitution());

                // 同时更新 patient_prescribed_exercise 表中所有运动项目的执行机构和状态
                baseMapper.updateExercisesInstitutionAndStatus(
                    dto.getPrescriptionId(),
                    dto.getExecutionInstitution(),
                    2 // 2-执行中
                );

                log.info("已更新处方运动项目表执行机构和状态: prescriptionId={}, executionInstitution={}, status=2",
                    dto.getPrescriptionId(), dto.getExecutionInstitution());
            }
        }

        log.info("======== 运动处方提交完成 ========");
        businessEventPublisher.publishPrescriptionEvent(
                "PRESCRIPTION_SUBMITTED",
                dto.getPrescriptionId(),
                dto.getPatientId(),
                null
        );
    }
}
