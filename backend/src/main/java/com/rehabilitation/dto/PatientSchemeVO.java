package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 患者方案展示VO
 */
@Data
public class PatientSchemeVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 患者信息（姓名 + 性别图标 + 年龄）
     */
    private String patientInfo;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 性别
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
     * 病种
     */
    private String diseaseType;

    /**
     * 状态
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

    /**
     * 构建患者信息展示
     */
    public String getPatientInfo() {
        StringBuilder sb = new StringBuilder();
        if (patientName != null) {
            sb.append(patientName);
        }
        if (genderDesc != null) {
            sb.append(" ").append("M".equals(gender) ? "♂" : "♀");
        }
        if (age != null) {
            sb.append(" ").append(age).append("岁");
        }
        return sb.toString();
    }
}
