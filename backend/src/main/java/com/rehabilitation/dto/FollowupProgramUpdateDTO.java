package com.rehabilitation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 随访项目更新DTO
 */
@Data
@ApiModel(description = "随访项目更新信息")
public class FollowupProgramUpdateDTO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "项目名称")
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 255, message = "项目名称不能超过255个字符")
    private String projectName;

    @ApiModelProperty(value = "项目介绍")
    @Size(max = 1000, message = "项目介绍不能超过1000个字符")
    private String projectDescription;

    @ApiModelProperty(value = "绑定随访计划")
    @NotBlank(message = "绑定随访计划不能为空")
    @Size(max = 255, message = "绑定随访计划不能超过255个字符")
    private String linkedFollowupPlan;

    @ApiModelProperty(value = "是否发布")
    private Integer isPublished;
}