package com.rehabilitation.service;

import com.rehabilitation.entity.DiseaseRecord;

/**
 * 专病档案服务接口
 */
public interface DiseaseRecordService {

    /**
     * 保存或更新专病档案
     */
    DiseaseRecord saveOrUpdate(DiseaseRecord diseaseRecord);

    /**
     * 根据患者ID查询专病档案
     */
    DiseaseRecord getByPatientId(Integer patientId);

    /**
     * 根据患者ID删除专病档案
     */
    boolean deleteByPatientId(Integer patientId);
}
