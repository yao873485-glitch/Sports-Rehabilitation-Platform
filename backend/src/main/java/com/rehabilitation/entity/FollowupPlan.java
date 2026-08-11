package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 随访计划实体类
 */
@Data
@TableName("followup_plan")
public class FollowupPlan {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 计划名称
     */
    @TableField("plan_name")
    private String planName;

    /**
     * 计划描述
     */
    @TableField("plan_description")
    private String planDescription;

    /**
     * 关联的随访项目ID
     */
    @TableField("linked_project_id")
    private Long linkedProjectId;

    /**
     * 关联的随访项目名称
     */
    @TableField("linked_project_name")
    private String linkedProjectName;

    /**
     * 可用团队
     */
    @TableField("available_team")
    private String availableTeam;

    /**
     * 所属机构
     */
    @TableField("owner_institution")
    private String ownerInstitution;

    /**
     * 首次随访基线
     */
    @TableField("first_followup_baseline")
    private String firstFollowupBaseline;

    /**
     * 版本号
     */
    @TableField("version_number")
    private String versionNumber;

    /**
     * 版本备注
     */
    @TableField("version_remark")
    private String versionRemark;

    /**
     * 状态：未发布、已发布、已停用
     */
    @TableField("status")
    private String status;

    /**
     * 随访邀请二维码URL
     */
    @TableField("invitation_qr_code_url")
    private String invitationQrCodeUrl;

    /**
     * 是否草稿：0-否，1-是
     */
    @TableField("is_draft")
    private Integer isDraft;

    /**
     * 操作人
     */
    @TableField("operator")
    private String operator;

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

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public Long getLinkedProjectId() {
        return linkedProjectId;
    }

    public void setLinkedProjectId(Long linkedProjectId) {
        this.linkedProjectId = linkedProjectId;
    }

    public String getLinkedProjectName() {
        return linkedProjectName;
    }

    public void setLinkedProjectName(String linkedProjectName) {
        this.linkedProjectName = linkedProjectName;
    }

    public String getAvailableTeam() {
        return availableTeam;
    }

    public void setAvailableTeam(String availableTeam) {
        this.availableTeam = availableTeam;
    }

    public String getOwnerInstitution() {
        return ownerInstitution;
    }

    public void setOwnerInstitution(String ownerInstitution) {
        this.ownerInstitution = ownerInstitution;
    }

    public String getFirstFollowupBaseline() {
        return firstFollowupBaseline;
    }

    public void setFirstFollowupBaseline(String firstFollowupBaseline) {
        this.firstFollowupBaseline = firstFollowupBaseline;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionRemark() {
        return versionRemark;
    }

    public void setVersionRemark(String versionRemark) {
        this.versionRemark = versionRemark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvitationQrCodeUrl() {
        return invitationQrCodeUrl;
    }

    public void setInvitationQrCodeUrl(String invitationQrCodeUrl) {
        this.invitationQrCodeUrl = invitationQrCodeUrl;
    }

    public Integer getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Integer isDraft) {
        this.isDraft = isDraft;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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
