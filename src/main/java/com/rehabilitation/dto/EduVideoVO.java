package com.rehabilitation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 教育视频素材视图对象
 */
@Data
@Schema(description = "教育视频素材信息")
public class EduVideoVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "内容类型")
    private String contentType;

    @Schema(description = "视频标题")
    private String videoTitle;

    @Schema(description = "视频ID")
    private String videoId;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "作者类型")
    private String authorType;

    @Schema(description = "所属板块")
    private String categorySection;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "是否支持游戏模式")
    private Integer supportGameMode;

    @Schema(description = "是否原创")
    private Integer isOriginal;

    @Schema(description = "视频描述")
    private String videoDescription;

    @Schema(description = "封面图")
    private String coverImage;

    @Schema(description = "视频来源")
    private String videoSource;

    @Schema(description = "视频URL")
    private String videoUrl;

    @Schema(description = "所属模块")
    private String moduleCategory;

    @Schema(description = "阅读基数")
    private Integer viewCountBase;

    @Schema(description = "点赞基数")
    private Integer likeCountBase;

    @Schema(description = "内容状态")
    private String contentStatus;

    @Schema(description = "播放量")
    private Integer viewCount;

    @Schema(description = "点赞量")
    private Integer likeCount;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "更新人")
    private String updatedBy;
}
