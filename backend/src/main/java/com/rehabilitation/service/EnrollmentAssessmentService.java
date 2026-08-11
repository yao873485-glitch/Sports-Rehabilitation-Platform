package com.rehabilitation.service;

import com.rehabilitation.entity.EnrollmentAssessment;

/**
 * 入组评估服务接口
 */
public interface EnrollmentAssessmentService {

    /**
     * 保存或更新入组评估
     * @param enrollmentAssessment 入组评估信息
     * @return 保存后的入组评估
     */
    EnrollmentAssessment saveOrUpdate(EnrollmentAssessment enrollmentAssessment);

    /**
     * 根据患者ID查询入组评估
     * @param patientId 患者ID
     * @return 入组评估
     */
    EnrollmentAssessment getByPatientId(Integer patientId);

    /**
     * 根据患者ID删除入组评估
     * @param patientId 患者ID
     * @return 是否删除成功
     */
    boolean deleteByPatientId(Integer patientId);
}
