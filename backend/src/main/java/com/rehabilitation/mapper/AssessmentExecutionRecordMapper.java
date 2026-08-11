package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.AssessmentQueryDTO;
import com.rehabilitation.dto.AssessmentVO;
import com.rehabilitation.entity.AssessmentExecutionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 评定执行记录Mapper
 */
@Mapper
public interface AssessmentExecutionRecordMapper extends BaseMapper<AssessmentExecutionRecord> {

    /**
     * 分页查询评定执行记录列表
     */
    IPage<AssessmentVO> selectAssessmentPage(Page<AssessmentVO> page, 
                                           @Param("query") AssessmentQueryDTO query);
}
