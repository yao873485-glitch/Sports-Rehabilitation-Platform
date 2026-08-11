package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 随访项目配置实体
 */
@Data
@TableName("followup_project_config")
public class FollowupProjectConfig {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目标号（10位随机字符串）
     */
    @TableField("project_code")
    private String projectCode;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 项目简介
     */
    @TableField("project_brief")
    private String projectBrief;

    /**
     * 项目介绍
     */
    @TableField("project_description")
    private String projectDescription;

    /**
     * 项目病种
     */
    @TableField("disease_type")
    private String diseaseType;

    /**
     * 项目类型
     */
    @TableField("project_type")
    private String projectType;

    /**
     * 患者咨询次数
     */
    @TableField("consultation_times")
    private Integer consultationTimes;

    /**
     * 单次时长（小时）
     */
    @TableField("single_duration")
    private Integer singleDuration;

    /**
     * 绑定随访计划
     */
    @TableField("bind_plan")
    private String bindPlan;

    /**
     * 操作人
     */
    @TableField("operator")
    private String operator;

    /**
     * 是否发布：0-否，1-是
     */
    @TableField("is_published")
    private Integer isPublished;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

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

    public Integer getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Integer isPublished) {
        this.isPublished = isPublished;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
