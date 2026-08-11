package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 随访患者查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FollowupQueryDTO extends PageQuery {

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 申请开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applicationStartTime;

    /**
     * 申请结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applicationEndTime;

    /**
     * 加入项目开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enrollmentStartTime;

    /**
     * 加入项目结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enrollmentEndTime;

    /**
     * 年龄范围
     */
    private String ageRange;

    /**
     * 随访计划
     */
    private String followupPlanName;

    /**
     * 随访状态
     */
    private String followupStatus;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 主负责医生
     */
    private String primaryDoctor;

    /**
     * 搜索关键词（姓名、电话、病历号）
     */
    private String searchKeyword;

    // 手动添加getter/setter避免Lombok问题
    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPrimaryDoctor() {
        return primaryDoctor;
    }

    public void setPrimaryDoctor(String primaryDoctor) {
        this.primaryDoctor = primaryDoctor;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDateTime getApplicationStartTime() {
        return applicationStartTime;
    }

    public void setApplicationStartTime(LocalDateTime applicationStartTime) {
        this.applicationStartTime = applicationStartTime;
    }

    public LocalDateTime getApplicationEndTime() {
        return applicationEndTime;
    }

    public void setApplicationEndTime(LocalDateTime applicationEndTime) {
        this.applicationEndTime = applicationEndTime;
    }

    public LocalDateTime getEnrollmentStartTime() {
        return enrollmentStartTime;
    }

    public void setEnrollmentStartTime(LocalDateTime enrollmentStartTime) {
        this.enrollmentStartTime = enrollmentStartTime;
    }

    public LocalDateTime getEnrollmentEndTime() {
        return enrollmentEndTime;
    }

    public void setEnrollmentEndTime(LocalDateTime enrollmentEndTime) {
        this.enrollmentEndTime = enrollmentEndTime;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getFollowupPlanName() {
        return followupPlanName;
    }

    public void setFollowupPlanName(String followupPlanName) {
        this.followupPlanName = followupPlanName;
    }

    public String getFollowupStatus() {
        return followupStatus;
    }

    public void setFollowupStatus(String followupStatus) {
        this.followupStatus = followupStatus;
    }
}
