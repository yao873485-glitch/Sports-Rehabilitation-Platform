package com.rehabilitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.dto.PatientSchemeInfoQueryDTO;
import com.rehabilitation.dto.PatientSchemeInfoVO;
import com.rehabilitation.dto.PrescriptionListDTO;

import java.util.List;

/**
 * 患者方案信息Service接口
 */
public interface PatientSchemeInfoService {

    /**
     * 分页查询患者方案信息列表
     */
    IPage<PatientSchemeInfoVO> getPatientSchemeInfoPage(PatientSchemeInfoQueryDTO query);

    /**
     * 获取患者方案信息详情
     */
    PatientSchemeInfoVO getPatientSchemeInfoDetail(Long id);

    /**
     * 获取患者处方清单
     */
    List<PrescriptionListDTO> getPatientPrescriptionList(String schemeId);
}
