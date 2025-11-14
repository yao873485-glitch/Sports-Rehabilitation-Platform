package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.PatientSchemeInfoQueryDTO;
import com.rehabilitation.entity.PatientSchemeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 患者方案信息Mapper接口
 */
@Mapper
public interface PatientSchemeInfoMapper extends BaseMapper<PatientSchemeInfo> {

    /**
     * 分页查询患者方案信息列表
     */
    IPage<PatientSchemeInfo> selectPatientSchemeInfoPage(Page<PatientSchemeInfo> page, @Param("query") PatientSchemeInfoQueryDTO query);
}
