package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rehabilitation.entity.DiseaseRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 专病档案Mapper接口
 */
@Mapper
public interface DiseaseRecordMapper extends BaseMapper<DiseaseRecord> {

    /**
     * 根据患者ID查询专病档案
     */
    DiseaseRecord selectByPatientId(@Param("patientId") Integer patientId);

    /**
     * 根据患者ID删除专病档案
     */
    int deleteByPatientId(@Param("patientId") Integer patientId);
}
