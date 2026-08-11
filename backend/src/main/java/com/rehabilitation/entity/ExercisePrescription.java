package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 运动处方信息实体
 */
@TableName("exercise_prescription")
public class ExercisePrescription {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 病种类型：显示该运动处方适用的病种类型
     */
    private String diseaseType;

    /**
     * 运动方式：显示具体的运动方式，如普拉提、瑜伽、慢跑等
     */
    private String exerciseMethod;

    /**
     * 运动类型：显示运动的类型分类，如有氧运动、抗阻训练等
     */
    private String exerciseType;

    /**
     * 动作图式：显示与该运动处方相关的动作图示或视频链接，可以存储图片URL或视频URL
     */
    private String actionDiagram;

    /**
     * 注意事项：显示执行该运动处方时需要注意的事项
     */
    private String precautions;

    /**
     * 运动进阶：显示运动的进阶情况或难度级别，例如初级、中级、高级
     */
    private String progressionLevel;

    /**
     * 运动场景：显示适合执行该运动处方的场景或环境，如室内、户外、健身房等
     */
    private String exerciseScene;

    /**
     * 状态：显示运动处方当前的状态（如上架、下架等）
     */
    private String status;

    /**
     * 上次操作时间：显示运动处方最近一次状态变更的时间
     */
    private LocalDateTime lastOperationTime;

    /**
     * 创建时间：显示运动处方记录创建的时间
     */
    private LocalDateTime createdTime;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getExerciseMethod() {
        return exerciseMethod;
    }

    public void setExerciseMethod(String exerciseMethod) {
        this.exerciseMethod = exerciseMethod;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getActionDiagram() {
        return actionDiagram;
    }

    public void setActionDiagram(String actionDiagram) {
        this.actionDiagram = actionDiagram;
    }

    public String getPrecautions() {
        return precautions;
    }

    public void setPrecautions(String precautions) {
        this.precautions = precautions;
    }

    public String getProgressionLevel() {
        return progressionLevel;
    }

    public void setProgressionLevel(String progressionLevel) {
        this.progressionLevel = progressionLevel;
    }

    public String getExerciseScene() {
        return exerciseScene;
    }

    public void setExerciseScene(String exerciseScene) {
        this.exerciseScene = exerciseScene;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastOperationTime() {
        return lastOperationTime;
    }

    public void setLastOperationTime(LocalDateTime lastOperationTime) {
        this.lastOperationTime = lastOperationTime;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}