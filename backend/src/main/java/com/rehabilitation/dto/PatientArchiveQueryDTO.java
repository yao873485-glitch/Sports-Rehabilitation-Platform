package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 患者档案查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PatientArchiveQueryDTO extends PageQuery {

    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 搜索关键字（患者姓名、手机号或档案号）
     */
    private String searchKeyword;

    /**
     * 患者姓名（用于前端搜索）
     */
    private String name;
}
