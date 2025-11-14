package com.rehabilitation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 教育内容VO
 */
@Data
@ApiModel(description = "教育内容信息")
public class EduContentVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "内容标题")
    private String contentTitle;

    @ApiModelProperty(value = "内容ID")
    private String contentId;

    @ApiModelProperty(value = "内容类型")
    private String contentType;

    @ApiModelProperty(value = "所属板块")
    private String categorySection;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "作者类型")
    private String authorType;

    @ApiModelProperty(value = "标签")
    private String tags;

    @ApiModelProperty(value = "内容状态")
    private String contentStatus;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "是否支持游戏模式")
    private Integer supportGameMode;

    @ApiModelProperty(value = "是否原创")
    private Integer isOriginal;

    @ApiModelProperty(value = "文章描述")
    private String articleDescription;

    @ApiModelProperty(value = "封面图")
    private String coverImage;

    @ApiModelProperty(value = "文章来源")
    private String contentSource;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "外链地址")
    private String externalLink;

    @ApiModelProperty(value = "所属模块")
    private String moduleCategory;

    @ApiModelProperty(value = "阅读量")
    private Integer viewCount;

    @ApiModelProperty(value = "点赞量")
    private Integer likeCount;

    @ApiModelProperty(value = "阅读基数")
    private Integer viewCountBase;

    @ApiModelProperty(value = "点赞基数")
    private Integer likeCountBase;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "更新人")
    private String updatedBy;
}