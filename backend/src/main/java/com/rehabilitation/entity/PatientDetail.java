package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;

/**
 * 患者详细信息实体
 */
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
     * 详细地址（门牌号、小区名称等）
     */
    @TableField("detail_address")
    private String detailAddress;

    /**
     * 诊断信息
     */
    @TableField("diagnosis")
    private String diagnosis;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    // 手动添加所有字段的 Getter/Setter 方法（解决 Lombok 编译问题）

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public Integer getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Integer heightCm) {
        this.heightCm = heightCm;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getContactProvinceCityDistrict() {
        return contactProvinceCityDistrict;
    }

    public void setContactProvinceCityDistrict(String contactProvinceCityDistrict) {
        this.contactProvinceCityDistrict = contactProvinceCityDistrict;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
