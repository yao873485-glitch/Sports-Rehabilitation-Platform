package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.EduVideoQueryDTO;
import com.rehabilitation.dto.EduVideoVO;
import com.rehabilitation.entity.EduVideoAsset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 教育视频素材Mapper接口
 */
@Mapper
public interface EduVideoAssetMapper extends BaseMapper<EduVideoAsset> {

    /**
     * 分页查询教育视频素材
     */
    IPage<EduVideoVO> selectEduVideoPage(Page<EduVideoVO> page, @Param("queryDTO") EduVideoQueryDTO queryDTO);

    /**
     * 根据ID查询教育视频素材详情
     */
    EduVideoVO selectEduVideoById(@Param("id") Long id);
}
