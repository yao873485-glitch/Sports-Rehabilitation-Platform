package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.FollowupEnrollmentVO;
import com.rehabilitation.dto.FollowupQueryDTO;
import com.rehabilitation.entity.FollowupPatientList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 随访患者列表Mapper
 */
@Mapper
public interface FollowupPatientListMapper extends BaseMapper<FollowupPatientList> {

    /**
     * 分页查询随访患者列表
     */
    IPage<FollowupEnrollmentVO> selectFollowupPage(Page<FollowupEnrollmentVO> page, @Param("query") FollowupQueryDTO query);

    /**
     * 根据ID查询随访患者详情
     */
    FollowupEnrollmentVO selectFollowupById(@Param("id") Long id);

    /**
     * 根据患者ID查询随访记录
     */
    FollowupPatientList selectByPatientId(@Param("patientId") Integer patientId);
}
