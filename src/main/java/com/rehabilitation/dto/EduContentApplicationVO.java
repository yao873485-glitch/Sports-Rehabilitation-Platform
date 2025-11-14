package com.rehabilitation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 内容应用管理视图对象
 */
@Data
@Schema(description = "内容应用管理信息")
public class EduContentApplicationVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "内容ID")
    private String contentId;

    @Schema(description = "资源类型")
    private String contentType;

    @Schema(description = "内容标题")
    private String contentTitle;

    @Schema(description = "内容描述")
    private String contentDescription;

    @Schema(description = "所属版块")
    private String categorySection;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "应用模块")
    private String applicationModule;

    @Schema(description = "应用项目")
    private String applicationProject;

    @Schema(description = "所属频道")
    private String channel;

    @Schema(description = "关联疾病")
    private String relatedDisease;

    @Schema(description = "关联症状")
    private String relatedSymptom;

    @Schema(description = "关联商品")
    private String relatedProduct;

    @Schema(description = "置顶状态")
    private Integer pinStatus;

    @Schema(description = "上下架状态")
    private String contentStatus;

    @Schema(description = "内容应用配置标识")
    private String contentConfig;

    @Schema(description = "阅读量")
    private Integer viewCount;

    @Schema(description = "点赞量")
    private Integer likeCount;

    @Schema(description = "上架时间")
    private LocalDateTime publishTime;

    @Schema(description = "文章链接")
    private String articleUrl;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "更新人")
    private String updatedBy;
}
