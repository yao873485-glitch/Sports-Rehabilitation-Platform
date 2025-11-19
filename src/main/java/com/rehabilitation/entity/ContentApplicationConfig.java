package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 内容应用配置实体类
 * 用于存储图文和视频内容的应用配置信息
 */
@Data
@TableName("content_application_config")
public class ContentApplicationConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 内容ID：关联education_image_content或edu_video_asset的content_id/video_id
     */
    private String contentId;

    /**
     * 内容类型：文章或视频
     */
    private String contentType;

    /**
     * 应用模块：如"健康管理"、"疾病管理"等
     */
    private String applicationModule;

    /**
     * 应用项目：具体的应用项目名称
     */
    private String applicationProject;

    /**
     * 所属频道：如"儿童生长发育"、"育儿健康"等
     */
    private String channel;

    /**
     * 关联疾病：多个疾病用逗号分隔
     */
    private String relatedDiseases;

    /**
     * 关联症状：多个症状用逗号分隔
     */
    private String relatedSymptoms;

    /**
     * 关联商品：多个商品用逗号分隔
     */
    private String relatedProducts;

    /**
     * 自定义标签：多个标签用逗号分隔
     */
    private String customTags;

    /**
     * 排序顺序：数字越小越靠前
     */
    private Integer sortOrder;

    /**
     * 是否置顶：0-否，1-是
     */
    private Integer isPinned;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}
