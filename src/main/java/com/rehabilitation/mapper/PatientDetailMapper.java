package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rehabilitation.entity.PatientDetail;

/**
 * 患者详细信息Mapper
 */
public interface PatientDetailMapper extends BaseMapper<PatientDetail> {

    /**
     * 根据患者ID查询详细信息
     * @param patientId 患者ID
     * @return 详细信息
     */
    PatientDetail selectByPatientId(Integer patientId);
}