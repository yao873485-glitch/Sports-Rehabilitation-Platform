package com.rehabilitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.dto.PrescriptionExecutionQueryDTO;
import com.rehabilitation.dto.PrescriptionExecutionVO;

import java.util.List;

/**
 * 处方执行Service接口
 */
public interface PrescriptionExecutionService {

    /**
     * 分页查询处方执行列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<PrescriptionExecutionVO> getPrescriptionExecutionPage(PrescriptionExecutionQueryDTO query);

    /**
     * 根据ID查询处方执行详情
     *
     * @param id 主键ID
     * @return 处方执行详情
     */
    PrescriptionExecutionVO getPrescriptionExecutionById(Integer id);

    /**
     * 结束处方执行
     *
     * @param id 主键ID
     */
    void endPrescriptionExecution(Integer id);

    /**
     * 执行处方
     *
     * @param id 主键ID
     */
    void executePrescription(Integer id);

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