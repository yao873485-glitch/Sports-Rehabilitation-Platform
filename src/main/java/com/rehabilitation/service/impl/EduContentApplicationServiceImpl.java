package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.EduContentApplicationQueryDTO;
import com.rehabilitation.dto.EduContentApplicationVO;
import com.rehabilitation.entity.EduContentApplication;
import com.rehabilitation.mapper.EduContentApplicationMapper;
import com.rehabilitation.service.EduContentApplicationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 内容应用管理Service实现类
 */
@Service
public class EduContentApplicationServiceImpl extends ServiceImpl<EduContentApplicationMapper, EduContentApplication>
        implements EduContentApplicationService {

    @Resource
    private EduContentApplicationMapper contentApplicationMapper;

    @Override
    public PageResult<EduContentApplicationVO> getContentApplicationPage(EduContentApplicationQueryDTO queryDTO) {
        Page<EduContentApplication> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<EduContentApplication> wrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        if (StringUtils.hasText(queryDTO.getContentTitle())) {
            wrapper.like(EduContentApplication::getContentTitle, queryDTO.getContentTitle());
        }
        if (StringUtils.hasText(queryDTO.getAuthor())) {
            wrapper.like(EduContentApplication::getAuthor, queryDTO.getAuthor());
        }
        if (StringUtils.hasText(queryDTO.getResourceType())) {
            wrapper.eq(EduContentApplication::getContentType, queryDTO.getResourceType());
        }
        if (StringUtils.hasText(queryDTO.getCategorySection())) {
            wrapper.eq(EduContentApplication::getCategorySection, queryDTO.getCategorySection());
        }
        if (StringUtils.hasText(queryDTO.getStatus())) {
            wrapper.eq(EduContentApplication::getContentStatus, queryDTO.getStatus());
        }
        if (StringUtils.hasText(queryDTO.getChannel())) {
            wrapper.eq(EduContentApplication::getChannel, queryDTO.getChannel());
        }

        // 置顶状态处理
        if (StringUtils.hasText(queryDTO.getPinStatus())) {
            if ("已置顶".equals(queryDTO.getPinStatus())) {
                wrapper.eq(EduContentApplication::getPinStatus, 1);
            } else if ("未置顶".equals(queryDTO.getPinStatus())) {
                wrapper.eq(EduContentApplication::getPinStatus, 0);
            }
        }

        // 按创建时间倒序
        wrapper.orderByDesc(EduContentApplication::getCreatedAt);

        IPage<EduContentApplication> result = this.page(page, wrapper);

        // 转换为VO
        List<EduContentApplicationVO> voList = result.getRecords().stream().map(entity -> {
            EduContentApplicationVO vo = new EduContentApplicationVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList());

        return PageResult.build(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public EduContentApplicationVO getContentApplicationById(Long id) {
        EduContentApplication entity = this.getById(id);
        if (entity == null) {
            return null;
        }

        EduContentApplicationVO vo = new EduContentApplicationVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    @Override
    public boolean updateContentApplication(EduContentApplicationVO vo) {
        EduContentApplication entity = new EduContentApplication();
        BeanUtils.copyProperties(vo, entity);
        return this.updateById(entity);
    }

    @Override
    public boolean updateContentStatus(Long id, String status) {
        EduContentApplication entity = this.getById(id);
        if (entity == null) {
            return false;
        }
        entity.setContentStatus(status);
        return this.updateById(entity);
    }

    @Override
    public boolean deleteContentApplication(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean createContentApplication(String contentId, String contentType, String contentTitle,
                                            String contentDescription, String categorySection, String author,
                                            Integer viewCount, Integer likeCount) {
        // 检查是否已存在
        LambdaQueryWrapper<EduContentApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduContentApplication::getContentId, contentId);
        EduContentApplication existing = this.getOne(wrapper);

        if (existing != null) {
            // 已存在，更新基本信息
            existing.setContentTitle(contentTitle);
            existing.setContentDescription(contentDescription);
            existing.setCategorySection(categorySection);
            existing.setAuthor(author);
            existing.setViewCount(viewCount);
            existing.setLikeCount(likeCount);
            return this.updateById(existing);
        }

        // 不存在，创建新记录
        EduContentApplication newEntity = new EduContentApplication();
        newEntity.setContentId(contentId);
        newEntity.setContentType(contentType);
        newEntity.setContentTitle(contentTitle);
        newEntity.setContentDescription(contentDescription);
        newEntity.setCategorySection(categorySection);
        newEntity.setAuthor(author);
        newEntity.setViewCount(viewCount != null ? viewCount : 0);
        newEntity.setLikeCount(likeCount != null ? likeCount : 0);
        newEntity.setContentStatus("未上架");
        newEntity.setPinStatus(0);
        newEntity.setContentConfig("配置");

        return this.save(newEntity);
    }
}
