package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.AssessmentPlanDetailVO;
import com.rehabilitation.dto.AssessmentPlanSaveDTO;
import com.rehabilitation.dto.PatientAssessmentPlanQueryDTO;
import com.rehabilitation.dto.PatientAssessmentPlanVO;
import com.rehabilitation.entity.AssessmentExecutionRecord;
import com.rehabilitation.entity.MedicalScheme;
import com.rehabilitation.entity.PatientAssessmentPlan;
import com.rehabilitation.entity.PatientInfo;
import com.rehabilitation.mapper.AssessmentExecutionRecordMapper;
import com.rehabilitation.mapper.MedicalSchemeMapper;
import com.rehabilitation.mapper.PatientAssessmentPlanMapper;
import com.rehabilitation.mapper.PatientInfoMapper;
import com.rehabilitation.service.PatientAssessmentPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 患者评定方案Service实现类
 */
@Service
public class PatientAssessmentPlanServiceImpl implements PatientAssessmentPlanService {

    private static final Logger log = LoggerFactory.getLogger(PatientAssessmentPlanServiceImpl.class);

    private final PatientAssessmentPlanMapper patientAssessmentPlanMapper;
    private final AssessmentExecutionRecordMapper assessmentExecutionRecordMapper;
    private final PatientInfoMapper patientInfoMapper;
    private final MedicalSchemeMapper medicalSchemeMapper;

    @Autowired
    public PatientAssessmentPlanServiceImpl(
            PatientAssessmentPlanMapper patientAssessmentPlanMapper,
            AssessmentExecutionRecordMapper assessmentExecutionRecordMapper,
            PatientInfoMapper patientInfoMapper,
            MedicalSchemeMapper medicalSchemeMapper) {
        this.patientAssessmentPlanMapper = patientAssessmentPlanMapper;
        this.assessmentExecutionRecordMapper = assessmentExecutionRecordMapper;
        this.patientInfoMapper = patientInfoMapper;
        this.medicalSchemeMapper = medicalSchemeMapper;
    }

    @Override
    public IPage<PatientAssessmentPlanVO> getPatientAssessmentPlanPage(PatientAssessmentPlanQueryDTO query) {
        log.info("======== 开始查询患者评定方案列表 ========");
        log.info("查询参数: patientId={}, diseaseType={}, status={}, enrollmentInstitution={}, prescribingDoctor={}, searchKeyword={}, pageNum={}, pageSize={}",
            query.getPatientId(), query.getDiseaseType(), query.getStatus(),
            query.getEnrollmentInstitution(), query.getPrescribingDoctor(),
            query.getSearchKeyword(), query.getPageNum(), query.getPageSize());

        Page<PatientAssessmentPlanVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<PatientAssessmentPlanVO> result = patientAssessmentPlanMapper.selectPatientAssessmentPlanPage(page, query);

        log.info("查询结果: 总记录数={}, 当前页={}, 每页条数={}, 返回记录数={}",
            result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords().size());

        if (result.getRecords().isEmpty()) {
            log.warn("!!!!! 查询结果为空，没有找到任何评定方案记录 !!!!!");
        } else {
            log.info("查询到的评定方案列表:");
            for (PatientAssessmentPlanVO vo : result.getRecords()) {
                log.info("  - 方案ID={}, 评定名称={}, 患者姓名={}, 档案号={}",
                    vo.getId(), vo.getAssessmentName(), vo.getPatientName(), vo.getMedicalRecordNumber());
            }
        }
        log.info("======== 患者评定方案列表查询完成 ========");

        return result;
    }

    @Override
    public PatientAssessmentPlanVO getPatientAssessmentPlanById(Integer id) {
        return patientAssessmentPlanMapper.selectPatientAssessmentPlanById(id);
    }

    @Override
    public AssessmentPlanDetailVO getAssessmentPlanDetailById(Integer id) {
        return patientAssessmentPlanMapper.selectAssessmentPlanDetailById(id);
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
    public void deletePatientAssessmentPlan(Integer id) {
        log.info("======== 开始删除患者评定方案 ========");
        log.info("评定方案ID={}", id);

        // 1. 删除评定执行记录
        LambdaQueryWrapper<AssessmentExecutionRecord> recordWrapper = new LambdaQueryWrapper<>();
        recordWrapper.eq(AssessmentExecutionRecord::getAssessmentPlanId, id);
        assessmentExecutionRecordMapper.delete(recordWrapper);
        log.info("已删除关联的评定执行记录");

        // 2. 删除评定方案
        patientAssessmentPlanMapper.deleteById(id);
        log.info("已删除评定方案");

        log.info("======== 患者评定方案删除完成 ========");
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

        // 直接使用前端传来的executionInstitution（保存时为null，提交时有值）
        String executionInstitution = assessmentPlanSaveDTO.getExecutionInstitution();
        // 根据执行机构是否为空设置状态：为空=1(已创建)，不为空=2(执行中)
        int status = (executionInstitution == null || executionInstitution.isEmpty()) ? 1 : 2;

        // 3. 为每个评定项目创建记录
        for (AssessmentPlanSaveDTO.AssessmentItemDTO assessmentItem : assessmentPlanSaveDTO.getAssessmentList()) {
            // 创建评定方案记录
            PatientAssessmentPlan plan = new PatientAssessmentPlan();

            // 通过 schemeId（字符串）查询 medical_scheme 表获取主键 ID
            if (assessmentPlanSaveDTO.getSchemeId() != null && !assessmentPlanSaveDTO.getSchemeId().isEmpty()) {
                QueryWrapper<MedicalScheme> schemeWrapper = new QueryWrapper<>();
                schemeWrapper.eq("scheme_id", assessmentPlanSaveDTO.getSchemeId());
                MedicalScheme medicalScheme = medicalSchemeMapper.selectOne(schemeWrapper);
                if (medicalScheme != null) {
                    try {
                        java.lang.reflect.Field idField = medicalScheme.getClass().getDeclaredField("id");
                        idField.setAccessible(true);
                        Long schemeIdValue = (Long) idField.get(medicalScheme);
                        plan.setMedicalSchemeId(schemeIdValue);
                    } catch (Exception e) {
                        plan.setMedicalSchemeId(null);
                    }
                }
            }

            plan.setPatientId(assessmentPlanSaveDTO.getPatientId());
            plan.setAssessmentName(assessmentName);
            plan.setExecutionInstitution(executionInstitution);
            plan.setStatus(status);
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
            // 同样使用前端传来的executionInstitution
            record.setExecutionInstitution(executionInstitution);
            // 使用与评定计划相同的状态
            record.setStatus(status);
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