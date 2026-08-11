package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.PrescriptionQueryDTO;
import com.rehabilitation.dto.PrescriptionVO;
import com.rehabilitation.dto.PrescriptionDetailVO;
import com.rehabilitation.dto.PrescriptionExerciseVO;
import com.rehabilitation.entity.PatientPrescribedExercise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 根据处方ID查询处方详情（包含患者信息）
     *
     * @param prescriptionId 处方ID
     * @return 处方详情
     */
    PrescriptionDetailVO selectPrescriptionDetailById(@Param("prescriptionId") Integer prescriptionId);

    /**
     * 根据处方ID查询运动项目列表
     *
     * @param prescriptionId 处方ID
     * @return 运动项目列表
     */
    List<PrescriptionExerciseVO> selectExerciseListByPrescriptionId(@Param("prescriptionId") Integer prescriptionId);

    /**
     * 根据处方ID删除运动项目
     *
     * @param prescriptionId 处方ID
     */
    void deleteExercisesByPrescriptionId(@Param("prescriptionId") Integer prescriptionId);

    /**
     * 根据处方ID批量更新运动项目的执行机构和状态
     *
     * @param prescriptionId 处方ID
     * @param executionInstitution 执行机构
     * @param prescriptionStatus 处方状态
     */
    void updateExercisesInstitutionAndStatus(@Param("prescriptionId") Integer prescriptionId,
                                            @Param("executionInstitution") String executionInstitution,
                                            @Param("prescriptionStatus") Integer prescriptionStatus);
}
