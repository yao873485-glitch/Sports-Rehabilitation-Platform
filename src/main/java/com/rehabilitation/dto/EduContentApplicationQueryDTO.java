package com.rehabilitation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 内容应用管理查询DTO
 */
@Data
@Schema(description = "内容应用管理查询条件")
public class EduContentApplicationQueryDTO {

    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页大小")
    private Integer pageSize = 10;

    @Schema(description = "内容标题")
    private String contentTitle;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "资源类型")
    private String resourceType;

    @Schema(description = "所属版块")
    private String categorySection;

    @Schema(description = "上下架状态")
    private String status;

    @Schema(description = "置顶状态")
    private String pinStatus;

    @Schema(description = "所属频道")
    private String channel;

    @Schema(description = "上架开始时间")
    private String publishStartTime;

    @Schema(description = "上架结束时间")
    private String publishEndTime;
}
