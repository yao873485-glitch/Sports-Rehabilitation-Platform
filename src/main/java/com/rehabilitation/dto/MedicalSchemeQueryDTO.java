package com.rehabilitation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医疗方案查询DTO
 */
@Data
public class MedicalSchemeQueryDTO {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 病种类型
     */
    private String diseaseType;

    /**
     * 开方医生
     */
    private String doctorName;

    /**
     * 搜索关键字（患者姓名/电话/档案号）
     */
    private String searchKeyword;

    /**
     * 方案状态：1-执行中，2-已结束
     */
    private Integer status;

    /**
     * 开始日期（用于按开方日期查询）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 结束日期（用于按开方日期查询）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}