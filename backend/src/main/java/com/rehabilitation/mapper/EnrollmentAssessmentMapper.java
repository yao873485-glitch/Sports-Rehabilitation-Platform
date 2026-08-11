package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rehabilitation.entity.EnrollmentAssessment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 入组评估 Mapper 接口
 */
@Mapper
public interface EnrollmentAssessmentMapper extends BaseMapper<EnrollmentAssessment> {

    /**
     * 根据患者ID查询入组评估
     * @param patientId 患者ID
     * @return 入组评估
     */
    EnrollmentAssessment selectByPatientId(@Param("patientId") Integer patientId);

    /**
     * 根据患者ID删除入组评估
     * @param patientId 患者ID
     * @return 删除行数
     */
    int deleteByPatientId(@Param("patientId") Integer patientId);
}
