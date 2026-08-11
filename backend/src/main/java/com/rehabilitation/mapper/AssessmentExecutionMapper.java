package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.AssessmentExecutionQueryDTO;
import com.rehabilitation.dto.AssessmentExecutionVO;
import com.rehabilitation.entity.AssessmentExecutionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 评定执行Mapper接口
 */
@Mapper
public interface AssessmentExecutionMapper extends BaseMapper<AssessmentExecutionRecord> {

    /**
     * 分页查询评定执行列表
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<AssessmentExecutionVO> selectAssessmentExecutionPage(
            Page<AssessmentExecutionVO> page,
            @Param("query") AssessmentExecutionQueryDTO query);

    /**
     * 根据ID查询评定执行详情
     *
     * @param id 主键ID
     * @return 评定执行详情
     */
    AssessmentExecutionVO selectAssessmentExecutionById(@Param("id") Integer id);
}