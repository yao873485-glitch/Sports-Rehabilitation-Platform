package com.rehabilitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.dto.AssessmentExecutionQueryDTO;
import com.rehabilitation.dto.AssessmentExecutionVO;

import java.util.List;

/**
 * 评定执行Service接口
 */
public interface AssessmentExecutionService {

    /**
     * 分页查询评定执行列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<AssessmentExecutionVO> getAssessmentExecutionPage(AssessmentExecutionQueryDTO query);

    /**
     * 根据ID查询评定执行详情
     *
     * @param id 主键ID
     * @return 评定执行详情
     */
    AssessmentExecutionVO getAssessmentExecutionById(Integer id);

    /**
     * 结束评定执行
     *
     * @param id 主键ID
     */
    void endAssessmentExecution(Integer id);

    /**
     * 执行评定
     *
     * @param id 主键ID
     */
    void executeAssessment(Integer id);

    /**
     * 获取所有病种类型
     *
     * @return 病种类型列表
     */
    List<String> getAllDiseaseTypes();

    /**
     * 获取所有机构列表
     *
     * @return 机构列表
     */
    List<String> getAllInstitutions();
}