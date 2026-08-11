package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 入组评估实体类（运动前健康评估问卷 PAR-Q）
 */
@Data
@TableName("enrollment_assessment")
public class EnrollmentAssessment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer patientId;

    // ========== 一般健康问题 (7个) ==========
    private String generalQ1;
    private String generalQ2;
    private String generalQ3;
    private String generalQ4;
    private String generalQ5;
    private String generalQ6;
    private String generalQ7;

    // ========== 医疗相关问题 (问题1及子问题) ==========
    private String medicalQ1;
    private String medicalQ1a;
    private String medicalQ1b;
    private String medicalQ1c;

    // ========== 医疗相关问题 (问题2及子问题) ==========
    private String medicalQ2;
    private String medicalQ2a;
    private String medicalQ2b;

    // ========== 医疗相关问题 (问题3及子问题) ==========
    private String medicalQ3;
    private String medicalQ3a;
    private String medicalQ3b;
    private String medicalQ3c;
    private String medicalQ3d;

    // ========== 医疗相关问题 (问题4及子问题) ==========
    private String medicalQ4;
    private String medicalQ4a;
    private String medicalQ4b;

    // ========== 医疗相关问题 (问题5及子问题) ==========
    private String medicalQ5;
    private String medicalQ5a;
    private String medicalQ5b;
    private String medicalQ5c;
    private String medicalQ5d;
    private String medicalQ5e;

    // ========== 医疗相关问题 (问题6及子问题) ==========
    private String medicalQ6;
    private String medicalQ6a;
    private String medicalQ6b;

    // ========== 医疗相关问题 (问题7及子问题) ==========
    private String medicalQ7;
    private String medicalQ7a;
    private String medicalQ7b;
    private String medicalQ7c;
    private String medicalQ7d;

    // ========== 医疗相关问题 (问题8及子问题) ==========
    private String medicalQ8;
    private String medicalQ8a;
    private String medicalQ8b;
    private String medicalQ8c;

    // ========== 医疗相关问题 (问题9及子问题) ==========
    private String medicalQ9;
    private String medicalQ9a;
    private String medicalQ9b;
    private String medicalQ9c;

    // ========== 医疗相关问题 (问题10及子问题) ==========
    private String medicalQ10;
    private String medicalQ10a;
    private String medicalQ10b;
    private String medicalQ10c;

    // ========== 运动建议 ==========
    private String recommendation1;
    private String recommendation2;
    private String recommendation3;

    // ========== 评估人员信息 ==========
    private String assessorName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assessmentDate;

    // ========== 系统字段 ==========
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
