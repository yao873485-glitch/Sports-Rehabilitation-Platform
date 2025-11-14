package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 随访计划查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FollowupPlanQueryDTO extends PageQuery {

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 状态
     */
    private String status;
}