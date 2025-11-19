package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者方案信息实体（精简版）
 */
@Data
@TableName("patient_scheme_info")
public class PatientSchemeInfo {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 患者ID
     */
    @TableField("patient_id")
    private Integer patientId;

    /**
     * 方案编号，唯一标识
     */
    @TableField("scheme_id")
    private String schemeId;

    /**
     * 方案状态：1-执行中，2-已结束
     */
    @TableField("status")
    private Integer status;

    /**
     * 方案创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 预计或实际结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    // ========== 关联查询字段（非数据库表字段） ==========

    /**
     * 患者姓名（关联patient表）
     */
    @TableField(exist = false)
    private String patientName;

    /**
     * 性别（关联patient表）
     */
    @TableField(exist = false)
    private String gender;

    /**
     * 出生日期（关联patient表）
     */
    @TableField(exist = false)
    private LocalDate birthDate;

    /**
     * 档案号（关联patient表）
     */
    @TableField(exist = false)
    private String medicalRecordNo;

    /**
     * 病种类型（关联patient表）
     */
    @TableField(exist = false)
    private String diseaseType;

    /**
     * 入组机构（关联patient表）
     */
    @TableField(exist = false)
    private String orgName;

    // ========== 手动添加getter/setter方法（解决Lombok编译问题） ==========

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getMedicalRecordNo() {
        return medicalRecordNo;
    }

    public void setMedicalRecordNo(String medicalRecordNo) {
        this.medicalRecordNo = medicalRecordNo;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
