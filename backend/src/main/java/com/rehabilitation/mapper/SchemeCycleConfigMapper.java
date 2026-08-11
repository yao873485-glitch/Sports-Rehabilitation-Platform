package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rehabilitation.entity.SchemeCycleConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 方案周期配置 Mapper 接口
 */
@Mapper
public interface SchemeCycleConfigMapper extends BaseMapper<SchemeCycleConfig> {

    /**
     * 根据患者ID查询方案周期配置
     * @param patientId 患者ID
     * @return 方案周期配置
     */
    SchemeCycleConfig selectByPatientId(@Param("patientId") Integer patientId);

    /**
     * 根据患者ID和方案ID查询方案周期配置
     * @param patientId 患者ID
     * @param schemeId 方案ID
     * @return 方案周期配置
     */
    SchemeCycleConfig selectByPatientIdAndSchemeId(@Param("patientId") Integer patientId,
                                                     @Param("schemeId") String schemeId);

    /**
     * 根据患者ID删除方案周期配置
     * @param patientId 患者ID
     * @return 删除行数
     */
    int deleteByPatientId(@Param("patientId") Integer patientId);

    /**
     * 根据方案ID查询方案周期配置
     * @param schemeId 方案ID
     * @return 方案周期配置
     */
    SchemeCycleConfig selectBySchemeId(@Param("schemeId") String schemeId);
}
