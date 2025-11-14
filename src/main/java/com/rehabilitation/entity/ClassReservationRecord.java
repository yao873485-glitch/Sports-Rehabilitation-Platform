package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程预约记录实体
 */
@Data
@TableName("class_reservation_record")
public class ClassReservationRecord {
    
    /**
     * 主键ID，唯一标识每条预约记录
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 预约日期：客户预约的具体上课时间，如2025-08-21 13:00
     */
    @TableField("reservation_date")
    private LocalDateTime reservationDate;

    /**
     * 预约人员：发起并创建该预约的工作人员或系统用户姓名
     */
    @TableField("reserved_by")
    private String reservedBy;

    /**
     * 教室名称：被预约的教室名称，如游泳教室01、普拉提教室01等
     */
    @TableField("classroom_name")
    private String classroomName;

    /**
     * 运动方式：该课程对应的运动类型，如游泳、瑜伽、普拉提等
     */
    @TableField("exercise_type")
    private String exerciseType;

    /**
     * 患者姓名：实际参与课程的客户姓名
     */
    @TableField("patient_name")
    private String patientName;

    /**
     * 性别：患者性别
     */
    @TableField("gender")
    private String gender;

    /**
     * 年龄：患者当前年龄（0-255）
     */
    @TableField("age")
    private Integer age;

    /**
     * 档案号：患者的唯一档案编号，用于身份识别和数据关联
     */
    @TableField("medical_record_number")
    private String medicalRecordNumber;

    /**
     * 创建时间：预约记录在系统中生成的时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 预约状态：当前预约的执行状态，用于流程控制与界面展示
     */
    @TableField("reservation_status")
    private String reservationStatus;

    /**
     * 获取性别描述
     */
    public String getGenderDesc() {
        if ("男".equals(gender)) {
            return "男";
        } else if ("女".equals(gender)) {
            return "女";
        }
        return "";
    }

    /**
     * 获取患者信息展示
     */
    public String getPatientInfo() {
        StringBuilder sb = new StringBuilder();
        if (patientName != null) {
            sb.append(patientName);
        }
        if (gender != null) {
            sb.append(" ").append("男".equals(gender) ? "♂" : "♀");
        }
        if (age != null) {
            sb.append(" ").append(age).append("岁");
        }
        return sb.toString();
    }
}
