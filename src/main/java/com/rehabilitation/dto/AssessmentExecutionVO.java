package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评定执行视图对象VO
 */
@Data
public class AssessmentExecutionVO {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 评定方案名称
     */
    private String assessmentName;

    /**
     * 客户姓名
     */
    private String patientName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

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
     * 状态
     */
    private String status;

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

    /**
     * 获取客户信息展示
     */
    public String getCustomerInfo() {
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