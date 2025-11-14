package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 教育视频素材实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("edu_video_asset")
@Schema(description = "教育视频素材")
public class EduVideoAsset {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @TableField("content_type")
    @Schema(description = "内容类型")
    private String contentType;

    @TableField("video_title")
    @Schema(description = "视频标题")
    private String videoTitle;

    @TableField("video_id")
    @Schema(description = "视频ID")
    private String videoId;

    @TableField("author")
    @Schema(description = "作者")
    private String author;

    @TableField("author_type")
    @Schema(description = "作者类型")
    private String authorType;

    @TableField("publish_time")
    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @TableField("support_game_mode")
    @Schema(description = "是否支持游戏模式")
    private Integer supportGameMode;

    @TableField("is_original")
    @Schema(description = "是否原创")
    private Integer isOriginal;

    @TableField("video_description")
    @Schema(description = "视频描述")
    private String videoDescription;

    @TableField("cover_image")
    @Schema(description = "封面图")
    private String coverImage;

    @TableField("video_source")
    @Schema(description = "视频来源")
    private String videoSource;

    @TableField("video_url")
    @Schema(description = "视频URL")
    private String videoUrl;

    @TableField("module_category")
    @Schema(description = "所属模块")
    private String moduleCategory;

    @TableField("view_count_base")
    @Schema(description = "阅读基数")
    private Integer viewCountBase;

    @TableField("like_count_base")
    @Schema(description = "点赞基数")
    private Integer likeCountBase;

    @TableField("category_section")
    @Schema(description = "所属板块")
    private String categorySection;

    @TableField("content_status")
    @Schema(description = "内容状态")
    private String contentStatus;

    @TableField("view_count")
    @Schema(description = "播放量")
    private Integer viewCount;

    @TableField("like_count")
    @Schema(description = "点赞量")
    private Integer likeCount;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @TableField("created_by")
    @Schema(description = "创建人")
    private String createdBy;

    @TableField("updated_by")
    @Schema(description = "更新人")
    private String updatedBy;
}
