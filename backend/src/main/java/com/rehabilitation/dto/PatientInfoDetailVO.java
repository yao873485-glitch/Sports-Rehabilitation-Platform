package com.rehabilitation.dto;

import com.rehabilitation.entity.DiseaseRecord;
import com.rehabilitation.entity.EnrollmentAssessment;
import com.rehabilitation.entity.HealthRecord;
import com.rehabilitation.entity.PatientDetail;
import com.rehabilitation.entity.PatientInfo;

/**
 * 患者详情VO
 */
public class PatientInfoDetailVO {

    /**
     * 基本信息
     */
    private PatientInfo basicInfo;

    /**
     * 补充信息
     */
    private PatientDetail detailInfo;

    /**
     * 健康档案
     */
    private HealthRecord healthRecord;

    /**
     * 专病档案
     */
    private DiseaseRecord diseaseRecord;

    /**
     * 入组评估记录
     */
    private EnrollmentAssessment enrollmentAssessment;

    /**
     * 入组审核状态（PENDING/PASSED/REJECTED）
     */
    private String enrollmentStatus;

    /**
     * 入组审核拒绝原因
     */
    private String enrollmentRejectReason;

    public PatientInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(PatientInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public PatientDetail getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(PatientDetail detailInfo) {
        this.detailInfo = detailInfo;
    }

    public HealthRecord getHealthRecord() {
        return healthRecord;
    }

    public void setHealthRecord(HealthRecord healthRecord) {
        this.healthRecord = healthRecord;
    }

    public DiseaseRecord getDiseaseRecord() {
        return diseaseRecord;
    }

    public void setDiseaseRecord(DiseaseRecord diseaseRecord) {
        this.diseaseRecord = diseaseRecord;
    }

    public EnrollmentAssessment getEnrollmentAssessment() {
        return enrollmentAssessment;
    }

    public void setEnrollmentAssessment(EnrollmentAssessment enrollmentAssessment) {
        this.enrollmentAssessment = enrollmentAssessment;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public String getEnrollmentRejectReason() {
        return enrollmentRejectReason;
    }

    public void setEnrollmentRejectReason(String enrollmentRejectReason) {
        this.enrollmentRejectReason = enrollmentRejectReason;
    }
}
