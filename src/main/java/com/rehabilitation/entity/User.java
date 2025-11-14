package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("user")
public class User {

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 手机号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 密码（加密存储）
     */
    @TableField("password")
    private String password;

    /**
     * 性别：男、女
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
     * 角色
     */
    @TableField("role")
    private String role;

    /**
     * 证件URL
     */
    @TableField("certificate_url")
    private String certificateUrl;

    /**
     * 状态：0-待审核，1-已通过，2-已拒绝
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
