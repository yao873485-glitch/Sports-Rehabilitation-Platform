package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 专病档案实体类（运动康复专病档案）
 *
 * 注意：由于 Lombok 编译问题，为关键字段添加了手动的 getter/setter 方法
 */
@Data
@TableName(value = "disease_record", autoResultMap = true)
public class DiseaseRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer patientId;

    // 手动添加 patientId 的 getter/setter 方法（解决 Lombok 编译问题）
    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    // ========== 运动前筛查结果 ==========
    private BigDecimal lpaMin;
    private BigDecimal mpaMin;
    private BigDecimal vpaMin;

    // ========== 体力活动水平 ==========
    private BigDecimal heightCm;
    private BigDecimal weightKg;
    private BigDecimal bmi;
    private BigDecimal waistCm;
    private BigDecimal hipCm;
    private BigDecimal waistHipRatio;

    // ========== 疾病史 ==========
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> diseaseHistoryList;
    private String diseaseHistoryDetail;

    // ========== 家族史 ==========
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> familyHistoryList;
    private String familyHistoryDetail;

    // ========== 血液指标 ==========
    private BigDecimal fastingGlucose;
    private BigDecimal totalCholesterol;
    private BigDecimal hdlCholesterol;
    private BigDecimal ldlCholesterol;
    private String bloodPressure;
    private Integer heartRate;

    // ========== 健康筛查 - 生活方式 ==========
    private String sedentary;
    private String smoking;
    private String drinking;
    private String insufficientSunlight;
    private String excessiveCaffeine;

    // ========== 健康筛查 - 饮食情况 ==========
    private String proteinIntake;
    private String highCalciumDiet;
    private String calciumVdDeficiency;

    // ========== 健康筛查 - 药物使用情况 ==========
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> medicationList;

    // ========== 运动风险分级 ==========
    private String exerciseRiskLevel;

    // ========== 医学检查 - 骨折史 ==========
    private String fractureHistory;
    private String fractureDetail;

    // ========== 医学检查 - 血常规 ==========
    private String bloodRoutineStatus;
    private String bloodRoutineDetail;

    // ========== 医学检查 - 尿常规 ==========
    private String urineRoutineStatus;
    private String urineRoutineDetail;

    // ========== 医学检查 - 肝功能 ==========
    private String liverFunctionStatus;
    private String liverFunctionDetail;

    // ========== 医学检查 - 肾功能 ==========
    private String kidneyFunctionStatus;
    private String kidneyFunctionDetail;

    // ========== 医学检查 - 血脂 ==========
    private String bloodLipidStatus;
    private String bloodLipidDetail;

    // ========== 医学检查 - 糖化血红蛋白 ==========
    private String hba1cStatus;
    private String hba1cDetail;

    // ========== 医学检查 - 空腹血糖 ==========
    private String fastingGlucoseStatus;
    private String fastingGlucoseDetail;

    // ========== 医学检查 - 胰岛功能 ==========
    private String pancreaticFunctionStatus;
    private String pancreaticFunctionDetail;

    // ========== 医学检查 - 胰岛功能/C肽 ==========
    private String cPeptideStatus;
    private String cPeptideDetail;

    // ========== 医学检查 - 维生素D ==========
    private String vitaminDStatus;
    private String vitaminDDetail;

    // ========== 医学检查 - D二聚体 ==========
    private String dDimerStatus;
    private String dDimerDetail;

    // ========== 影像学检查 - 彩超 ==========
    private String ultrasoundUterusStatus;
    private String ultrasoundUterusDetail;
    private String ultrasoundBreastStatus;
    private String ultrasoundBreastDetail;
    private String ultrasoundLiverStatus;
    private String ultrasoundLiverDetail;

    // ========== 影像学检查 - 骨密度 ==========
    private String boneDensityStatus;
    private String boneDensityDetail;

    // ========== 影像学检查 - CT腹部脂肪容积 ==========
    private String ctAbdominalFatStatus;
    private String ctAbdominalFatDetail;

    // ========== 运动与体能评估 - 心肺功能 ==========
    private Integer maxHeartRate;
    private BigDecimal sixMwt;
    private BigDecimal vo2Max;

    // ========== 运动与体能评估 - 肌肉力量 ==========
    private BigDecimal upperLimb1rm;
    private BigDecimal lowerLimb1rm;

    // ========== 运动与体能评估 - 肌肉耐力 ==========
    private Integer upperLimbEndurance;
    private Integer lowerLimbEndurance;

    // ========== 运动与体能评估 - 柔韧性 ==========
    private BigDecimal sitReachCm;

    // ========== 运动与体能评估 - 平衡测试 ==========
    private BigDecimal balanceLeftEyeClosed;
    private BigDecimal balanceRightEyeClosed;

    // ========== 系统字段 ==========
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
