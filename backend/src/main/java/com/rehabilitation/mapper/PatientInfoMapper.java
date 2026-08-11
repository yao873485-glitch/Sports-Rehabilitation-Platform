package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.entity.PatientInfo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 患者基本信息Mapper
 */
public interface PatientInfoMapper extends BaseMapper<PatientInfo> {

    /**
     * 分页查询患者信息
     * @param page 分页参数
     * @param name 姓名
     * @param phone 联系电话
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    IPage<PatientInfo> selectPatientInfoPage(
            Page<PatientInfo> page,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}