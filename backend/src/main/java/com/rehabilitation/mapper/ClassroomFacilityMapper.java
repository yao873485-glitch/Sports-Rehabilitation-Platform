package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.dto.ClassroomFacilityQueryDTO;
import com.rehabilitation.dto.ClassroomFacilityVO;
import com.rehabilitation.entity.ClassroomFacilityInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 教室设施信息Mapper接口
 */
@Mapper
public interface ClassroomFacilityMapper extends BaseMapper<ClassroomFacilityInfo> {

    /**
     * 分页查询教室设施信息
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<ClassroomFacilityVO> selectClassroomFacilityPage(IPage<ClassroomFacilityVO> page, @Param("query") ClassroomFacilityQueryDTO query);

    /**
     * 根据ID查询教室设施详情
     *
     * @param id 主键ID
     * @return 教室设施详情
     */
    ClassroomFacilityVO selectClassroomFacilityById(@Param("id") Integer id);
}