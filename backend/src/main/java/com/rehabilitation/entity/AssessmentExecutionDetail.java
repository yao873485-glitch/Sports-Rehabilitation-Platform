package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 评估执行详细记录实体（IPAQ问卷）
 */
@TableName("assessment_execution_detail")
public class AssessmentExecutionDetail {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("assessment_plan_id")
    private Integer assessmentPlanId;

    @TableField("patient_id")
    private Integer patientId;

    @TableField("record_date")
    private LocalDate recordDate;

    @TableField("question1_answer")
    private String question1Answer;

    @TableField("question1_skip_reason")
    private String question1SkipReason;

    @TableField("question2_answer")
    private String question2Answer;

    @TableField("question2_unknown")
    private Integer question2Unknown;

    @TableField("question3_answer")
    private String question3Answer;

    @TableField("question3_skip_reason")
    private String question3SkipReason;

    @TableField("question4_answer")
    private String question4Answer;

    @TableField("question4_unknown")
    private Integer question4Unknown;

    @TableField("question5_answer")
    private String question5Answer;

    @TableField("question5_skip_reason")
    private String question5SkipReason;

    @TableField("question6_answer")
    private String question6Answer;

    @TableField("question6_unknown")
    private Integer question6Unknown;

    @TableField("question7_answer")
    private String question7Answer;

    @TableField("question7_unknown")
    private Integer question7Unknown;

    @TableField("executor")
    private String executor;

    @TableField("execution_time")
    private LocalDateTime executionTime;

    @TableField("status")
    private Integer status;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAssessmentPlanId() {
        return assessmentPlanId;
    }

    public void setAssessmentPlanId(Integer assessmentPlanId) {
        this.assessmentPlanId = assessmentPlanId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getQuestion1Answer() {
        return question1Answer;
    }

    public void setQuestion1Answer(String question1Answer) {
        this.question1Answer = question1Answer;
    }

    public String getQuestion1SkipReason() {
        return question1SkipReason;
    }

    public void setQuestion1SkipReason(String question1SkipReason) {
        this.question1SkipReason = question1SkipReason;
    }

    public String getQuestion2Answer() {
        return question2Answer;
    }

    public void setQuestion2Answer(String question2Answer) {
        this.question2Answer = question2Answer;
    }

    public Integer getQuestion2Unknown() {
        return question2Unknown;
    }

    public void setQuestion2Unknown(Integer question2Unknown) {
        this.question2Unknown = question2Unknown;
    }

    public String getQuestion3Answer() {
        return question3Answer;
    }

    public void setQuestion3Answer(String question3Answer) {
        this.question3Answer = question3Answer;
    }

    public String getQuestion3SkipReason() {
        return question3SkipReason;
    }

    public void setQuestion3SkipReason(String question3SkipReason) {
        this.question3SkipReason = question3SkipReason;
    }

    public String getQuestion4Answer() {
        return question4Answer;
    }

    public void setQuestion4Answer(String question4Answer) {
        this.question4Answer = question4Answer;
    }

    public Integer getQuestion4Unknown() {
        return question4Unknown;
    }

    public void setQuestion4Unknown(Integer question4Unknown) {
        this.question4Unknown = question4Unknown;
    }

    public String getQuestion5Answer() {
        return question5Answer;
    }

    public void setQuestion5Answer(String question5Answer) {
        this.question5Answer = question5Answer;
    }

    public String getQuestion5SkipReason() {
        return question5SkipReason;
    }

    public void setQuestion5SkipReason(String question5SkipReason) {
        this.question5SkipReason = question5SkipReason;
    }

    public String getQuestion6Answer() {
        return question6Answer;
    }

    public void setQuestion6Answer(String question6Answer) {
        this.question6Answer = question6Answer;
    }

    public Integer getQuestion6Unknown() {
        return question6Unknown;
    }

    public void setQuestion6Unknown(Integer question6Unknown) {
        this.question6Unknown = question6Unknown;
    }

    public String getQuestion7Answer() {
        return question7Answer;
    }

    public void setQuestion7Answer(String question7Answer) {
        this.question7Answer = question7Answer;
    }

    public Integer getQuestion7Unknown() {
        return question7Unknown;
    }

    public void setQuestion7Unknown(Integer question7Unknown) {
        this.question7Unknown = question7Unknown;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public LocalDateTime getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(LocalDateTime executionTime) {
        this.executionTime = executionTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
