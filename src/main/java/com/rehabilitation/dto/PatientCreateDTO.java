package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 患者创建DTO - 用于接收前端提交的完整患者信息
 */
@Data
public class PatientCreateDTO {

    // ========== 基本信息 ==========
    /**
     * 姓名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 证件类型
     */
    private String idCardType;

    /**
     * 证件号码
     */
    private String idCard;

    /**
     * 性别
     */
    private String gender;

    /**
     * 身高（cm）
     */
    private Integer height;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 民族
     */
    private String ethnicity;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 联系地址（省市区）
     */
    private String contactAddress;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 备注说明
     */
    private String remarks;

    // ========== 入组信息 ==========
    /**
     * 档案号（系统自动生成，前端传null即可）
     */
    private String medicalRecordNo;

    /**
     * 诊断
     */
    private String diagnosis;

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 入组机构
     */
    private String enrollmentInstitution;

    /**
     * 居住地址
     */
    private String address;
}
