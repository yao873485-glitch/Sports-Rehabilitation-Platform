package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.EduContentQueryDTO;
import com.rehabilitation.dto.EduContentVO;
import com.rehabilitation.entity.EduContentRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 教育内容资源库Mapper接口
 */
@Mapper
public interface EduContentRepositoryMapper extends BaseMapper<EduContentRepository> {

    /**
     * 分页查询教育内容
     */
    IPage<EduContentVO> selectEduContentPage(Page<EduContentVO> page, @Param("dto") EduContentQueryDTO queryDTO);

    /**
     * 根据ID查询教育内容详情
     */
    EduContentVO selectEduContentById(@Param("id") Long id);
}