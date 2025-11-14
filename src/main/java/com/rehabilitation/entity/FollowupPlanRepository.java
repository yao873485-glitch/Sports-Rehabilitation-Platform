package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 随访计划仓库实体类
 */
@ApiModel(description = "随访计划仓库")
@TableName("followup_plan_repository")
public class FollowupPlanRepository {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "计划名称")
    @TableField("plan_name")
    private String planName;

    @ApiModelProperty(value = "关联项目ID")
    @TableField("linked_project_id")
    private Long linkedProjectId;

    @ApiModelProperty(value = "随访项目名称")
    @TableField("linked_project")
    private String linkedProject;

    @ApiModelProperty(value = "当前版本号")
    @TableField("version_number")
    private String versionNumber;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "最近一次修改时间")
    @TableField("last_modified_time")
    private LocalDateTime lastModifiedTime;

    @ApiModelProperty(value = "创建时间")
    @TableField("created_time")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "版本备注")
    @TableField("version_remark")
    private String versionRemark;

    @ApiModelProperty(value = "随访邀请二维码")
    @TableField("invitation_qr_code_url")
    private String invitationQrCodeUrl;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getLinkedProject() {
        return linkedProject;
    }

    public void setLinkedProject(String linkedProject) {
        this.linkedProject = linkedProject;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getVersionRemark() {
        return versionRemark;
    }

    public void setVersionRemark(String versionRemark) {
        this.versionRemark = versionRemark;
    }

    public String getInvitationQrCodeUrl() {
        return invitationQrCodeUrl;
    }

    public void setInvitationQrCodeUrl(String invitationQrCodeUrl) {
        this.invitationQrCodeUrl = invitationQrCodeUrl;
    }

    public Long getLinkedProjectId() {
        return linkedProjectId;
    }

    public void setLinkedProjectId(Long linkedProjectId) {
        this.linkedProjectId = linkedProjectId;
    }

    @Override
    public String toString() {
        return "FollowupPlanRepository{" +
                "id=" + id +
                ", planName='" + planName + '\'' +
                ", linkedProject='" + linkedProject + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", status='" + status + '\'' +
                ", lastModifiedTime=" + lastModifiedTime +
                ", createdTime=" + createdTime +
                ", versionRemark='" + versionRemark + '\'' +
                ", invitationQrCodeUrl='" + invitationQrCodeUrl + '\'' +
                '}';
    }
}