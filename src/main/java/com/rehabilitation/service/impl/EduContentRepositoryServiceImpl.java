package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.EduContentQueryDTO;
import com.rehabilitation.dto.EduContentVO;
import com.rehabilitation.entity.EduContentRepository;
import com.rehabilitation.mapper.EduContentRepositoryMapper;
import com.rehabilitation.service.EduContentApplicationService;
import com.rehabilitation.service.EduContentRepositoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 教育内容资源库Service实现类
 */
@Service
public class EduContentRepositoryServiceImpl extends ServiceImpl<EduContentRepositoryMapper, EduContentRepository>
        implements EduContentRepositoryService {

    @Resource
    private EduContentRepositoryMapper eduContentRepositoryMapper;

    @Resource
    private EduContentApplicationService contentApplicationService;

    @Override
    public PageResult<EduContentVO> getEduContentPage(EduContentQueryDTO queryDTO) {
        Page<EduContentVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<EduContentVO> result = eduContentRepositoryMapper.selectEduContentPage(page, queryDTO);

        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public EduContentVO getEduContentById(Long id) {
        return eduContentRepositoryMapper.selectEduContentById(id);
    }

    @Override
    public EduContentVO getEduContentByContentId(String contentId) {
        EduContentRepository entity = this.lambdaQuery()
                .eq(EduContentRepository::getContentId, contentId)
                .one();
        if (entity == null) {
            return null;
        }
        return eduContentRepositoryMapper.selectEduContentById(entity.getId());
    }

    @Override
    public boolean addEduContent(EduContentVO contentVO) {
        EduContentRepository entity = new EduContentRepository();
        BeanUtils.copyProperties(contentVO, entity);

        // 生成唯一的内容ID
        if (entity.getContentId() == null || entity.getContentId().isEmpty()) {
            entity.setContentId("CONTENT_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());
        }

        // 设置默认值
        if (entity.getContentStatus() == null) {
            entity.setContentStatus("草稿");
        }
        if (entity.getViewCount() == null) {
            entity.setViewCount(entity.getViewCountBase() != null ? entity.getViewCountBase() : 0);
        }
        if (entity.getLikeCount() == null) {
            entity.setLikeCount(entity.getLikeCountBase() != null ? entity.getLikeCountBase() : 0);
        }

        boolean saved = this.save(entity);

        // 同步创建内容应用管理记录
        if (saved) {
            contentApplicationService.createContentApplication(
                entity.getContentId(),
                "文章",
                contentVO.getContentTitle(),
                contentVO.getArticleDescription(),
                contentVO.getCategorySection(),
                contentVO.getAuthor(),
                entity.getViewCount(),
                entity.getLikeCount()
            );
        }

        return saved;
    }

    @Override
    public boolean updateEduContent(EduContentVO contentVO) {
        EduContentRepository entity = new EduContentRepository();
        BeanUtils.copyProperties(contentVO, entity);
        return this.updateById(entity);
    }

    @Override
    public boolean deleteEduContent(Long id) {
        return this.removeById(id);
    }
}