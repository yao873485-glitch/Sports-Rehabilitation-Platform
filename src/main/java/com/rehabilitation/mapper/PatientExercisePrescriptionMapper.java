package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.PatientExercisePrescriptionQueryDTO;
import com.rehabilitation.dto.PatientExercisePrescriptionVO;
import com.rehabilitation.entity.PatientExercisePrescription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 患者运动处方Mapper接口
 */
@Mapper
public interface PatientExercisePrescriptionMapper extends BaseMapper<PatientExercisePrescription> {

    /**
     * 分页查询患者运动处方列表
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<PatientExercisePrescriptionVO> selectPatientExercisePrescriptionPage(
            Page<PatientExercisePrescriptionVO> page,
            @Param("query") PatientExercisePrescriptionQueryDTO query);

    /**
     * 根据ID查询患者运动处方详情
     *
     * @param id 主键ID
     * @return 患者运动处方详情
     */
    PatientExercisePrescriptionVO selectPatientExercisePrescriptionById(@Param("id") Long id);
}