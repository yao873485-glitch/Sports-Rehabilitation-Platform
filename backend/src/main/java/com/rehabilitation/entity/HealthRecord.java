package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 健康档案实体类（优化版：移除与patient/patient_detail重复字段）
 *
 * 说明：姓名、性别、出生日期、证件号等基本信息存储在patient表中
 *      民族、职业等信息存储在patient_detail表中
 *      本表只存储健康档案特有的信息
 */
@Data
@TableName(value = "health_record", autoResultMap = true)
public class HealthRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer patientId;

    // ========== 健康档案特有的基本信息 ==========
    private String workUnit;          // 工作单位/学校
    private String nativePlace;       // 籍贯
    private String birthPlace;        // 出生地

    // 紧急联系人
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String emergencyContactPhone;

    // 常驻信息
    private String residenceType;
    private String householdAddress;
    private String residenceAddress;

    // 文化程度、婚姻、医疗费用（occupation在patient_detail中）
    private String educationLevel;
    private String maritalStatus;
    private String medicalPaymentMethod;

    // ========== 二、过敏史 ==========
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> allergyList;

    private String allergyDetail;

    // ========== 三、暴露史 ==========
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> exposureList;

    // ========== 四、既往史 ==========
    // 疾病
    private String diseaseHistory;
    private String diseaseDetail;

    // 手术
    private String surgeryHistory;
    private String surgeryDetail;

    // 外伤
    private String traumaHistory;
    private String traumaDetail;

    // 输血
    private String transfusionHistory;
    private String transfusionDetail;

    // ========== 五、预防接种史 ==========
    private String vaccinationHistory;
    private String vaccinationDetail;

    // ========== 六、家族史 ==========
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> fatherDiseaseList;

    private String fatherDetail;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> motherDiseaseList;

    private String motherDetail;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> siblingsDiseaseList;

    private String siblingsDetail;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> childrenDiseaseList;

    private String childrenDetail;

    // ========== 七、遗传病史 ==========
    private String geneticDiseaseHistory;
    private String geneticDiseaseDetail;

    // ========== 八、残疾情况 ==========
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> disabilityList;

    private String disabilityDetail;

    // ========== 九、个人健康标识 ==========
    private String healthIdentity;
    private String pregnancyRiskLevel;

    // ========== 慢性/重点疾病 ==========
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> chronicDiseaseList;

    // ========== 法定传染病 ==========
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> infectiousDiseaseList;

    private String infectiousDiseaseDetail;

    // ========== 体重和血型 ==========
    private String weightStatus;
    private String bloodTypeAbo;
    private String bloodTypeRh;

    // ========== 系统字段 ==========
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
