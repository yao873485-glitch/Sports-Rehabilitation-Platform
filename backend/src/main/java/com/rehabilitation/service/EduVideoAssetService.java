package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.EduVideoQueryDTO;
import com.rehabilitation.dto.EduVideoVO;
import com.rehabilitation.entity.EduVideoAsset;

/**
 * 教育视频素材Service接口
 */
public interface EduVideoAssetService extends IService<EduVideoAsset> {

    /**
     * 分页查询教育视频素材
     */
    PageResult<EduVideoVO> getEduVideoPage(EduVideoQueryDTO queryDTO);

    /**
     * 根据ID获取教育视频素材详情
     */
    EduVideoVO getEduVideoById(Long id);

    /**
     * 根据videoId获取教育视频素材详情
     */
    EduVideoVO getEduVideoByVideoId(String videoId);

    /**
     * 添加教育视频素材
     */
    boolean addEduVideo(EduVideoVO videoVO);

    /**
     * 更新教育视频素材
     */
    boolean updateEduVideo(EduVideoVO videoVO);

    /**
     * 删除教育视频素材
     */
    boolean deleteEduVideo(Long id);
}