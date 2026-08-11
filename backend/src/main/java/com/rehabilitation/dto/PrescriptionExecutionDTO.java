package com.rehabilitation.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 处方执行DTO
 */
public class PrescriptionExecutionDTO {

    /**
     * 处方ID
     */
    private Integer prescriptionId;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 运动项目ID（单个执行）
     */
    private Integer exerciseId;

    /**
     * 运动项目ID列表（批量执行）
     */
    private List<Integer> exerciseIds;

    /**
     * 执行次数
     */
    private Integer executionCount;

    /**
     * 执行人员
     */
    private String executor;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 执行记录
     */
    private String executionRecord;

    // 手动添加getter和setter方法（解决Lombok问题）
    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public List<Integer> getExerciseIds() {
        return exerciseIds;
    }

    public void setExerciseIds(List<Integer> exerciseIds) {
        this.exerciseIds = exerciseIds;
    }

    public Integer getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(Integer executionCount) {
        this.executionCount = executionCount;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getExecutionRecord() {
        return executionRecord;
    }

    public void setExecutionRecord(String executionRecord) {
        this.executionRecord = executionRecord;
    }
}
