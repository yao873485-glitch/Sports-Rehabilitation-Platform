package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.PatientCreateDTO;
import com.rehabilitation.entity.PatientDetail;
import com.rehabilitation.entity.PatientInfo;
import com.rehabilitation.mapper.PatientDetailMapper;
import com.rehabilitation.mapper.PatientInfoMapper;
import com.rehabilitation.service.PatientInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 患者信息服务实现类
 */
@Service
@RequiredArgsConstructor
public class PatientInfoServiceImpl implements PatientInfoService {

    private final PatientInfoMapper patientInfoMapper;
    private final PatientDetailMapper patientDetailMapper;

    @Override
    public IPage<PatientInfo> getPatientInfoPage(Integer pageNum, Integer pageSize, String name, LocalDateTime startTime, LocalDateTime endTime) {
        Page<PatientInfo> page = new Page<>(pageNum, pageSize);
        return patientInfoMapper.selectPatientInfoPage(page, name, null, startTime, endTime);
    }

    @Override
    public PatientInfo getPatientInfoById(Integer id) {
        return patientInfoMapper.selectById(id);
    }

    @Override
    public PatientDetail getPatientDetailByBasicId(Integer basicId) {
        return patientDetailMapper.selectByPatientId(basicId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPatientInfo(PatientInfo patientInfo) {
        // 自动生成唯一档案号
        if (patientInfo.getMedicalRecordNo() == null || patientInfo.getMedicalRecordNo().trim().isEmpty()) {
            patientInfo.setMedicalRecordNo(generateUniqueRecordNumber());
        } else {
            // 如果用户手动输入了档案号，检查是否已存在
            if (isRecordNumberExists(patientInfo.getMedicalRecordNo())) {
                throw new RuntimeException("档案号已存在：" + patientInfo.getMedicalRecordNo());
            }
        }
        patientInfo.setCreatedTime(LocalDateTime.now());
        patientInfo.setUpdatedTime(LocalDateTime.now());
        return patientInfoMapper.insert(patientInfo) > 0;
    }

    /**
     * 检查档案号是否已存在
     */
    private boolean isRecordNumberExists(String recordNumber) {
        QueryWrapper<PatientInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("medical_record_no", recordNumber);
        Long count = patientInfoMapper.selectCount(queryWrapper);
        return count > 0;
    }

    /**
     * 生成唯一的档案号
     * 格式：PT + 年月日时分秒 + 4位随机数
     * 例如：PT202410111645321234
     */
    private String generateUniqueRecordNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        return "PT" + timestamp + String.format("%04d", randomNum);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePatientInfo(PatientInfo patientInfo) {
        if (patientInfo.getId() == null) {
            throw new IllegalArgumentException("更新患者信息时ID不能为空");
        }
        patientInfo.setUpdatedTime(LocalDateTime.now());
        return patientInfoMapper.updateById(patientInfo) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdatePatientDetail(PatientDetail patientDetail) {
        PatientDetail existingDetail = patientDetailMapper.selectByPatientId(patientDetail.getPatientId());
        if (existingDetail != null) {
            patientDetail.setId(existingDetail.getId());
            return patientDetailMapper.updateById(patientDetail) > 0;
        } else {
            return patientDetailMapper.insert(patientDetail) > 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePatientInfo(Integer id) {
        // 先删除详细信息
        PatientDetail detail = patientDetailMapper.selectByPatientId(id);
        if (detail != null) {
            patientDetailMapper.deleteById(detail.getId());
        }
        // 再删除基本信息
        return patientInfoMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PatientInfo createPatient(PatientCreateDTO patientCreateDTO) {
        // 1. 创建患者基本信息
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setName(patientCreateDTO.getName());
        patientInfo.setGender(patientCreateDTO.getGender());
        patientInfo.setBirthDate(patientCreateDTO.getBirthDate());
        patientInfo.setIdCard(patientCreateDTO.getIdCard());
        patientInfo.setPhone(patientCreateDTO.getPhone());
        patientInfo.setDiseaseType(patientCreateDTO.getDiseaseType());
        patientInfo.setEnrollmentInstitution(patientCreateDTO.getEnrollmentInstitution());
        patientInfo.setAddress(patientCreateDTO.getAddress());

        // 自动生成唯一档案号
        patientInfo.setMedicalRecordNo(generateUniqueRecordNumber());
        patientInfo.setCreatedTime(LocalDateTime.now());
        patientInfo.setUpdatedTime(LocalDateTime.now());

        // 保存患者基本信息
        int insertResult = patientInfoMapper.insert(patientInfo);
        if (insertResult <= 0) {
            throw new RuntimeException("保存患者基本信息失败");
        }

        // 2. 创建患者详细信息
        PatientDetail patientDetail = new PatientDetail();
        patientDetail.setPatientId(patientInfo.getId());

        // 设置详细信息字段
        patientDetail.setNickname(patientCreateDTO.getNickname());
        patientDetail.setIdCardType(patientCreateDTO.getIdCardType());
        patientDetail.setHeightCm(patientCreateDTO.getHeight());
        patientDetail.setEthnicity(patientCreateDTO.getEthnicity());
        patientDetail.setOccupation(patientCreateDTO.getOccupation());

        // 设置联系地址（省市区）
        if (patientCreateDTO.getContactAddress() != null && !patientCreateDTO.getContactAddress().trim().isEmpty()) {
            patientDetail.setContactProvinceCityDistrict(patientCreateDTO.getContactAddress());
        }

        // 组装备注信息
        StringBuilder remark = new StringBuilder();
        if (patientCreateDTO.getDetailAddress() != null && !patientCreateDTO.getDetailAddress().trim().isEmpty()) {
            remark.append("详细地址：").append(patientCreateDTO.getDetailAddress()).append("; ");
        }
        if (patientCreateDTO.getDiagnosis() != null && !patientCreateDTO.getDiagnosis().trim().isEmpty()) {
            remark.append("诊断：").append(patientCreateDTO.getDiagnosis()).append("; ");
        }
        if (patientCreateDTO.getRemarks() != null && !patientCreateDTO.getRemarks().trim().isEmpty()) {
            remark.append(patientCreateDTO.getRemarks());
        }
        if (remark.length() > 0) {
            patientDetail.setRemark(remark.toString());
        }

        patientDetailMapper.insert(patientDetail);

        return patientInfo;
    }
}