package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 随访患者入组DTO
 */
@Data
public class FollowupEnrollmentDTO {

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 随访计划ID
     */
    private Long followupPlanId;

    /**
     * 随访项目ID
     */
    private Long followupProjectId;

    /**
     * 随访计划名称（用于查询）
     */
    private String followupPlanName;

    /**
     * 随访项目名称（用于查询）
     */
    private String followupProjectName;

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

    /**
     * 申请时间
     */
    private LocalDateTime applicationTime;

    /**
     * 加入项目时间
     */
    private LocalDateTime enrollmentTime;

    /**
     * 随访状态：待入组/随访中/已暂停/已完成/已退出
     */
    private String followupStatus;

    /**
     * 入组备注
     */
    private String enrollmentRemark;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 来源：医生推荐、患者自主申请等
     */
    private String source;

    // 手动添加所有getter/setter避免Lombok问题
    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Long getFollowupPlanId() {
        return followupPlanId;
    }

    public void setFollowupPlanId(Long followupPlanId) {
        this.followupPlanId = followupPlanId;
    }

    public Long getFollowupProjectId() {
        return followupProjectId;
    }

    public void setFollowupProjectId(Long followupProjectId) {
        this.followupProjectId = followupProjectId;
    }

    public String getFollowupPlanName() {
        return followupPlanName;
    }

    public void setFollowupPlanName(String followupPlanName) {
        this.followupPlanName = followupPlanName;
    }

    public String getFollowupProjectName() {
        return followupProjectName;
    }

    public void setFollowupProjectName(String followupProjectName) {
        this.followupProjectName = followupProjectName;
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

    public String getEnrollmentRemark() {
        return enrollmentRemark;
    }

    public void setEnrollmentRemark(String enrollmentRemark) {
        this.enrollmentRemark = enrollmentRemark;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "FollowupEnrollmentDTO{" +
                "patientId=" + patientId +
                ", followupPlanName='" + followupPlanName + '\'' +
                ", followupProjectName='" + followupProjectName + '\'' +
                ", followupTeam='" + followupTeam + '\'' +
                ", primaryDoctor='" + primaryDoctor + '\'' +
                ", healthManager='" + healthManager + '\'' +
                ", applicationTime=" + applicationTime +
                ", enrollmentTime=" + enrollmentTime +
                ", followupStatus='" + followupStatus + '\'' +
                ", enrollmentRemark='" + enrollmentRemark + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
