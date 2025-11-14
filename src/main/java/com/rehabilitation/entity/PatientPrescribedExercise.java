package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 患者运动处方执行记录实体
 */
@Data
@TableName("patient_prescribed_exercise")
public class PatientPrescribedExercise {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 患者ID
     */
    @TableField("patient_id")
    private Integer patientId;

    /**
     * 对应运动处方ID
     */
    @TableField("exercise_prescription_id")
    private Integer exercisePrescriptionId;

    /**
     * 执行机构
     */
    @TableField("execution_institution")
    private String executionInstitution;

    /**
     * 状态：1-未开始, 2-执行中, 3-已完成, 4-已结束
     */
    @TableField("prescription_status")
    private Integer prescriptionStatus;

    /**
     * 开方医生
     */
    @TableField("prescribing_doctor")
    private String prescribingDoctor;

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

    /**
     * 运动场景：机构/居家
     */
    @TableField("exercise_scene")
    private String exerciseScene;

    /**
     * 运动类型：如 有氧运动、呼吸训练、平衡训练、抗阻训练、柔韧性训练
     */
    @TableField("exercise_type")
    private String exerciseType;

    /**
     * 运动方式：如 单腿站立（扶椅背）、直线行走、弹力带训练等
     */
    @TableField("exercise_method")
    private String exerciseMethod;

    /**
     * 注意事项：运动过程中的安全提示或个体化建议
     */
    @TableField("precautions")
    private String precautions;

    /**
     * 单次运动时间（分钟）
     */
    @TableField("duration_minutes")
    private Integer durationMinutes;

    /**
     * 运动频率：如 1次/周, 3次/周, 7次/周 等
     */
    @TableField("exercise_frequency")
    private String exerciseFrequency;

    /**
     * 运动强度：高强度、中等、低强度
     */
    @TableField("exercise_intensity")
    private String exerciseIntensity;

    /**
     * 运动周期（周）
     */
    @TableField("cycle_weeks")
    private Integer cycleWeeks;

    /**
     * 计划总次数（次）
     */
    @TableField("total_sessions")
    private Integer totalSessions;

    /**
     * 每周建议累计运动时长（分钟）
     */
    @TableField("weekly_total_minutes")
    private Integer weeklyTotalMinutes;

    /**
     * 运动进阶要求描述
     */
    @TableField("progression")
    private String progression;

    /**
     * 补充说明，如调整依据、特殊情况等
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 运动图示图片路径
     */
    @TableField("exercise_image")
    private String exerciseImage;
}
