package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 患者方案信息视图对象VO
 */
@Data
public class PatientSchemeInfoVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 性别：M-男，F-女
     */
    private String gender;

    /**
     * 性别描述
     */
    private String genderDesc;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 档案号
     */
    private String medicalRecordNo;

    /**
     * 方案编号
     */
    private String schemeId;

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 状态：1-执行中，2-已结束
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 入组机构
     */
    private String orgName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}