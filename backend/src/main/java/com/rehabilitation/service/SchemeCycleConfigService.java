package com.rehabilitation.service;

import com.rehabilitation.entity.SchemeCycleConfig;

/**
 * 方案周期配置服务接口
 */
public interface SchemeCycleConfigService {

    /**
     * 保存或更新方案周期配置
     * @param config 方案周期配置信息
     * @return 保存后的配置
     */
    SchemeCycleConfig saveOrUpdate(SchemeCycleConfig config);

    /**
     * 根据患者ID查询方案周期配置
     * @param patientId 患者ID
     * @return 方案周期配置
     */
    SchemeCycleConfig getByPatientId(Integer patientId);

    /**
     * 根据患者ID和方案ID查询方案周期配置
     * @param patientId 患者ID
     * @param schemeId 方案ID
     * @return 方案周期配置
     */
    SchemeCycleConfig getByPatientIdAndSchemeId(Integer patientId, String schemeId);

    /**
     * 根据方案ID查询方案周期配置
     * @param schemeId 方案ID
     * @return 方案周期配置
     */
    SchemeCycleConfig getBySchemeId(String schemeId);

    /**
     * 根据患者ID删除方案周期配置
     * @param patientId 患者ID
     * @return 是否删除成功
     */
    boolean deleteByPatientId(Integer patientId);
}
