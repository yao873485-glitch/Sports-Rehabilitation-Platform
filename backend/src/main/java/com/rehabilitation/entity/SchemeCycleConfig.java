package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 方案周期配置实体类
 *
 * 用于存储患者的方案周期时间配置
 * 注意：由于 Lombok 编译问题，为关键字段添加了手动的 getter/setter 方法
 */
@Data
@TableName("scheme_cycle_config")
public class SchemeCycleConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer patientId;

    private String schemeId;

    private Integer cycleMonths;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer status;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String remarks;

    // 手动添加 patientId 的 getter/setter 方法（解决 Lombok 编译问题）
    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    // 手动添加 cycleMonths 的 getter/setter 方法
    public Integer getCycleMonths() {
        return cycleMonths;
    }

    public void setCycleMonths(Integer cycleMonths) {
        this.cycleMonths = cycleMonths;
    }

    // 手动添加 schemeId 的 getter/setter 方法
    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }
}
