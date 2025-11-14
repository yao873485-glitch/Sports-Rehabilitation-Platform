package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 处方清单DTO
 */
@Data
public class PrescriptionListDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 运动项目名称
     */
    private String exerciseName;

    /**
     * 运动类型（有氧运动、力量训练、柔韧性训练、平衡训练）
     */
    private String exerciseType;

    /**
     * 频次（如：每日、每周3次等）
     */
    private String frequency;

    /**
     * 时长（分钟）
     */
    private Integer duration;

    /**
     * 强度（低强度、中等强度、高强度）
     */
    private String intensity;

    /**
     * 组数
     */
    private Integer sets;

    /**
     * 次数
     */
    private Integer reps;

    /**
     * 休息时间（秒）
     */
    private Integer restTime;

    /**
     * 目标部位（如：下肢、上肢、全身等）
     */
    private String targetArea;

    /**
     * 动作要领说明
     */
    private String instructions;

    /**
     * 执行状态（completed-已完成，in_progress-进行中，pending-待执行）
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 方案ID
     */
    private String schemeId;

    /**
     * 获取状态描述
     */
    public String getStatusDesc() {
        switch (status) {
            case "completed":
                return "已完成";
            case "in_progress":
                return "进行中";
            case "pending":
                return "待执行";
            default:
                return status;
        }
    }
}