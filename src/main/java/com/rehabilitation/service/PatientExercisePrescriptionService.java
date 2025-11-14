package com.rehabilitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.dto.PatientExercisePrescriptionQueryDTO;
import com.rehabilitation.dto.PatientExercisePrescriptionVO;
import com.rehabilitation.dto.PrescriptionSaveDTO;
import com.rehabilitation.entity.PatientExercisePrescription;

import java.util.List;

/**
 * 患者运动处方Service接口
 */
public interface PatientExercisePrescriptionService {

    /**
     * 分页查询患者运动处方列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<PatientExercisePrescriptionVO> getPatientExercisePrescriptionPage(PatientExercisePrescriptionQueryDTO query);

    /**
     * 根据ID查询患者运动处方详情
     *
     * @param id 主键ID
     * @return 患者运动处方详情
     */
    PatientExercisePrescriptionVO getPatientExercisePrescriptionById(Long id);

    /**
     * 新增患者运动处方
     *
     * @param patientExercisePrescription 患者运动处方
     */
    void addPatientExercisePrescription(PatientExercisePrescription patientExercisePrescription);

    /**
     * 更新患者运动处方
     *
     * @param patientExercisePrescription 患者运动处方
     */
    void updatePatientExercisePrescription(PatientExercisePrescription patientExercisePrescription);

    /**
     * 结束患者运动处方
     *
     * @param id 主键ID
     */
    void endPatientExercisePrescription(Long id);

    /**
     * 执行患者运动处方
     *
     * @param id 主键ID
     */
    void executePatientExercisePrescription(Long id);

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

    /**
     * 保存运动处方（从方案配置页面）
     *
     * @param prescriptionSaveDTO 运动处方保存DTO
     * @return 处方ID
     */
    Long savePrescription(PrescriptionSaveDTO prescriptionSaveDTO);
}