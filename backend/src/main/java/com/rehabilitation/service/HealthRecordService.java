package com.rehabilitation.service;

import com.rehabilitation.entity.HealthRecord;

/**
 * 健康档案服务接口
 */
public interface HealthRecordService {

    /**
     * 保存或更新健康档案
     * @param healthRecord 健康档案信息
     * @return 保存后的健康档案
     */
    HealthRecord saveOrUpdate(HealthRecord healthRecord);

    /**
     * 根据患者ID查询健康档案
     * @param patientId 患者ID
     * @return 健康档案
     */
    HealthRecord getByPatientId(Integer patientId);

    /**
     * 根据患者ID删除健康档案
     * @param patientId 患者ID
     * @return 是否删除成功
     */
    boolean deleteByPatientId(Integer patientId);
}
