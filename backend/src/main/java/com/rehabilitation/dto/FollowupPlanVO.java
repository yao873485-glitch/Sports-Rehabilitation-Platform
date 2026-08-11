package com.rehabilitation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 随访计划VO
 */
@Data
@ApiModel(description = "随访计划信息")
public class FollowupPlanVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "随访项目")
    private String linkedProject;

    @ApiModelProperty(value = "当前版本号")
    private String versionNumber;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "最近一次修改时间")
    private LocalDateTime lastModifiedTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "版本备注")
    private String versionRemark;

    @ApiModelProperty(value = "随访邀请二维码")
    private String invitationQrCodeUrl;
}