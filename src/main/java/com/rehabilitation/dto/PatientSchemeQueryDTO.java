package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 患者方案查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PatientSchemeQueryDTO extends PageQuery {

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 搜索关键字（患者姓名、手机号或档案号）
     */
    private String searchKeyword;

    /**
     * 入组机构
     */
    private String orgName;

    /**
     * 方案状态：1-执行中，2-已结束
     */
    private Integer status;
}
