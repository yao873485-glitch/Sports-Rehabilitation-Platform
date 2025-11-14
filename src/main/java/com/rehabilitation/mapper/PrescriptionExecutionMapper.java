package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.PrescriptionExecutionQueryDTO;
import com.rehabilitation.dto.PrescriptionExecutionVO;
import com.rehabilitation.entity.PatientPrescribedExercise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 处方执行Mapper接口
 */
@Mapper
public interface PrescriptionExecutionMapper extends BaseMapper<PatientPrescribedExercise> {

    /**
     * 分页查询处方执行列表
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<PrescriptionExecutionVO> selectPrescriptionExecutionPage(
            Page<PrescriptionExecutionVO> page,
            @Param("query") PrescriptionExecutionQueryDTO query);

    /**
     * 根据ID查询处方执行详情
     *
     * @param id 主键ID
     * @return 处方执行详情
     */
    PrescriptionExecutionVO selectPrescriptionExecutionById(@Param("id") Integer id);
}