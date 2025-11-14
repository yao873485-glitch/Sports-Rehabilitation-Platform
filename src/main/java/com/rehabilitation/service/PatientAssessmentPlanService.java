package com.rehabilitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.dto.AssessmentPlanSaveDTO;
import com.rehabilitation.dto.PatientAssessmentPlanQueryDTO;
import com.rehabilitation.dto.PatientAssessmentPlanVO;
import com.rehabilitation.entity.PatientAssessmentPlan;

import java.util.List;

/**
 * 患者评定方案Service接口
 */
public interface PatientAssessmentPlanService {

    /**
     * 分页查询患者评定方案列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<PatientAssessmentPlanVO> getPatientAssessmentPlanPage(PatientAssessmentPlanQueryDTO query);

    /**
     * 根据ID查询患者评定方案详情
     *
     * @param id 主键ID
     * @return 患者评定方案详情
     */
    PatientAssessmentPlanVO getPatientAssessmentPlanById(Integer id);

    /**
     * 新增患者评定方案
     *
     * @param patientAssessmentPlan 患者评定方案
     */
    void addPatientAssessmentPlan(PatientAssessmentPlan patientAssessmentPlan);

    /**
     * 更新患者评定方案
     *
     * @param patientAssessmentPlan 患者评定方案
     */
    void updatePatientAssessmentPlan(PatientAssessmentPlan patientAssessmentPlan);

    /**
     * 结束患者评定方案
     *
     * @param id 主键ID
     */
    void endPatientAssessmentPlan(Integer id);

    /**
     * 保存评定计划
     *
     * @param assessmentPlanSaveDTO 评定计划保存DTO
     * @return 评定方案ID
     */
    Long saveAssessmentPlan(AssessmentPlanSaveDTO assessmentPlanSaveDTO);

    /**
     * 获取所有病种类型
     *
     * @return 病种类型列表
     */
    List<String> getAllDiseaseTypes();

    /**
     * 获取所有机构列表
     *
     * @return 机构列表
     */
    List<String> getAllInstitutions();
}