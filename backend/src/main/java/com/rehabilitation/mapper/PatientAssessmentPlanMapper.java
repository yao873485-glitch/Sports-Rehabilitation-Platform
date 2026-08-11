package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.AssessmentPlanDetailVO;
import com.rehabilitation.dto.PatientAssessmentPlanQueryDTO;
import com.rehabilitation.dto.PatientAssessmentPlanVO;
import com.rehabilitation.entity.PatientAssessmentPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 患者评定方案Mapper接口
 */
@Mapper
public interface PatientAssessmentPlanMapper extends BaseMapper<PatientAssessmentPlan> {

    /**
     * 分页查询患者评定方案列表
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<PatientAssessmentPlanVO> selectPatientAssessmentPlanPage(
            Page<PatientAssessmentPlanVO> page,
            @Param("query") PatientAssessmentPlanQueryDTO query);

    /**
     * 根据ID查询患者评定方案详情
     *
     * @param id 主键ID
     * @return 患者评定方案详情
     */
    PatientAssessmentPlanVO selectPatientAssessmentPlanById(@Param("id") Integer id);

    /**
     * 根据评定计划ID查询完整详情（包含患者信息）
     *
     * @param id 评定计划ID
     * @return 评定计划详情（含患者信息）
     */
    AssessmentPlanDetailVO selectAssessmentPlanDetailById(@Param("id") Integer id);
}