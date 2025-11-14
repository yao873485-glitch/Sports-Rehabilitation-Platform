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
}
