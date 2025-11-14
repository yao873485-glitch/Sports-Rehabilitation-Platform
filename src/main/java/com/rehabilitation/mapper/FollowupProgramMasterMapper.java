package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.FollowupProgramQueryDTO;
import com.rehabilitation.dto.FollowupProgramVO;
import com.rehabilitation.entity.FollowupProgramMaster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 随访项目主数据Mapper接口
 */
@Mapper
public interface FollowupProgramMasterMapper extends BaseMapper<FollowupProgramMaster> {

    /**
     * 分页查询随访项目
     */
    IPage<FollowupProgramVO> selectFollowupProgramPage(Page<FollowupProgramVO> page, @Param("dto") FollowupProgramQueryDTO queryDTO);

    /**
     * 根据ID查询随访项目详情
     */
    FollowupProgramVO selectFollowupProgramById(@Param("id") Long id);
}