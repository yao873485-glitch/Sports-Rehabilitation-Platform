package com.rehabilitation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程预约记录展示VO
 */
@Data
public class ReservationVO {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 预约日期
     */
    private LocalDateTime reservationDate;

    /**
     * 预约人员
     */
    private String reservedBy;

    /**
     * 教室名称
     */
    private String classroomName;

    /**
     * 运动方式
     */
    private String exerciseType;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 档案号
     */
    private String medicalRecordNumber;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 预约状态
     */
    private String reservationStatus;

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
