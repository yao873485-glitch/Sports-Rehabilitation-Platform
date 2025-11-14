package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 随访患者入组记录实体
 */
@Data
@TableName("followup_patient_enrollment")
public class FollowupPatientEnrollment {

    /**
     * 主键ID，系统内部唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 序号：用于展示和排序的外显编号
     */
    @TableField("serial_number")
    private String serialNumber;

    /**
     * 患者ID：外键关联patient表
     */
    @TableField("patient_id")
    private Integer patientId;

    /**
     * 随访计划ID：外键关联followup_plan_repository表
     */
    @TableField("followup_plan_id")
    private Long followupPlanId;

    /**
     * 随访项目ID：外键关联followup_program_master表
     */
    @TableField("followup_project_id")
    private Long followupProjectId;

    /**
     * 随访团队：负责该患者的医疗团队名称
     */
    @TableField("followup_team")
    private String followupTeam;

    /**
     * 主负责医生：直接管理该患者的主要医生姓名
     */
    @TableField("primary_doctor")
    private String primaryDoctor;

    /**
     * 健康管理师：协助管理的健康管理师姓名
     */
    @TableField("health_manager")
    private String healthManager;

    /**
     * 申请时间：患者提交随访申请的时间
     */
    @TableField("application_time")
    private LocalDateTime applicationTime;

    /**
     * 加入项目时间：患者正式被纳入随访项目的时间
     */
    @TableField("enrollment_time")
    private LocalDateTime enrollmentTime;

    /**
     * 随访状态：当前患者在随访流程中的状态
     */
    @TableField("followup_status")
    private String followupStatus;

    /**
     * 入组备注：记录入组时的特殊说明
     */
    @TableField("enrollment_remark")
    private String enrollmentRemark;

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
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private String updatedBy;
}
