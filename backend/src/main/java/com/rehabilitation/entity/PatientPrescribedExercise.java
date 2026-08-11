package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

/**
 * 患者运动处方执行记录实体
 */
@TableName("patient_prescribed_exercise")
public class PatientPrescribedExercise {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 患者ID
     */
    @TableField("patient_id")
    private Integer patientId;

    /**
     * 对应运动处方ID
     */
    @TableField("exercise_prescription_id")
    private Integer exercisePrescriptionId;

    /**
     * 执行机构
     */
    @TableField("execution_institution")
    private String executionInstitution;

    /**
     * 状态：1-未开始, 2-执行中, 3-已完成, 4-已结束
     */
    @TableField("prescription_status")
    private Integer prescriptionStatus;

    /**
     * 开方医生
     */
    @TableField("prescribing_doctor")
    private String prescribingDoctor;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 运动场景：机构/居家
     */
    @TableField("exercise_scene")
    private String exerciseScene;

    /**
     * 运动类型：如 有氧运动、呼吸训练、平衡训练、抗阻训练、柔韧性训练
     */
    @TableField("exercise_type")
    private String exerciseType;

    /**
     * 运动方式：如 单腿站立（扶椅背）、直线行走、弹力带训练等
     */
    @TableField("exercise_method")
    private String exerciseMethod;

    /**
     * 注意事项：运动过程中的安全提示或个体化建议
     */
    @TableField("precautions")
    private String precautions;

    /**
     * 单次运动时间（分钟）
     */
    @TableField("duration_minutes")
    private Integer durationMinutes;

    /**
     * 运动频率：如 1次/周, 3次/周, 7次/周 等
     */
    @TableField("exercise_frequency")
    private String exerciseFrequency;

    /**
     * 运动强度：高强度、中等、低强度
     */
    @TableField("exercise_intensity")
    private String exerciseIntensity;

    /**
     * 运动周期（周）
     */
    @TableField("cycle_weeks")
    private Integer cycleWeeks;

    /**
     * 计划总次数（次）
     */
    @TableField("total_sessions")
    private Integer totalSessions;

    /**
     * 每周建议累计运动时长（分钟）
     */
    @TableField("weekly_total_minutes")
    private Integer weeklyTotalMinutes;

    /**
     * 运动进阶要求描述
     */
    @TableField("progression")
    private String progression;

    /**
     * 补充说明，如调整依据、特殊情况等
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 运动图示图片路径
     */
    @TableField("exercise_image")
    private String exerciseImage;

    // Getter and Setter methods
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

    public Integer getExercisePrescriptionId() {
        return exercisePrescriptionId;
    }

    public void setExercisePrescriptionId(Integer exercisePrescriptionId) {
        this.exercisePrescriptionId = exercisePrescriptionId;
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

    public String getExerciseScene() {
        return exerciseScene;
    }

    public void setExerciseScene(String exerciseScene) {
        this.exerciseScene = exerciseScene;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getExerciseMethod() {
        return exerciseMethod;
    }

    public void setExerciseMethod(String exerciseMethod) {
        this.exerciseMethod = exerciseMethod;
    }

    public String getPrecautions() {
        return precautions;
    }

    public void setPrecautions(String precautions) {
        this.precautions = precautions;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getExerciseFrequency() {
        return exerciseFrequency;
    }

    public void setExerciseFrequency(String exerciseFrequency) {
        this.exerciseFrequency = exerciseFrequency;
    }

    public String getExerciseIntensity() {
        return exerciseIntensity;
    }

    public void setExerciseIntensity(String exerciseIntensity) {
        this.exerciseIntensity = exerciseIntensity;
    }

    public Integer getCycleWeeks() {
        return cycleWeeks;
    }

    public void setCycleWeeks(Integer cycleWeeks) {
        this.cycleWeeks = cycleWeeks;
    }

    public Integer getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(Integer totalSessions) {
        this.totalSessions = totalSessions;
    }

    public Integer getWeeklyTotalMinutes() {
        return weeklyTotalMinutes;
    }

    public void setWeeklyTotalMinutes(Integer weeklyTotalMinutes) {
        this.weeklyTotalMinutes = weeklyTotalMinutes;
    }

    public String getProgression() {
        return progression;
    }

    public void setProgression(String progression) {
        this.progression = progression;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getExerciseImage() {
        return exerciseImage;
    }

    public void setExerciseImage(String exerciseImage) {
        this.exerciseImage = exerciseImage;
    }
}
