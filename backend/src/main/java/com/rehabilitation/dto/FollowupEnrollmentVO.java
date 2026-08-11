package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 随访患者入组信息VO（包含关联的患者、计划、项目信息）
 */
@Data
public class FollowupEnrollmentVO {

    /**
     * 入组记录ID
     */
    private Long id;

    /**
     * 序号
     */
    private String serialNumber;

    // ========== 患者信息（来自patient表） ==========
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
     * 年龄（计算得出）
     */
    private Integer age;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 病历号
     */
    private String medicalRecordNo;

    /**
     * 病种
     */
    private String diseaseType;

    // ========== 随访计划信息（来自followup_plan_repository表） ==========
    /**
     * 随访计划ID
     */
    private Long followupPlanId;

    /**
     * 随访计划名称
     */
    private String followupPlanName;

    // ========== 随访项目信息（来自followup_program_master表） ==========
    /**
     * 随访项目ID
     */
    private Long followupProjectId;

    /**
     * 随访项目名称
     */
    private String followupProject;

    // ========== 随访团队信息（来自enrollment表） ==========
    /**
     * 随访团队
     */
    private String followupTeam;

    /**
     * 主负责医生
     */
    private String primaryDoctor;

    /**
     * 健康管理师
     */
    private String healthManager;

    // ========== 时间信息 ==========
    /**
     * 申请时间
     */
    private LocalDateTime applicationTime;

    /**
     * 加入项目时间
     */
    private LocalDateTime enrollmentTime;

    // ========== 状态信息 ==========
    /**
     * 随访状态
     */
    private String followupStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 来源
     */
    private String source;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    // 手动添加所有getter/setter避免Lombok问题
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public Long getFollowupPlanId() {
        return followupPlanId;
    }

    public void setFollowupPlanId(Long followupPlanId) {
        this.followupPlanId = followupPlanId;
    }

    public String getFollowupPlanName() {
        return followupPlanName;
    }

    public void setFollowupPlanName(String followupPlanName) {
        this.followupPlanName = followupPlanName;
    }

    public Long getFollowupProjectId() {
        return followupProjectId;
    }

    public void setFollowupProjectId(Long followupProjectId) {
        this.followupProjectId = followupProjectId;
    }

    public String getFollowupProject() {
        return followupProject;
    }

    public void setFollowupProject(String followupProject) {
        this.followupProject = followupProject;
    }

    public String getFollowupTeam() {
        return followupTeam;
    }

    public void setFollowupTeam(String followupTeam) {
        this.followupTeam = followupTeam;
    }

    public String getPrimaryDoctor() {
        return primaryDoctor;
    }

    public void setPrimaryDoctor(String primaryDoctor) {
        this.primaryDoctor = primaryDoctor;
    }

    public String getHealthManager() {
        return healthManager;
    }

    public void setHealthManager(String healthManager) {
        this.healthManager = healthManager;
    }

    public LocalDateTime getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(LocalDateTime applicationTime) {
        this.applicationTime = applicationTime;
    }

    public LocalDateTime getEnrollmentTime() {
        return enrollmentTime;
    }

    public void setEnrollmentTime(LocalDateTime enrollmentTime) {
        this.enrollmentTime = enrollmentTime;
    }

    public String getFollowupStatus() {
        return followupStatus;
    }

    public void setFollowupStatus(String followupStatus) {
        this.followupStatus = followupStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
