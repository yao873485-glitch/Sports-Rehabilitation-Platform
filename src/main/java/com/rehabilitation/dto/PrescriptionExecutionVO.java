package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 处方执行视图对象VO
 */
@Data
public class PrescriptionExecutionVO {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 运动处方名称
     */
    private String prescriptionName;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者性别
     */
    private String gender;

    /**
     * 患者年龄
     */
    private Integer age;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 档案号
     */
    private String medicalRecordNumber;

    /**
     * 病种
     */
    private String diseaseType;

    /**
     * 入组机构
     */
    private String enrollmentInstitution;

    /**
     * 执行机构
     */
    private String executionInstitution;

    /**
     * 处方状态
     */
    private String prescriptionStatus;

    /**
     * 开方医生
     */
    private String prescribingDoctor;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 获取患者信息展示
     */
    public String getPatientInfo() {
        StringBuilder sb = new StringBuilder();
        if (patientName != null) {
            sb.append(patientName);
        }
        if (gender != null) {
            sb.append(" ").append("男".equals(gender) ? "♂" : "♀");
        }
        if (age != null) {
            sb.append(" ").append(age).append("岁");
        }
        return sb.toString();
    }
}