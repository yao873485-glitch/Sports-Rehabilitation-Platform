package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 医疗方案视图对象VO
 */
@Data
public class MedicalSchemeVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 性别：1-男，2-女
     */
    private Integer gender;

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
    private String archiveNo;

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
     * 开方医生
     */
    private String doctorName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}