package com.rehabilitation.dto;

/**
 * 处方运动项目视图对象
 */
public class PrescriptionExerciseVO {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 运动场景：机构/居家
     */
    private String exerciseScene;

    /**
     * 运动类型：如 有氧运动、呼吸训练、平衡训练、抗阻训练、柔韧性训练
     */
    private String exerciseType;

    /**
     * 运动方式：如 单腿站立（扶椅背）、直线行走、弹力带训练等
     */
    private String exerciseMethod;

    /**
     * 注意事项：运动过程中的安全提示或个体化建议
     */
    private String precautions;

    /**
     * 单次运动时间（分钟）
     */
    private Integer durationMinutes;

    /**
     * 运动频率：如 1次/周, 3次/周, 7次/周 等
     */
    private String exerciseFrequency;

    /**
     * 运动强度：高强度、中等、低强度
     */
    private String exerciseIntensity;

    /**
     * 运动周期（周）
     */
    private Integer cycleWeeks;

    /**
     * 计划总次数（次）
     */
    private Integer totalSessions;

    /**
     * 每周建议累计运动时长（分钟）
     */
    private Integer weeklyTotalMinutes;

    /**
     * 运动进阶要求描述
     */
    private String progression;

    /**
     * 补充说明，如调整依据、特殊情况等
     */
    private String remarks;

    /**
     * 运动图示图片路径
     */
    private String exerciseImage;

    // 手动添加 getter/setter 避免 Lombok 问题
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
