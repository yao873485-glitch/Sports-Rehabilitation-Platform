package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.EduVideoQueryDTO;
import com.rehabilitation.dto.EduVideoVO;
import com.rehabilitation.entity.EduVideoAsset;
import com.rehabilitation.mapper.EduVideoAssetMapper;
import com.rehabilitation.service.EduContentApplicationService;
import com.rehabilitation.service.EduVideoAssetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 教育视频素材Service实现类
 */
@Service
public class EduVideoAssetServiceImpl extends ServiceImpl<EduVideoAssetMapper, EduVideoAsset>
        implements EduVideoAssetService {

    @Resource
    private EduVideoAssetMapper eduVideoAssetMapper;

    @Resource
    private EduContentApplicationService contentApplicationService;

    @Override
    public PageResult<EduVideoVO> getEduVideoPage(EduVideoQueryDTO queryDTO) {
        Page<EduVideoVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<EduVideoVO> result = eduVideoAssetMapper.selectEduVideoPage(page, queryDTO);

        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public EduVideoVO getEduVideoById(Long id) {
        return eduVideoAssetMapper.selectEduVideoById(id);
    }

    @Override
    public EduVideoVO getEduVideoByVideoId(String videoId) {
        EduVideoAsset entity = this.lambdaQuery()
                .eq(EduVideoAsset::getVideoId, videoId)
                .one();
        if (entity == null) {
            return null;
        }
        return eduVideoAssetMapper.selectEduVideoById(entity.getId());
    }

    @Override
    public boolean addEduVideo(EduVideoVO videoVO) {
        EduVideoAsset entity = new EduVideoAsset();
        BeanUtils.copyProperties(videoVO, entity);

        // 生成唯一的视频ID
        if (entity.getVideoId() == null || entity.getVideoId().isEmpty()) {
            entity.setVideoId("VIDEO_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());
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
                entity.getVideoId(),
                "视频",
                videoVO.getVideoTitle(),
                videoVO.getVideoDescription(),
                videoVO.getCategorySection(),
                videoVO.getAuthor(),
                entity.getViewCount(),
                entity.getLikeCount()
            );
        }

        return saved;
    }

    @Override
    public boolean updateEduVideo(EduVideoVO videoVO) {
        EduVideoAsset entity = new EduVideoAsset();
        BeanUtils.copyProperties(videoVO, entity);
        return this.updateById(entity);
    }

    @Override
    public boolean deleteEduVideo(Long id) {
        return this.removeById(id);
    }
}