package com.rehabilitation.dto;

import lombok.Data;

/**
 * 随访计划DTO（用于接收前端数据）
 */
@Data
public class FollowupPlanDTO {

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 计划描述
     */
    private String planDescription;

    /**
     * 关联的随访项目ID
     */
    private Long linkedProjectId;

    /**
     * 可用团队
     */
    private String availableTeam;

    /**
     * 所属机构
     */
    private String ownerInstitution;

    /**
     * 首次随访基线
     */
    private String firstFollowupBaseline;

    /**
     * 是否草稿：true-草稿，false-发布
     */
    private Boolean isDraft;

    /**
     * 操作人
     */
    private String operator;

    // Manual getters and setters to avoid Lombok issues

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

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
