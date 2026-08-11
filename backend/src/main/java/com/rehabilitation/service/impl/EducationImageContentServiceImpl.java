package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.dto.EducationImageContentDTO;
import com.rehabilitation.entity.EducationImageContent;
import com.rehabilitation.mapper.EducationImageContentMapper;
import com.rehabilitation.service.EducationImageContentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 医教图文内容服务实现类
 */
@Service
public class EducationImageContentServiceImpl extends ServiceImpl<EducationImageContentMapper, EducationImageContent>
        implements EducationImageContentService {

    @Override
    public EducationImageContent saveContent(EducationImageContentDTO dto) {
        EducationImageContent content = new EducationImageContent();

        // 生成内容ID
        String contentId = "IMG_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
        content.setContentId(contentId);

        // 设置基本信息
        content.setContentType(dto.getContentType());
        content.setContentTitle(dto.getContentTitle());
        content.setAuthor(dto.getAuthor());
        content.setAuthorType(dto.getAuthorType());
        content.setTags(dto.getTags());
        content.setCategorySection(dto.getCategorySection());
        content.setPublishTime(dto.getPublishTime());
        content.setSupportGameMode(dto.getSupportGameMode());
        content.setIsOriginal(dto.getIsOriginal());
        content.setArticleDescription(dto.getArticleDescription());
        content.setCoverImage(dto.getCoverImage());
        content.setContentSource(dto.getContentSource());
        content.setArticleContent(dto.getArticleContent());
        content.setExternalLink(dto.getExternalLink());
        content.setModuleCategory(dto.getModuleCategory());
        content.setLikeCountBase(dto.getLikeCountBase() != null ? dto.getLikeCountBase() : 0);
        content.setViewCountBase(dto.getViewCountBase() != null ? dto.getViewCountBase() : 0);
        content.setLikeCount(0);
        content.setViewCount(0);
        content.setContentStatus(dto.getContentStatus() != null ? dto.getContentStatus() : "草稿");
        content.setArticleLink(dto.getArticleLink());
        content.setOperator(dto.getOperator() != null ? dto.getOperator() : "系统管理员");

        // 手动设置时间字段
        LocalDateTime now = LocalDateTime.now();
        content.setCreatedTime(now);
        content.setUpdatedTime(now);

        // 保存到数据库
        this.save(content);
        return content;
    }

    @Override
    public Page<EducationImageContent> getContentPage(Integer pageNum, Integer pageSize,
                                                        String contentTitle, String author,
                                                        String categorySection, String contentStatus,
                                                        String publishStartTime, String publishEndTime) {
        Page<EducationImageContent> page = new Page<>(pageNum, pageSize);
        QueryWrapper<EducationImageContent> wrapper = new QueryWrapper<>();

        // 添加查询条件
        if (StringUtils.hasText(contentTitle)) {
            wrapper.like("content_title", contentTitle);
        }
        if (StringUtils.hasText(author)) {
            wrapper.like("author", author);
        }
        if (StringUtils.hasText(categorySection)) {
            wrapper.eq("category_section", categorySection);
        }
        if (StringUtils.hasText(contentStatus)) {
            wrapper.eq("content_status", contentStatus);
        }
        if (StringUtils.hasText(publishStartTime)) {
            wrapper.ge("publish_time", publishStartTime);
        }
        if (StringUtils.hasText(publishEndTime)) {
            wrapper.le("publish_time", publishEndTime);
        }

        // 按创建时间倒序排列
        wrapper.orderByDesc("created_time");

        return this.page(page, wrapper);
    }
}
