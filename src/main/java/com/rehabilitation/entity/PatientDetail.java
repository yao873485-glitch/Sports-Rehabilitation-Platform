package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 患者详细信息实体
 */
@Data
@TableName("patient_detail")
public class PatientDetail {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 外键，关联patient表的id
     */
    @TableField("patient_id")
    private Integer patientId;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 证件类型
     */
    @TableField("id_card_type")
    private String idCardType;

    /**
     * 身高（cm）
     */
    @TableField("height_cm")
    private Integer heightCm;

    /**
     * 民族
     */
    @TableField("ethnicity")
    private String ethnicity;

    /**
     * 职业
     */
    @TableField("occupation")
    private String occupation;

    /**
     * 联系地址（省市区）
     */
    @TableField("contact_province_city_district")
    private String contactProvinceCityDistrict;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
