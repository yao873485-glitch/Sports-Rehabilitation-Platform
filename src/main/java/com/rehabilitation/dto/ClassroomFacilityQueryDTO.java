package com.rehabilitation.dto;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 教室设施查询条件DTO
 */
@Data
public class ClassroomFacilityQueryDTO {

    /**
     * 页码
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于0")
    private Integer pageSize = 20;

    /**
     * 运动类型筛选
     */
    private String exerciseType;

    /**
     * 状态筛选
     */
    private String status;

    /**
     * 搜索关键词（教室名称）
     */
    private String searchKeyword;
}