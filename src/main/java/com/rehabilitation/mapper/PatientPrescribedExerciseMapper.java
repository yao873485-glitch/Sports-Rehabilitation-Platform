package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.PrescriptionQueryDTO;
import com.rehabilitation.dto.PrescriptionVO;
import com.rehabilitation.entity.PatientPrescribedExercise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 患者运动处方Mapper
 */
@Mapper
public interface PatientPrescribedExerciseMapper extends BaseMapper<PatientPrescribedExercise> {

    /**
     * 分页查询运动处方列表
     */
    IPage<PrescriptionVO> selectPrescriptionPage(Page<PrescriptionVO> page, 
                                               @Param("query") PrescriptionQueryDTO query);
}
