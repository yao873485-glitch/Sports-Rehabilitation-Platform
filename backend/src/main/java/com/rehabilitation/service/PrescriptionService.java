package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.PrescriptionQueryDTO;
import com.rehabilitation.dto.PrescriptionVO;
import com.rehabilitation.dto.PrescriptionDetailVO;
import com.rehabilitation.dto.PrescriptionSaveDTO;
import com.rehabilitation.entity.PatientPrescribedExercise;

/**
 * 运动处方服务接口
 */
public interface PrescriptionService extends IService<PatientPrescribedExercise> {

    /**
     * 分页查询运动处方列表
     */
    PageResult<PrescriptionVO> getPrescriptionPage(PrescriptionQueryDTO query);

    /**
     * 根据ID获取运动处方详情
     */
    PrescriptionVO getPrescriptionDetail(Integer id);

    /**
     * 根据处方ID获取处方详情（包含患者信息和运动项目列表）
     * @param prescriptionId 处方ID
     * @return 处方详情
     */
    PrescriptionDetailVO getPrescriptionDetailWithExercises(Integer prescriptionId);

    /**
     * 结束运动处方
     */
    void endPrescription(Integer id);

    /**
     * 执行运动处方
     */
    void executePrescription(Integer id);

    /**
     * 保存运动处方
     */
    void savePrescription(PrescriptionSaveDTO dto);

    /**
     * 提交运动处方
     */
    void submitPrescription(PrescriptionSaveDTO dto);
}
