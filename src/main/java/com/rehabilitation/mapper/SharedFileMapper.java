package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.SharedFileQueryDTO;
import com.rehabilitation.dto.SharedFileVO;
import com.rehabilitation.entity.SharedFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 共享文件Mapper
 */
@Mapper
public interface SharedFileMapper extends BaseMapper<SharedFile> {

    /**
     * 分页查询共享文件列表
     */
    IPage<SharedFileVO> selectSharedFilePage(Page<SharedFileVO> page, 
                                           @Param("query") SharedFileQueryDTO query);
}
