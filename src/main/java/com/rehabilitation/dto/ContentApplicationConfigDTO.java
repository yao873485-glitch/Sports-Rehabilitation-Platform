package com.rehabilitation.dto;

import lombok.Data;

/**
 * 内容应用配置DTO
 */
@Data
public class ContentApplicationConfigDTO {
    private Long id;
    private String contentId;
    private String contentType;
    private String applicationModule;
    private String applicationProject;
    private String channel;
    private String relatedDiseases;
    private String relatedSymptoms;
    private String relatedProducts;
    private String customTags;
    private Integer sortOrder;
    private Integer isPinned;
    private String remark;
    private String operator;

    // 手动添加getter/setter以避免Lombok问题
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

    public String getApplicationModule() {
        return applicationModule;
    }

    public void setApplicationModule(String applicationModule) {
        this.applicationModule = applicationModule;
    }

    public String getApplicationProject() {
        return applicationProject;
    }

    public void setApplicationProject(String applicationProject) {
        this.applicationProject = applicationProject;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRelatedDiseases() {
        return relatedDiseases;
    }

    public void setRelatedDiseases(String relatedDiseases) {
        this.relatedDiseases = relatedDiseases;
    }

    public String getRelatedSymptoms() {
        return relatedSymptoms;
    }

    public void setRelatedSymptoms(String relatedSymptoms) {
        this.relatedSymptoms = relatedSymptoms;
    }

    public String getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(String relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    public String getCustomTags() {
        return customTags;
    }

    public void setCustomTags(String customTags) {
        this.customTags = customTags;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(Integer isPinned) {
        this.isPinned = isPinned;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
