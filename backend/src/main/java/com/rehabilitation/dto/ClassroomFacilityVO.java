package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 教室设施信息展示VO
 */
@Data
public class ClassroomFacilityVO {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 运动类型
     */
    private String exerciseType;

    /**
     * 教室名称
     */
    private String classroomName;

    /**
     * 课程时长（分钟）
     */
    private Integer classDuration;

    /**
     * 课时配置
     */
    private String classScheduleConfig;

    /**
     * 预约上限
     */
    private Integer reservationLimit;

    /**
     * 教室编码
     */
    private String classroomCode;

    /**
     * 状态
     */
    private String status;

    /**
     * 上次操作时间
     */
    private LocalDateTime lastOperationTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
}