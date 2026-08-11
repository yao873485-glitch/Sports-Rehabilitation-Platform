package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rehabilitation.entity.PatientEnrollmentAudit;
import com.rehabilitation.mapper.PatientEnrollmentAuditMapper;
import com.rehabilitation.service.PatientEnrollmentAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PatientEnrollmentAuditServiceImpl implements PatientEnrollmentAuditService {

    @Autowired
    private PatientEnrollmentAuditMapper patientEnrollmentAuditMapper;

    @Override
    public PatientEnrollmentAudit getByPatientId(Integer patientId) {
        if (patientId == null) {
            return null;
        }
        return patientEnrollmentAuditMapper.selectOne(
                new LambdaQueryWrapper<PatientEnrollmentAudit>().eq(PatientEnrollmentAudit::getPatientId, patientId)
        );
    }

    @Override
    public boolean saveOrUpdateStatus(Integer patientId, Integer status, String rejectReason) {
        if (patientId == null) {
            return false;
        }
        String finalReason = null;
        if (status != null && status == 2 && rejectReason != null) {
            finalReason = rejectReason.trim();
            if (finalReason.length() > 200) {
                finalReason = finalReason.substring(0, 200);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        PatientEnrollmentAudit existing = getByPatientId(patientId);
        if (existing == null) {
            PatientEnrollmentAudit record = new PatientEnrollmentAudit();
            record.setPatientId(patientId);
            record.setStatus(status == null ? 0 : status);
            record.setRejectReason(finalReason);
            record.setCreatedTime(now);
            record.setUpdatedTime(now);
            return patientEnrollmentAuditMapper.insert(record) > 0;
        } else {
            existing.setStatus(status == null ? 0 : status);
            existing.setRejectReason(finalReason);
            existing.setUpdatedTime(now);
            return patientEnrollmentAuditMapper.updateById(existing) > 0;
        }
    }
}

