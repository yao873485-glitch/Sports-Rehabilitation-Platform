package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;

/**
 * 运动处方查询DTO
 */
public class PrescriptionQueryDTO extends PageQuery {

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 处方状态
     */
    private String prescriptionStatus;

    /**
     * 入组机构
     */
    private String enrollmentInstitution;

    /**
     * 执行机构
     */
    private String executionInstitution;

    /**
     * 患者ID（用于精确查询指定患者的处方）
     */
    private Integer patientId;

    /**
     * 搜索关键字（患者姓名、电话或档案号）
     */
    private String searchKeyword;

    // 手动添加getter和setter方法以避免Lombok问题
    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getPrescriptionStatus() {
        return prescriptionStatus;
    }

    public void setPrescriptionStatus(String prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

    public String getEnrollmentInstitution() {
        return enrollmentInstitution;
    }

    public void setEnrollmentInstitution(String enrollmentInstitution) {
        this.enrollmentInstitution = enrollmentInstitution;
    }

    public String getExecutionInstitution() {
        return executionInstitution;
    }

    public void setExecutionInstitution(String executionInstitution) {
        this.executionInstitution = executionInstitution;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
}
