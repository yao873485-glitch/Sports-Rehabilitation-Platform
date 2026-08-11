package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rehabilitation.entity.HealthRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 健康档案 Mapper 接口
 */
@Mapper
public interface HealthRecordMapper extends BaseMapper<HealthRecord> {

    /**
     * 根据患者ID查询健康档案
     * @param patientId 患者ID
     * @return 健康档案
     */
    HealthRecord selectByPatientId(@Param("patientId") Integer patientId);

    /**
     * 根据患者ID删除健康档案
     * @param patientId 患者ID
     * @return 删除行数
     */
    int deleteByPatientId(@Param("patientId") Integer patientId);
}
