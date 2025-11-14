package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 患者方案信息实体（精简版）
 */
@Data
@TableName("patient_scheme_info")
public class PatientSchemeInfo {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 患者ID
     */
    @TableField("patient_id")
    private Integer patientId;

    /**
     * 方案编号，唯一标识
     */
    @TableField("scheme_id")
    private String schemeId;

    /**
     * 方案状态：1-执行中，2-已结束
     */
    @TableField("status")
    private Integer status;

    /**
     * 方案创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 预计或实际结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;
}
