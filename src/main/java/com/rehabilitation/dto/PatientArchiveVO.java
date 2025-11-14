package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者档案展示VO
 */
@Data
public class PatientArchiveVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 患者姓名
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
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 证件号码
     */
    private String idNumber;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 档案号
     */
    private String medicalRecordNo;

    /**
     * 病种
     */
    private String disease;

    /**
     * 入组机构
     */
    private String institution;

    /**
     * 居住地址
     */
    private String address;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
