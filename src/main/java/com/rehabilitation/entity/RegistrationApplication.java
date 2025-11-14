package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 注册申请实体
 */
@Data
@TableName("registration_application")
public class RegistrationApplication {

    /**
     * 申请ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账户用户名
     */
    @TableField("username")
    private String username;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 性别
     */
    @TableField("gender")
    private String gender;

    /**
     * 出生日期
     */
    @TableField("birth_date")
    private LocalDate birthDate;

    /**
     * 所在平台
     */
    @TableField("platform")
    private String platform;

    /**
     * 所在机构
     */
    @TableField("organization")
    private String organization;

    /**
     * 申请角色
     */
    @TableField("apply_role")
    private String applyRole;

    /**
     * 证件URL
     */
    @TableField("certificate_url")
    private String certificateUrl;

    /**
     * 审核状态：0-待审核，1-已通过，2-已拒绝
     */
    @TableField("status")
    private Integer status;

    /**
     * 密码（加密存储）
     */
    @TableField("password")
    private String password;

    /**
     * 拒绝原因
     */
    @TableField("reject_reason")
    private String rejectReason;

    /**
     * 审核时间
     */
    @TableField("审核时间")
    private LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    @TableField("审核人")
    private Long auditor;

    /**
     * 申请时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
