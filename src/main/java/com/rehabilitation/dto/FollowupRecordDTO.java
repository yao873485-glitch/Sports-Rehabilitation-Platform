package com.rehabilitation.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 随访记录DTO
 */
@Data
public class FollowupRecordDTO {

    /**
     * 入组记录ID
     */
    private Long enrollmentId;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 随访日期
     */
    private LocalDateTime followupDate;

    /**
     * 随访类型
     */
    private String followupType;

    /**
     * 随访医生
     */
    private String followupDoctor;

    /**
     * 随访内容
     */
    private String followupContent;

    /**
     * 随访结果
     */
    private String followupResult;

    /**
     * 血压值
     */
    private String bloodPressure;

    /**
     * 血糖值
     */
    private BigDecimal bloodSugar;

    /**
     * 心率
     */
    private Integer heartRate;

    /**
     * 体温
     */
    private BigDecimal bodyTemperature;

    /**
     * 体重
     */
    private BigDecimal weight;

    /**
     * 下次随访日期
     */
    private LocalDateTime nextFollowupDate;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 附件
     */
    private String attachments;

    /**
     * 创建人
     */
    private String createdBy;
}
