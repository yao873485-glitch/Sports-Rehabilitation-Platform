package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 教育视频素材实体类
 */
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
    @Schema(description = "是否支持游客模式")
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

    @TableField("content_status")
    @Schema(description = "内容状态")
    private String contentStatus;

    @TableField("operator")
    @Schema(description = "操作人")
    private String operator;

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

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorType() {
        return authorType;
    }

    public void setAuthorType(String authorType) {
        this.authorType = authorType;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getSupportGameMode() {
        return supportGameMode;
    }

    public void setSupportGameMode(Integer supportGameMode) {
        this.supportGameMode = supportGameMode;
    }

    public Integer getIsOriginal() {
        return isOriginal;
    }

    public void setIsOriginal(Integer isOriginal) {
        this.isOriginal = isOriginal;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getModuleCategory() {
        return moduleCategory;
    }

    public void setModuleCategory(String moduleCategory) {
        this.moduleCategory = moduleCategory;
    }

    public Integer getViewCountBase() {
        return viewCountBase;
    }

    public void setViewCountBase(Integer viewCountBase) {
        this.viewCountBase = viewCountBase;
    }

    public Integer getLikeCountBase() {
        return likeCountBase;
    }

    public void setLikeCountBase(Integer likeCountBase) {
        this.likeCountBase = likeCountBase;
    }

    public String getContentStatus() {
        return contentStatus;
    }

    public void setContentStatus(String contentStatus) {
        this.contentStatus = contentStatus;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
