package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

/**
 * 教室设施信息表
 * 记录各类运动教室的基本信息、容量、状态及时间戳
 */
@TableName("classroom_facility_info")
public class ClassroomFacilityInfo {

    /**
     * 主键ID，唯一标识每个教室记录
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 运动方式：该教室对应的运动类型，如游泳、瑜伽、普拉提、功率自行车等
     */
    private String exerciseType;

    /**
     * 教室名称：具体的教室名称，如"阳光瑜伽室"、"动感单车1号厅"
     */
    private String classroomName;

    /**
     * 课程时长：在该教室内进行的标准课程时长，单位为分钟，例如45、60等
     */
    private Integer classDuration;

    /**
     * 课时配置：课程的时间安排配置，如"上午班"、"下午班"、"全天班"等
     */
    private String classScheduleConfig;

    /**
     * 预约上限：该教室可预约的最大人数
     */
    private Integer reservationLimit;

    /**
     * 教室编码：系统自动生成的唯一教室编码，首次保存成功后自动创建
     */
    private String classroomCode;

    /**
     * 状态：教室当前的运营状态，控制是否可被预约或展示
     */
    private String status;

    /**
     * 上次操作时间：最近一次对该教室进行操作（如编辑、上下架）的时间
     */
    @TableField(value = "last_operation_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastOperationTime;

    /**
     * 创建时间：该教室记录在系统中创建的时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    // Getter and Setter methods
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public Integer getClassDuration() {
        return classDuration;
    }

    public void setClassDuration(Integer classDuration) {
        this.classDuration = classDuration;
    }

    public Integer getReservationLimit() {
        return reservationLimit;
    }

    public void setReservationLimit(Integer reservationLimit) {
        this.reservationLimit = reservationLimit;
    }

    public String getClassScheduleConfig() {
        return classScheduleConfig;
    }

    public void setClassScheduleConfig(String classScheduleConfig) {
        this.classScheduleConfig = classScheduleConfig;
    }

    public String getClassroomCode() {
        return classroomCode;
    }

    public void setClassroomCode(String classroomCode) {
        this.classroomCode = classroomCode;
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