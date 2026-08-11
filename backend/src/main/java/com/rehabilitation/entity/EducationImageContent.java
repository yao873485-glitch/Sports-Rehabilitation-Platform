package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 医教图文内容实体类
 */
@Data
@TableName("education_image_content")
public class EducationImageContent {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 内容ID（业务主键）
     */
    @TableField("content_id")
    private String contentId;

    /**
     * 内容类型
     */
    @TableField("content_type")
    private String contentType;

    /**
     * 文章标题
     */
    @TableField("content_title")
    private String contentTitle;

    /**
     * 作者
     */
    @TableField("author")
    private String author;

    /**
     * 作者类型
     */
    @TableField("author_type")
    private String authorType;

    /**
     * 文章标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 所属板块
     */
    @TableField("category_section")
    private String categorySection;

    /**
     * 发布时间
     */
    @TableField("publish_time")
    private LocalDateTime publishTime;

    /**
     * 是否支持游客模式：0-不支持，1-支持
     */
    @TableField("support_game_mode")
    private Integer supportGameMode;

    /**
     * 是否原创：0-否，1-是
     */
    @TableField("is_original")
    private Integer isOriginal;

    /**
     * 文章描述
     */
    @TableField("article_description")
    private String articleDescription;

    /**
     * 封面图URL
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * 文章来源：系统录入、外链
     */
    @TableField("content_source")
    private String contentSource;

    /**
     * 文章内容（富文本）
     */
    @TableField("article_content")
    private String articleContent;

    /**
     * 外链地址
     */
    @TableField("external_link")
    private String externalLink;

    /**
     * 所属模块：医教、患教
     */
    @TableField("module_category")
    private String moduleCategory;

    /**
     * 点赞基数
     */
    @TableField("like_count_base")
    private Integer likeCountBase;

    /**
     * 阅读基数
     */
    @TableField("view_count_base")
    private Integer viewCountBase;

    /**
     * 实际点赞量
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 实际阅读量
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 内容状态：草稿、待审核、已上架、未上架、已下架
     */
    @TableField("content_status")
    private String contentStatus;

    /**
     * 文章链接
     */
    @TableField("article_link")
    private String articleLink;

    /**
     * 操作人
     */
    @TableField("operator")
    private String operator;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    // Manual getters and setters to avoid Lombok issues

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
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

    public String getCategorySection() {
        return categorySection;
    }

    public void setCategorySection(String categorySection) {
        this.categorySection = categorySection;
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

    public Integer getLikeCountBase() {
        return likeCountBase;
    }

    public void setLikeCountBase(Integer likeCountBase) {
        this.likeCountBase = likeCountBase;
    }

    public Integer getViewCountBase() {
        return viewCountBase;
    }

    public void setViewCountBase(Integer viewCountBase) {
        this.viewCountBase = viewCountBase;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getContentStatus() {
        return contentStatus;
    }

    public void setContentStatus(String contentStatus) {
        this.contentStatus = contentStatus;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
