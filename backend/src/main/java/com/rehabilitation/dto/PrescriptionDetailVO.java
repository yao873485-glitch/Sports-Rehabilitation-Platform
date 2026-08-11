package com.rehabilitation.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 处方详情视图对象（包含患者信息和运动项目列表）
 */
public class PrescriptionDetailVO {

    // ========== 患者基本信息 ==========
    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 出生日期
     */
    private String birthDate;

    /**
     * 档案号
     */
    private String medicalRecordNo;

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 诊断
     */
    private String diagnosis;

    /**
     * 入组机构
     */
    private String enrollmentInstitution;

    // ========== 处方信息 ==========
    /**
     * 处方ID
     */
    private Integer prescriptionId;

    /**
     * 方案ID
     */
    private Long schemeId;

    /**
     * 执行机构
     */
    private String executionInstitution;

    /**
     * 处方状态：1-未开始, 2-执行中, 3-已完成, 4-已结束
     */
    private Integer prescriptionStatus;

    /**
     * 开方医生
     */
    private String prescribingDoctor;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    // ========== 运动项目列表 ==========
    /**
     * 运动项目列表
     */
    private List<PrescriptionExerciseVO> exerciseList;

    // 手动添加 getter/setter 避免 Lombok 问题
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getMedicalRecordNo() {
        return medicalRecordNo;
    }

    public void setMedicalRecordNo(String medicalRecordNo) {
        this.medicalRecordNo = medicalRecordNo;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getEnrollmentInstitution() {
        return enrollmentInstitution;
    }

    public void setEnrollmentInstitution(String enrollmentInstitution) {
        this.enrollmentInstitution = enrollmentInstitution;
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Long getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(Long schemeId) {
        this.schemeId = schemeId;
    }

    public String getExecutionInstitution() {
        return executionInstitution;
    }

    public void setExecutionInstitution(String executionInstitution) {
        this.executionInstitution = executionInstitution;
    }

    public Integer getPrescriptionStatus() {
        return prescriptionStatus;
    }

    public void setPrescriptionStatus(Integer prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

    public String getPrescribingDoctor() {
        return prescribingDoctor;
    }

    public void setPrescribingDoctor(String prescribingDoctor) {
        this.prescribingDoctor = prescribingDoctor;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public List<PrescriptionExerciseVO> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(List<PrescriptionExerciseVO> exerciseList) {
        this.exerciseList = exerciseList;
    }
}
