package com.rehabilitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.dto.ExercisePrescriptionBatchOperationDTO;
import com.rehabilitation.dto.ExercisePrescriptionQueryDTO;
import com.rehabilitation.dto.ExercisePrescriptionVO;
import com.rehabilitation.entity.ExercisePrescription;

import java.util.List;

/**
 * 运动处方信息服务接口
 */
public interface ExercisePrescriptionService extends IService<ExercisePrescription> {

    /**
     * 分页查询运动处方列表
     */
    IPage<ExercisePrescriptionVO> getExercisePrescriptionPage(ExercisePrescriptionQueryDTO query);

    /**
     * 获取运动处方详情
     */
    ExercisePrescriptionVO getExercisePrescriptionById(Long id);

    /**
     * 新增运动处方
     */
    void addExercisePrescription(ExercisePrescription exercisePrescription);

    /**
     * 更新运动处方
     */
    void updateExercisePrescription(ExercisePrescription exercisePrescription);

    /**
     * 获取所有病种类型
     */
    List<String> getAllDiseaseTypes();

    /**
     * 批量操作运动处方
     */
    void batchOperation(ExercisePrescriptionBatchOperationDTO batchOperation);

    /**
     * 导入运动处方数据
     */
    void importExercisePrescription(List<ExercisePrescription> exercisePrescriptionList);
}