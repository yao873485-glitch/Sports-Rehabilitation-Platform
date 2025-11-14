package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评定执行记录实体
 */
@Data
@TableName("assessment_execution_record")
public class AssessmentExecutionRecord {

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
     * 对应评定方案ID
     */
    @TableField("assessment_plan_id")
    private Integer assessmentPlanId;

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
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
