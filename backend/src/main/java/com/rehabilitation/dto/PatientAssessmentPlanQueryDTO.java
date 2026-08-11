package com.rehabilitation.dto;

/**
 * 患者评定方案查询DTO
 */
public class PatientAssessmentPlanQueryDTO {

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 评定状态
     */
    private String status;

    /**
     * 入组机构
     */
    private String enrollmentInstitution;

    /**
     * 开方医生
     */
    private String prescribingDoctor;

    /**
     * 患者ID（用于精确查询指定患者的评定方案）
     */
    private Integer patientId;

    /**
     * 搜索关键词（患者姓名、电话或档案号）
     */
    private String searchKeyword;

    // Getter and Setter methods
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnrollmentInstitution() {
        return enrollmentInstitution;
    }

    public void setEnrollmentInstitution(String enrollmentInstitution) {
        this.enrollmentInstitution = enrollmentInstitution;
    }

    public String getPrescribingDoctor() {
        return prescribingDoctor;
    }

    public void setPrescribingDoctor(String prescribingDoctor) {
        this.prescribingDoctor = prescribingDoctor;
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