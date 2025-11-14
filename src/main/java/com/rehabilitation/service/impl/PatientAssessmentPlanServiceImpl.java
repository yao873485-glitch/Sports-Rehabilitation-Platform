package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.AssessmentPlanSaveDTO;
import com.rehabilitation.dto.PatientAssessmentPlanQueryDTO;
import com.rehabilitation.dto.PatientAssessmentPlanVO;
import com.rehabilitation.entity.AssessmentExecutionRecord;
import com.rehabilitation.entity.PatientAssessmentPlan;
import com.rehabilitation.entity.PatientInfo;
import com.rehabilitation.mapper.AssessmentExecutionRecordMapper;
import com.rehabilitation.mapper.PatientAssessmentPlanMapper;
import com.rehabilitation.mapper.PatientInfoMapper;
import com.rehabilitation.service.PatientAssessmentPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 患者评定方案Service实现类
 */
@Service
@RequiredArgsConstructor
public class PatientAssessmentPlanServiceImpl implements PatientAssessmentPlanService {

    private final PatientAssessmentPlanMapper patientAssessmentPlanMapper;
    private final AssessmentExecutionRecordMapper assessmentExecutionRecordMapper;
    private final PatientInfoMapper patientInfoMapper;

    @Override
    public IPage<PatientAssessmentPlanVO> getPatientAssessmentPlanPage(PatientAssessmentPlanQueryDTO query) {
        Page<PatientAssessmentPlanVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return patientAssessmentPlanMapper.selectPatientAssessmentPlanPage(page, query);
    }

    @Override
    public PatientAssessmentPlanVO getPatientAssessmentPlanById(Integer id) {
        return patientAssessmentPlanMapper.selectPatientAssessmentPlanById(id);
    }

    @Override
    public void addPatientAssessmentPlan(PatientAssessmentPlan patientAssessmentPlan) {
        patientAssessmentPlan.setStatus(1); // 1-未开始
        patientAssessmentPlan.setCreatedTime(LocalDateTime.now());
        patientAssessmentPlan.setUpdatedTime(LocalDateTime.now());
        patientAssessmentPlanMapper.insert(patientAssessmentPlan);
    }

    @Override
    public void updatePatientAssessmentPlan(PatientAssessmentPlan patientAssessmentPlan) {
        patientAssessmentPlan.setUpdatedTime(LocalDateTime.now());
        patientAssessmentPlanMapper.updateById(patientAssessmentPlan);
    }

    @Override
    public void endPatientAssessmentPlan(Integer id) {
        PatientAssessmentPlan plan = new PatientAssessmentPlan();
        plan.setId(id);
        plan.setStatus(4); // 4-已结束
        plan.setUpdatedTime(LocalDateTime.now());
        patientAssessmentPlanMapper.updateById(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveAssessmentPlan(AssessmentPlanSaveDTO assessmentPlanSaveDTO) {
        // 1. 获取患者信息
        PatientInfo patientInfo = patientInfoMapper.selectById(assessmentPlanSaveDTO.getPatientId());
        if (patientInfo == null) {
            throw new RuntimeException("患者信息不存在");
        }

        // 2. 查询该患者已有的评定方案数量，生成方案名称
        LambdaQueryWrapper<PatientAssessmentPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PatientAssessmentPlan::getPatientId, assessmentPlanSaveDTO.getPatientId());
        Long count = patientAssessmentPlanMapper.selectCount(queryWrapper);
        int nextNumber = count.intValue() + 1;
        String assessmentName = assessmentPlanSaveDTO.getDiseaseType() + "评定方案" + nextNumber;

        Long firstPlanId = null;

        // 3. 为每个评定项目创建记录
        for (AssessmentPlanSaveDTO.AssessmentItemDTO assessmentItem : assessmentPlanSaveDTO.getAssessmentList()) {
            // 创建评定方案记录
            PatientAssessmentPlan plan = new PatientAssessmentPlan();

            // 将 String 类型的 schemeId 转换为 Long
            if (assessmentPlanSaveDTO.getSchemeId() != null && !assessmentPlanSaveDTO.getSchemeId().isEmpty()) {
                try {
                    plan.setMedicalSchemeId(Long.parseLong(assessmentPlanSaveDTO.getSchemeId()));
                } catch (NumberFormatException e) {
                    plan.setMedicalSchemeId(null);
                }
            }

            plan.setPatientId(assessmentPlanSaveDTO.getPatientId());
            plan.setAssessmentName(assessmentName);
            plan.setExecutionInstitution(patientInfo.getEnrollmentInstitution());
            plan.setStatus(1); // 1-未开始
            plan.setPrescribingDoctor("系统"); // TODO: 从登录用户信息获取
            plan.setCreatedTime(LocalDateTime.now());
            plan.setUpdatedTime(LocalDateTime.now());
            plan.setExecutionType(assessmentItem.getExecutionType());
            plan.setScaleName(assessmentItem.getScaleName());
            plan.setExecutionFrequency(assessmentItem.getExecutionFrequency());
            plan.setCycleDuration(assessmentItem.getCycleDuration() != null ? assessmentItem.getCycleDuration() : 0);
            plan.setTotalTimes(assessmentItem.getTotalTimes() != null ? assessmentItem.getTotalTimes() : 1);
            plan.setRemarks(assessmentItem.getRemarks());

            // 插入评定方案记录
            int insertResult = patientAssessmentPlanMapper.insert(plan);
            if (insertResult <= 0) {
                throw new RuntimeException("创建评定方案失败");
            }

            // 保存第一个方案ID用于返回
            if (firstPlanId == null) {
                firstPlanId = plan.getId().longValue();
            }

            // 创建评定执行记录
            AssessmentExecutionRecord record = new AssessmentExecutionRecord();
            record.setPatientId(assessmentPlanSaveDTO.getPatientId());
            record.setAssessmentPlanId(plan.getId());
            record.setExecutionInstitution(patientInfo.getEnrollmentInstitution());
            record.setStatus(1); // 1-未开始
            record.setPrescribingDoctor("系统");
            record.setCreatedTime(LocalDateTime.now());
            record.setUpdatedTime(LocalDateTime.now());

            int recordInsertResult = assessmentExecutionRecordMapper.insert(record);
            if (recordInsertResult <= 0) {
                throw new RuntimeException("创建评定执行记录失败");
            }
        }

        return firstPlanId;
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
}