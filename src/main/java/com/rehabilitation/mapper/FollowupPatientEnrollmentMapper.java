package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.FollowupEnrollmentVO;
import com.rehabilitation.dto.FollowupQueryDTO;
import com.rehabilitation.entity.FollowupPatientEnrollment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 随访患者入组记录Mapper
 */
@Mapper
public interface FollowupPatientEnrollmentMapper extends BaseMapper<FollowupPatientEnrollment> {

    /**
     * 分页查询随访患者列表（带JOIN查询完整信息）
     */
    IPage<FollowupEnrollmentVO> selectFollowupPage(Page<FollowupEnrollmentVO> page,
                                                   @Param("query") FollowupQueryDTO query);

    /**
     * 根据ID查询随访患者详情（带JOIN查询完整信息）
     */
    FollowupEnrollmentVO selectFollowupById(@Param("id") Long id);

    /**
     * 插入入组记录
     */
    int insertEnrollment(Map<String, Object> params);
}
