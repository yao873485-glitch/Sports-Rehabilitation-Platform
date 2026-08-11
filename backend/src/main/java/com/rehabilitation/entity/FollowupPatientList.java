package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 随访患者列表实体
 */
@Data
@TableName("followup_patient_list")
public class FollowupPatientList {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("patient_id")
    private Integer patientId;

    @TableField("source")
    private String source;

    @TableField("application_time")
    private LocalDateTime applicationTime;

    @TableField("enrollment_time")
    private LocalDateTime enrollmentTime;

    @TableField("primary_doctor")
    private String primaryDoctor;

    @TableField("health_manager")
    private String healthManager;

    @TableField("followup_team")
    private String followupTeam;

    @TableField("followup_plan_name")
    private String followupPlanName;

    @TableField("followup_plan_id")
    private Long followupPlanId;

    @TableField("followup_project")
    private String followupProject;

    @TableField("followup_status")
    private String followupStatus;

    @TableField("remark")
    private String remark;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableField("created_by")
    private String createdBy;

    @TableField("updated_by")
    private String updatedBy;
}
