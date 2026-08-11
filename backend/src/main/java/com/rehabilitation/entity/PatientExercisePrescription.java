package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 患者运动处方实例实体
 */
@TableName("patient_exercise_prescription")
public class PatientExercisePrescription {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 所属医疗方案ID（可选）
     */
    private Long medicalSchemeId;

    /**
     * 运动处方名称：显示该运动处方的具体名称
     */
    private String prescriptionName;

    /**
     * 执行机构：显示负责执行该运动处方的医疗机构或康复中心
     */
    private String executionInstitution;

    /**
     * 处方状态：1-未开始, 2-执行中, 3-已完成, 4-已结束
     */
    private Integer prescriptionStatus;

    /**
     * 开方医生：开具该运动处方的医生姓名
     */
    private String prescribingDoctor;

    /**
     * 创建时间：记录创建的时间
     */
    private LocalDateTime createdTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updatedTime;

    // Getter and Setter methods
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

    public Long getMedicalSchemeId() {
        return medicalSchemeId;
    }

    public void setMedicalSchemeId(Long medicalSchemeId) {
        this.medicalSchemeId = medicalSchemeId;
    }

    public String getPrescriptionName() {
        return prescriptionName;
    }

    public void setPrescriptionName(String prescriptionName) {
        this.prescriptionName = prescriptionName;
    }

    public String getExecutionInstitution() {
        return executionInstitution;
    }

    public void setExecutionInstitution(String executionInstitution) {
        this.executionInstitution = executionInstitution;
    }

    public Integer getPrescriptionStatus() {
        return prescriptionStatus;
    }

    public void setPrescriptionStatus(Integer prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

    public String getPrescribingDoctor() {
        return prescribingDoctor;
    }

    public void setPrescribingDoctor(String prescribingDoctor) {
        this.prescribingDoctor = prescribingDoctor;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}