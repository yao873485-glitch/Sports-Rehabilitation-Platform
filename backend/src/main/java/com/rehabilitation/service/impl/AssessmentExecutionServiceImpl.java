package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.AssessmentExecutionInfoVO;
import com.rehabilitation.dto.AssessmentExecutionQueryDTO;
import com.rehabilitation.dto.AssessmentExecutionSubmitDTO;
import com.rehabilitation.dto.AssessmentExecutionVO;
import com.rehabilitation.entity.AssessmentExecutionDetail;
import com.rehabilitation.entity.AssessmentExecutionRecord;
import com.rehabilitation.entity.PatientAssessmentPlan;
import com.rehabilitation.entity.PatientInfo;
import com.rehabilitation.mapper.AssessmentExecutionDetailMapper;
import com.rehabilitation.mapper.AssessmentExecutionMapper;
import com.rehabilitation.mapper.PatientAssessmentPlanMapper;
import com.rehabilitation.mapper.PatientInfoMapper;
import com.rehabilitation.messaging.BusinessEventPublisher;
import com.rehabilitation.service.AssessmentExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

/**
 * 评定执行Service实现类
 */
@Service
public class AssessmentExecutionServiceImpl implements AssessmentExecutionService {

    @Autowired
    private AssessmentExecutionMapper assessmentExecutionMapper;

    @Autowired
    private AssessmentExecutionDetailMapper assessmentExecutionDetailMapper;
    @Autowired
    private BusinessEventPublisher businessEventPublisher;

    @Autowired
    private PatientAssessmentPlanMapper patientAssessmentPlanMapper;

    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Override
    public IPage<AssessmentExecutionVO> getAssessmentExecutionPage(AssessmentExecutionQueryDTO query) {
        Page<AssessmentExecutionVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return assessmentExecutionMapper.selectAssessmentExecutionPage(page, query);
    }

    @Override
    public AssessmentExecutionVO getAssessmentExecutionById(Integer id) {
        return assessmentExecutionMapper.selectAssessmentExecutionById(id);
    }

    @Override
    public void endAssessmentExecution(Integer id) {
        AssessmentExecutionRecord record = new AssessmentExecutionRecord();
        record.setId(id);
        record.setStatus(4); // 4-已结束
        record.setUpdatedTime(LocalDateTime.now());
        assessmentExecutionMapper.updateById(record);
    }

    @Override
    public void executeAssessment(Integer id) {
        AssessmentExecutionRecord record = new AssessmentExecutionRecord();
        record.setId(id);
        record.setStatus(2); // 2-执行中
        record.setUpdatedTime(LocalDateTime.now());
        assessmentExecutionMapper.updateById(record);
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
    public AssessmentExecutionInfoVO getAssessmentExecutionInfo(Integer assessmentPlanId) {
        // 查询评定方案信息
        PatientAssessmentPlan plan = patientAssessmentPlanMapper.selectById(assessmentPlanId);
        if (plan == null) {
            throw new RuntimeException("评定方案不存在");
        }

        // 查询患者信息
        PatientInfo patient = patientInfoMapper.selectById(plan.getPatientId());
        if (patient == null) {
            throw new RuntimeException("患者信息不存在");
        }

        // 构建返回VO
        AssessmentExecutionInfoVO vo = new AssessmentExecutionInfoVO();
        vo.setAssessmentPlanId(assessmentPlanId);
        vo.setPatientId(patient.getId());
        vo.setPatientName(patient.getName());
        vo.setGender(patient.getGender());

        // 计算年龄
        if (patient.getBirthDate() != null) {
            Period period = Period.between(patient.getBirthDate(), java.time.LocalDate.now());
            vo.setAge(period.getYears());
        }

        vo.setIdCard(patient.getIdCard());
        vo.setPhone(patient.getPhone());
        vo.setMedicalRecordNo(patient.getMedicalRecordNo());
        vo.setAssessmentName(plan.getAssessmentName());

        // 设置入组日期和记录日期（这里使用创建时间的日期部分）
        if (patient.getCreatedTime() != null) {
            vo.setEnrollmentDate(patient.getCreatedTime().toLocalDate());
        }
        vo.setEndDate(java.time.LocalDate.now());

        return vo;
    }

    @Override
    @Transactional
    public void submitAssessmentExecution(AssessmentExecutionSubmitDTO submitDTO) {
        // 创建评估执行详细记录
        AssessmentExecutionDetail detail = new AssessmentExecutionDetail();
        detail.setAssessmentPlanId(submitDTO.getAssessmentPlanId());
        detail.setPatientId(submitDTO.getPatientId());
        detail.setRecordDate(submitDTO.getRecordDate());

        detail.setQuestion1Answer(submitDTO.getQuestion1Answer());
        detail.setQuestion1SkipReason(submitDTO.getQuestion1SkipReason());

        detail.setQuestion2Answer(submitDTO.getQuestion2Answer());
        detail.setQuestion2Unknown(submitDTO.getQuestion2Unknown());

        detail.setQuestion3Answer(submitDTO.getQuestion3Answer());
        detail.setQuestion3SkipReason(submitDTO.getQuestion3SkipReason());

        detail.setQuestion4Answer(submitDTO.getQuestion4Answer());
        detail.setQuestion4Unknown(submitDTO.getQuestion4Unknown());

        detail.setQuestion5Answer(submitDTO.getQuestion5Answer());
        detail.setQuestion5SkipReason(submitDTO.getQuestion5SkipReason());

        detail.setQuestion6Answer(submitDTO.getQuestion6Answer());
        detail.setQuestion6Unknown(submitDTO.getQuestion6Unknown());

        detail.setQuestion7Answer(submitDTO.getQuestion7Answer());
        detail.setQuestion7Unknown(submitDTO.getQuestion7Unknown());

        detail.setExecutor(submitDTO.getExecutor());
        detail.setExecutionTime(submitDTO.getExecutionTime());
        detail.setStatus(submitDTO.getStatus());

        // 保存记录
        assessmentExecutionDetailMapper.insert(detail);
        businessEventPublisher.publishAssessmentEvent(
                "ASSESSMENT_EXECUTED",
                submitDTO.getAssessmentPlanId(),
                submitDTO.getPatientId(),
                submitDTO.getExecutor()
        );
    }
}
