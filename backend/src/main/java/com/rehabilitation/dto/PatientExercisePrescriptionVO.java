package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者运动处方视图对象VO
 */
@Data
public class PatientExercisePrescriptionVO {

    /**
     * 主键ID
     */
    private Long id;

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
     * 最后更新时间
     */
    private LocalDateTime updatedTime;
}