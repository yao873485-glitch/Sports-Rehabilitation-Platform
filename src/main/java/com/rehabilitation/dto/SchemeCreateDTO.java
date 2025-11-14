package com.rehabilitation.dto;

import lombok.Data;

/**
 * 方案创建DTO
 */
@Data
public class SchemeCreateDTO {

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 档案号
     */
    private String medicalRecordNo;

    /**
     * 方案周期（月）
     */
    private Integer schemeCycle;

    /**
     * 病种类型
     */
    private String diseaseType;
}
