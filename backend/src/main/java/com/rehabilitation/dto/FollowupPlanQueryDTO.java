package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;

/**
 * 随访计划查询DTO
 */
public class FollowupPlanQueryDTO extends PageQuery {

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 状态
     */
    private String status;

    // Manual getters and setters to avoid Lombok issues

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}