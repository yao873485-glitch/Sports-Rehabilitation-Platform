package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 运动处方展示VO
 */
@Data
public class ExercisePrescriptionVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 运动方式
     */
    private String exerciseMethod;

    /**
     * 运动类型
     */
    private String exerciseType;

    /**
     * 动作图式
     */
    private String actionDiagram;

    /**
     * 注意事项
     */
    private String precautions;

    /**
     * 运动进阶
     */
    private String progressionLevel;

    /**
     * 运动场景
     */
    private String exerciseScene;

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