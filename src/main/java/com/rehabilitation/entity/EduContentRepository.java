package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 教育内容资源库实体类
 */
@ApiModel(description = "教育内容资源库")
@TableName("edu_content_repository")
public class EduContentRepository {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "内容标题")
    @TableField("content_title")
    private String contentTitle;

    @ApiModelProperty(value = "内容ID")
    @TableField("content_id")
    private String contentId;

    @ApiModelProperty(value = "内容类型")
    @TableField("content_type")
    private String contentType;

    @ApiModelProperty(value = "所属板块")
    @TableField("category_section")
    private String categorySection;

    @ApiModelProperty(value = "作者")
    @TableField("author")
    private String author;

    @ApiModelProperty(value = "作者类型")
    @TableField("author_type")
    private String authorType;

    @ApiModelProperty(value = "标签")
    @TableField("tags")
    private String tags;

    @ApiModelProperty(value = "内容状态")
    @TableField("content_status")
    private String contentStatus;

    @ApiModelProperty(value = "发布时间")
    @TableField("publish_time")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "是否支持游戏模式")
    @TableField("support_game_mode")
    private Integer supportGameMode;

    @ApiModelProperty(value = "是否原创")
    @TableField("is_original")
    private Integer isOriginal;

    @ApiModelProperty(value = "文章描述")
    @TableField("article_description")
    private String articleDescription;

    @ApiModelProperty(value = "封面图")
    @TableField("cover_image")
    private String coverImage;

    @ApiModelProperty(value = "文章来源")
    @TableField("content_source")
    private String contentSource;

    @ApiModelProperty(value = "文章内容")
    @TableField("article_content")
    private String articleContent;

    @ApiModelProperty(value = "外链地址")
    @TableField("external_link")
    private String externalLink;

    @ApiModelProperty(value = "所属模块")
    @TableField("module_category")
    private String moduleCategory;

    @ApiModelProperty(value = "阅读量")
    @TableField("view_count")
    private Integer viewCount;

    @ApiModelProperty(value = "点赞量")
    @TableField("like_count")
    private Integer likeCount;

    @ApiModelProperty(value = "阅读基数")
    @TableField("view_count_base")
    private Integer viewCountBase;

    @ApiModelProperty(value = "点赞基数")
    @TableField("like_count_base")
    private Integer likeCountBase;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "创建人")
    @TableField("created_by")
    private String createdBy;

    @ApiModelProperty(value = "更新人")
    @TableField("updated_by")
    private String updatedBy;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCategorySection() {
        return categorySection;
    }

    public void setCategorySection(String categorySection) {
        this.categorySection = categorySection;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getContentStatus() {
        return contentStatus;
    }

    public void setContentStatus(String contentStatus) {
        this.contentStatus = contentStatus;
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

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getContentSource() {
        return contentSource;
    }

    public void setContentSource(String contentSource) {
        this.contentSource = contentSource;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getModuleCategory() {
        return moduleCategory;
    }

    public void setModuleCategory(String moduleCategory) {
        this.moduleCategory = moduleCategory;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
