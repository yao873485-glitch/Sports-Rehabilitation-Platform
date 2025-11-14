package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 随访患者入组信息VO（包含关联的患者、计划、项目信息）
 */
@Data
public class FollowupEnrollmentVO {

    /**
     * 入组记录ID
     */
    private Long id;

    /**
     * 序号
     */
    private String serialNumber;

    // ========== 患者信息（来自patient表） ==========
    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄（计算得出）
     */
    private Integer age;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 病种
     */
    private String diseaseType;

    // ========== 随访计划信息（来自followup_plan_repository表） ==========
    /**
     * 随访计划ID
     */
    private Long followupPlanId;

    /**
     * 随访计划名称
     */
    private String followupPlanName;

    // ========== 随访项目信息（来自followup_program_master表） ==========
    /**
     * 随访项目ID
     */
    private Long followupProjectId;

    /**
     * 随访项目名称
     */
    private String followupProject;

    // ========== 随访团队信息（来自enrollment表） ==========
    /**
     * 随访团队
     */
    private String followupTeam;

    /**
     * 主负责医生
     */
    private String primaryDoctor;

    /**
     * 健康管理师
     */
    private String healthManager;

    // ========== 时间信息 ==========
    /**
     * 申请时间
     */
    private LocalDateTime applicationTime;

    /**
     * 加入项目时间
     */
    private LocalDateTime enrollmentTime;

    // ========== 状态信息 ==========
    /**
     * 随访状态
     */
    private String followupStatus;

    /**
     * 入组备注
     */
    private String enrollmentRemark;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;
}
