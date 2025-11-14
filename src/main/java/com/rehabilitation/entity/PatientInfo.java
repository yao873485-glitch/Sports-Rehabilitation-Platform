package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者基本信息实体
 */
@Data
@TableName("patient")
public class PatientInfo {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

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
     * 证件号码
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 档案号
     */
    @TableField("medical_record_no")
    private String medicalRecordNo;

    /**
     * 病种
     */
    @TableField("disease_type")
    private String diseaseType;

    /**
     * 入组机构
     */
    @TableField("enrollment_institution")
    private String enrollmentInstitution;

    /**
     * 居住地址
     */
    @TableField("address")
    private String address;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 最后修改时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;
}
