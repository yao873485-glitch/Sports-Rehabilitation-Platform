package com.rehabilitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.dto.MedicalSchemeQueryDTO;
import com.rehabilitation.dto.MedicalSchemeVO;
import com.rehabilitation.dto.SchemeCreateDTO;

/**
 * 医疗方案Service接口
 */
public interface MedicalSchemeService {

    /**
     * 分页查询医疗方案列表
     */
    IPage<MedicalSchemeVO> getMedicalSchemePage(MedicalSchemeQueryDTO query);

    /**
     * 获取医疗方案详情
     */
    MedicalSchemeVO getMedicalSchemeDetail(Long id);

    /**
     * 结束医疗方案
     */
    boolean endMedicalScheme(Long id, String reason);

    /**
     * 检查患者是否已有执行中的方案（通过档案号）
     */
    boolean checkPatientHasActiveScheme(String medicalRecordNo);

    /**
     * 创建方案
     */
    String createScheme(SchemeCreateDTO schemeCreateDTO);
}