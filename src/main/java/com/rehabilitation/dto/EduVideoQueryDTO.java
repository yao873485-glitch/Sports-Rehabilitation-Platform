package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教育视频素材查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "教育视频素材查询条件")
public class EduVideoQueryDTO extends PageQuery {

    @Schema(description = "视频标题")
    private String videoTitle;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "所属板块")
    private String categorySection;

    @Schema(description = "内容状态")
    private String contentStatus;

    @Schema(description = "发布开始时间")
    private String publishStartTime;

    @Schema(description = "发布结束时间")
    private String publishEndTime;
}