package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 患者评定方案实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("patient_assessment_plan")
public class PatientAssessmentPlan {

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
     * 所属医疗方案ID（可选）
     */
    @TableField("medical_scheme_id")
    private Long medicalSchemeId;

    /**
     * 评定方案名称
     */
    @TableField("assessment_name")
    private String assessmentName;

    /**
     * 执行机构
     */
    @TableField("execution_institution")
    private String executionInstitution;

    /**
     * 状态：1-未开始, 2-执行中, 3-已完成, 4-已结束
     */
    @TableField("status")
    private Integer status;

    /**
     * 开方医生
     */
    @TableField("prescribing_doctor")
    private String prescribingDoctor;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 最后更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;

    /**
     * 执行场景：机构、居家
     */
    @TableField("execution_type")
    private String executionType;

    /**
     * 量表名称
     */
    @TableField("scale_name")
    private String scaleName;

    /**
     * 执行频率，如：3次/周
     */
    @TableField("execution_frequency")
    private String executionFrequency;

    /**
     * 执行周期长度（周），0表示无周期
     */
    @TableField("cycle_duration")
    private Integer cycleDuration;

    /**
     * 总次数（次）
     */
    @TableField("total_times")
    private Integer totalTimes;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
}