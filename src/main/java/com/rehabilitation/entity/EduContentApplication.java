package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 内容应用管理实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("edu_content_application")
@Schema(description = "内容应用管理")
public class EduContentApplication {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @TableField("content_id")
    @Schema(description = "内容ID")
    private String contentId;

    @TableField("content_type")
    @Schema(description = "资源类型")
    private String contentType;

    @TableField("content_title")
    @Schema(description = "内容标题")
    private String contentTitle;

    @TableField("content_description")
    @Schema(description = "内容描述")
    private String contentDescription;

    @TableField("category_section")
    @Schema(description = "所属版块")
    private String categorySection;

    @TableField("author")
    @Schema(description = "作者")
    private String author;

    @TableField("application_module")
    @Schema(description = "应用模块")
    private String applicationModule;

    @TableField("application_project")
    @Schema(description = "应用项目")
    private String applicationProject;

    @TableField("channel")
    @Schema(description = "所属频道")
    private String channel;

    @TableField("related_disease")
    @Schema(description = "关联疾病")
    private String relatedDisease;

    @TableField("related_symptom")
    @Schema(description = "关联症状")
    private String relatedSymptom;

    @TableField("related_product")
    @Schema(description = "关联商品")
    private String relatedProduct;

    @TableField("pin_status")
    @Schema(description = "置顶状态")
    private Integer pinStatus;

    @TableField("content_status")
    @Schema(description = "上下架状态")
    private String contentStatus;

    @TableField("content_config")
    @Schema(description = "内容应用配置标识")
    private String contentConfig;

    @TableField("view_count")
    @Schema(description = "阅读量")
    private Integer viewCount;

    @TableField("like_count")
    @Schema(description = "点赞量")
    private Integer likeCount;

    @TableField("publish_time")
    @Schema(description = "上架时间")
    private LocalDateTime publishTime;

    @TableField("article_url")
    @Schema(description = "文章链接")
    private String articleUrl;

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
