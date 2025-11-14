package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * 医疗方案实体类（精简版）
 */
@Data
@TableName("medical_scheme")
public class MedicalScheme {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 患者ID
     */
    @TableField("patient_id")
    private Integer patientId;

    /**
     * 方案编号
     */
    @TableField("scheme_id")
    private String schemeId;

    /**
     * 开方医生
     */
    @TableField("doctor_name")
    private String doctorName;

    /**
     * 状态：1-执行中，2-已结束
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 开方日期，用于查询
     */
    @TableField("create_date")
    private LocalDate createDate;
}