package com.rehabilitation.dto;

/**
 * 入组审核状态保存 DTO
 */
public class EnrollmentAuditDTO {

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 审核状态：PENDING/PASSED/REJECTED
     */
    private String status;

    /**
     * 拒绝原因
     */
    private String rejectReason;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}

