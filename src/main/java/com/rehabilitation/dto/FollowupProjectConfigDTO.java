package com.rehabilitation.dto;

import lombok.Data;

/**
 * 随访项目配置DTO（用于接收前端数据）
 */
@Data
public class FollowupProjectConfigDTO {

    /**
     * 主键ID（更新时需要）
     */
    private Long id;

    /**
     * 项目标号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目简介
     */
    private String projectBrief;

    /**
     * 项目介绍
     */
    private String projectDescription;

    /**
     * 项目病种
     */
    private String diseaseType;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * 患者咨询次数
     */
    private Integer consultationTimes;

    /**
     * 单次时长（小时）
     */
    private Integer singleDuration;

    /**
     * 绑定随访计划
     */
    private String bindPlan;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 是否发布
     */
    private Boolean isPublished;

    // Manual getters and setters to avoid Lombok issues

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectBrief() {
        return projectBrief;
    }

    public void setProjectBrief(String projectBrief) {
        this.projectBrief = projectBrief;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Integer getConsultationTimes() {
        return consultationTimes;
    }

    public void setConsultationTimes(Integer consultationTimes) {
        this.consultationTimes = consultationTimes;
    }

    public Integer getSingleDuration() {
        return singleDuration;
    }

    public void setSingleDuration(Integer singleDuration) {
        this.singleDuration = singleDuration;
    }

    public String getBindPlan() {
        return bindPlan;
    }

    public void setBindPlan(String bindPlan) {
        this.bindPlan = bindPlan;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }
}
