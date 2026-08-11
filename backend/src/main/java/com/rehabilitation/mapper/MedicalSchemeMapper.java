package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.MedicalSchemeQueryDTO;
import com.rehabilitation.dto.MedicalSchemeVO;
import com.rehabilitation.entity.MedicalScheme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 医疗方案Mapper接口
 */
@Mapper
public interface MedicalSchemeMapper extends BaseMapper<MedicalScheme> {

    /**
     * 分页查询医疗方案列表（返回VO，已包含患者信息）
     */
    IPage<MedicalSchemeVO> selectMedicalSchemePage(Page<MedicalSchemeVO> page, @Param("query") MedicalSchemeQueryDTO query);

    /**
     * 统计患者执行中的方案数量（通过档案号）
     */
    Integer countActiveSchemeByMedicalRecordNo(@Param("medicalRecordNo") String medicalRecordNo);
}