package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.AssessmentQueryDTO;
import com.rehabilitation.dto.AssessmentVO;
import com.rehabilitation.entity.AssessmentExecutionRecord;

/**
 * 评定执行记录服务接口
 */
public interface AssessmentService extends IService<AssessmentExecutionRecord> {

    /**
     * 分页查询评定执行记录列表
     */
    PageResult<AssessmentVO> getAssessmentPage(AssessmentQueryDTO query);

    /**
     * 根据ID获取评定方案详情
     */
    AssessmentVO getAssessmentDetail(Integer id);

    /**
     * 结束评定方案
     */
    void endAssessment(Integer id);

    /**
     * 执行评定方案
     */
    void executeAssessment(Integer id);
}
