package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者档案实体
 */
@Data
@TableName("patient_info")
public class PatientArchive {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 患者姓名
     */
    @TableField("姓名")
    private String patientName;

    /**
     * 性别
     */
    @TableField("性别")
    private String gender;

    /**
     * 年龄
     */
    @TableField("年龄")
    private Integer age;

    /**
     * 出生日期，用于计算年龄
     */
    @TableField("出生日期")
    private LocalDate birthDate;

    /**
     * 证件号码
     */
    @TableField("证件号码")
    private String idNumber;

    /**
     * 联系电话
     */
    @TableField("联系电话")
    private String phone;

    /**
     * 档案号，唯一识别码
     */
    @TableField("档案号")
    private String medicalRecordNo;

    /**
     * 病种
     */
    @TableField("病种")
    private String disease;

    /**
     * 入组机构
     */
    @TableField("入组机构")
    private String institution;

    /**
     * 居住地址
     */
    @TableField("居住地址")
    private String address;

    /**
     * 创建时间
     */
    @TableField(value = "创建时间", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "最后修改时间", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
