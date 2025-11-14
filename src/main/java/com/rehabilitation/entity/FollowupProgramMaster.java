package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 随访项目主数据实体类
 */
@ApiModel(description = "随访项目主数据")
@TableName("followup_program_master")
public class FollowupProgramMaster {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "项目编号")
    @TableField("project_code")
    private String projectCode;

    @ApiModelProperty(value = "项目名称")
    @TableField("project_name")
    private String projectName;

    @ApiModelProperty(value = "项目介绍")
    @TableField("project_description")
    private String projectDescription;

    @ApiModelProperty(value = "绑定随访计划")
    @TableField("linked_followup_plan")
    private String linkedFollowupPlan;

    @ApiModelProperty(value = "最后修改时间")
    @TableField("last_modified_time")
    private LocalDateTime lastModifiedTime;

    @ApiModelProperty(value = "创建时间")
    @TableField("created_time")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "操作人")
    @TableField("operator")
    private String operator;

    @ApiModelProperty(value = "是否发布")
    @TableField("is_published")
    private Integer isPublished;

    // Getters and Setters
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

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getLinkedFollowupPlan() {
        return linkedFollowupPlan;
    }

    public void setLinkedFollowupPlan(String linkedFollowupPlan) {
        this.linkedFollowupPlan = linkedFollowupPlan;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
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

    @Override
    public String toString() {
        return "FollowupProgramMaster{" +
                "id=" + id +
                ", projectCode='" + projectCode + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                ", linkedFollowupPlan='" + linkedFollowupPlan + '\'' +
                ", lastModifiedTime=" + lastModifiedTime +
                ", createdTime=" + createdTime +
                ", operator='" + operator + '\'' +
                ", isPublished=" + isPublished +
                '}';
    }
}