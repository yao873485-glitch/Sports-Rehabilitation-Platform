package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 随访记录实体类
 */
@Data
@TableName("followup_record")
public class FollowupRecord {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 入组记录ID：外键关联followup_patient_enrollment表
     */
    @TableField("enrollment_id")
    private Long enrollmentId;

    /**
     * 患者ID：外键关联patient表
     */
    @TableField("patient_id")
    private Integer patientId;

    /**
     * 随访日期：本次随访的时间
     */
    @TableField("followup_date")
    private LocalDateTime followupDate;

    /**
     * 随访类型：电话随访/门诊随访/家庭随访/线上随访
     */
    @TableField("followup_type")
    private String followupType;

    /**
     * 随访医生：执行本次随访的医生
     */
    @TableField("followup_doctor")
    private String followupDoctor;

    /**
     * 随访内容：本次随访的详细记录
     */
    @TableField("followup_content")
    private String followupContent;

    /**
     * 随访结果：良好/一般/需要干预/异常
     */
    @TableField("followup_result")
    private String followupResult;

    /**
     * 血压值：如120/80
     */
    @TableField("blood_pressure")
    private String bloodPressure;

    /**
     * 血糖值：单位mmol/L
     */
    @TableField("blood_sugar")
    private BigDecimal bloodSugar;

    /**
     * 心率：单位次/分
     */
    @TableField("heart_rate")
    private Integer heartRate;

    /**
     * 体温：单位℃
     */
    @TableField("body_temperature")
    private BigDecimal bodyTemperature;

    /**
     * 体重：单位kg
     */
    @TableField("weight")
    private BigDecimal weight;

    /**
     * 下次随访日期
     */
    @TableField("next_followup_date")
    private LocalDateTime nextFollowupDate;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 附件：多个附件URL用逗号分隔
     */
    @TableField("attachments")
    private String attachments;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private String updatedBy;
}
