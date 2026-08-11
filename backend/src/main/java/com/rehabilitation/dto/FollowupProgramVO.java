package com.rehabilitation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 随访项目VO
 */
@Data
@ApiModel(description = "随访项目信息")
public class FollowupProgramVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "项目编号")
    private String projectCode;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目介绍")
    private String projectDescription;

    @ApiModelProperty(value = "绑定随访计划")
    private String linkedFollowupPlan;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime lastModifiedTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "是否发布")
    private Integer isPublished;
}