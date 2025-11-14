package com.rehabilitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.dto.PatientCreateDTO;
import com.rehabilitation.entity.PatientDetail;
import com.rehabilitation.entity.PatientInfo;

import java.time.LocalDateTime;

/**
 * 患者信息服务接口
 */
public interface PatientInfoService {

    /**
     * 分页查询患者信息
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param name 姓名/电话
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    IPage<PatientInfo> getPatientInfoPage(Integer pageNum, Integer pageSize, String name, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据ID查询患者基本信息
     * @param id 患者ID
     * @return 患者信息
     */
    PatientInfo getPatientInfoById(Integer id);

    /**
     * 根据基础信息ID查询详细信息
     * @param basicId 基础信息ID
     * @return 详细信息
     */
    PatientDetail getPatientDetailByBasicId(Integer basicId);

    /**
     * 新增患者基本信息
     * @param patientInfo 患者信息
     * @return 是否成功
     */
    boolean addPatientInfo(PatientInfo patientInfo);

    /**
     * 更新患者基本信息
     * @param patientInfo 患者信息
     * @return 是否成功
     */
    boolean updatePatientInfo(PatientInfo patientInfo);

    /**
     * 新增或更新患者详细信息
     * @param patientDetail 详细信息
     * @return 是否成功
     */
    boolean saveOrUpdatePatientDetail(PatientDetail patientDetail);

    /**
     * 删除患者信息
     * @param id 患者ID
     * @return 是否成功
     */
    boolean deletePatientInfo(Integer id);

    /**
     * 创建患者信息（包含基本信息和详细信息）
     * @param patientCreateDTO 患者创建DTO
     * @return 创建的患者信息
     */
    PatientInfo createPatient(PatientCreateDTO patientCreateDTO);
}