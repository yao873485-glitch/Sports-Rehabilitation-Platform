package com.rehabilitation.dto;

import java.time.LocalDate;

/**
 * 评估执行信息VO（用于执行弹窗显示患者信息）
 */
public class AssessmentExecutionInfoVO {

    private Integer assessmentPlanId;
    private Integer patientId;
    private String patientName;
    private String gender;
    private Integer age;
    private String idCard;
    private String phone;
    private String medicalRecordNo;
    private LocalDate enrollmentDate;
    private LocalDate endDate;
    private String assessmentName;

    // Getter and Setter methods
    public Integer getAssessmentPlanId() {
        return assessmentPlanId;
    }

    public void setAssessmentPlanId(Integer assessmentPlanId) {
        this.assessmentPlanId = assessmentPlanId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMedicalRecordNo() {
        return medicalRecordNo;
    }

    public void setMedicalRecordNo(String medicalRecordNo) {
        this.medicalRecordNo = medicalRecordNo;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }
}
