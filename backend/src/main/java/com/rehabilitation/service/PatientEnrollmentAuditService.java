package com.rehabilitation.service;

import com.rehabilitation.entity.PatientEnrollmentAudit;

public interface PatientEnrollmentAuditService {

    /**
     * 根据患者ID获取审核记录
     */
    PatientEnrollmentAudit getByPatientId(Integer patientId);

    /**
     * 保存或更新审核状态
     */
    boolean saveOrUpdateStatus(Integer patientId, Integer status, String rejectReason);
}

