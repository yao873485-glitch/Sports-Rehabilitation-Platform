package com.rehabilitation.dto;

/**
 * 方案创建DTO
 */
public class SchemeCreateDTO {

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 档案号
     */
    private String medicalRecordNo;

    /**
     * 方案周期（月）
     */
    private Integer schemeCycle;

    /**
     * 病种类型
     */
    private String diseaseType;

    // Getter and Setter methods
    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getMedicalRecordNo() {
        return medicalRecordNo;
    }

    public void setMedicalRecordNo(String medicalRecordNo) {
        this.medicalRecordNo = medicalRecordNo;
    }

    public Integer getSchemeCycle() {
        return schemeCycle;
    }

    public void setSchemeCycle(Integer schemeCycle) {
        this.schemeCycle = schemeCycle;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }
}
