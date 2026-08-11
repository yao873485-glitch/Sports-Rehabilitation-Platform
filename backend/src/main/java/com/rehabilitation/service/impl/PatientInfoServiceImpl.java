package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.PatientCreateDTO;
import com.rehabilitation.entity.PatientDetail;
import com.rehabilitation.entity.PatientInfo;
import com.rehabilitation.mapper.PatientDetailMapper;
import com.rehabilitation.mapper.PatientInfoMapper;
import com.rehabilitation.service.PatientEnrollmentAuditService;
import com.rehabilitation.service.PatientInfoService;
import com.rehabilitation.service.support.DistributedLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class PatientInfoServiceImpl implements PatientInfoService {

    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private PatientDetailMapper patientDetailMapper;
    @Autowired
    private PatientEnrollmentAuditService patientEnrollmentAuditService;
    @Autowired
    private DistributedLockService distributedLockService;

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
        return distributedLockService.executeWithLock("lock:patient:create", 3, 10, () -> {
            if (patientInfo.getMedicalRecordNo() == null || patientInfo.getMedicalRecordNo().trim().isEmpty()) {
                patientInfo.setMedicalRecordNo(generateUniqueRecordNumber());
            } else if (isRecordNumberExists(patientInfo.getMedicalRecordNo())) {
                throw new RuntimeException("档案号已存在: " + patientInfo.getMedicalRecordNo());
            }

            patientInfo.setCreatedTime(LocalDateTime.now());
            patientInfo.setUpdatedTime(LocalDateTime.now());
            boolean inserted = patientInfoMapper.insert(patientInfo) > 0;
            if (!inserted) {
                return false;
            }
            boolean auditSaved = patientEnrollmentAuditService.saveOrUpdateStatus(patientInfo.getId(), 0, null);
            if (!auditSaved) {
                throw new RuntimeException("Failed to save enrollment audit record");
            }
            return true;
        });
    }

    private boolean isRecordNumberExists(String recordNumber) {
        QueryWrapper<PatientInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("medical_record_no", recordNumber);
        Long count = patientInfoMapper.selectCount(queryWrapper);
        return count > 0;
    }

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
        boolean saved;
        if (existingDetail != null) {
            patientDetail.setId(existingDetail.getId());
            saved = patientDetailMapper.updateById(patientDetail) > 0;
        } else {
            saved = patientDetailMapper.insert(patientDetail) > 0;
        }

        if (!saved) {
            return false;
        }

        StringBuilder fullAddress = new StringBuilder();
        if (patientDetail.getContactProvinceCityDistrict() != null
                && !patientDetail.getContactProvinceCityDistrict().trim().isEmpty()) {
            fullAddress.append(patientDetail.getContactProvinceCityDistrict());
        }
        if (patientDetail.getDetailAddress() != null
                && !patientDetail.getDetailAddress().trim().isEmpty()) {
            fullAddress.append(patientDetail.getDetailAddress());
        }

        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setId(patientDetail.getPatientId());
        patientInfo.setAddress(fullAddress.length() > 0 ? fullAddress.toString() : null);
        patientInfo.setUpdatedTime(LocalDateTime.now());
        patientInfoMapper.updateById(patientInfo);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePatientInfo(Integer id) {
        PatientDetail detail = patientDetailMapper.selectByPatientId(id);
        if (detail != null) {
            patientDetailMapper.deleteById(detail.getId());
        }
        return patientInfoMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PatientInfo createPatient(PatientCreateDTO patientCreateDTO) {
        return distributedLockService.executeWithLock("lock:patient:create", 3, 10, () -> {
            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setName(patientCreateDTO.getName());
            patientInfo.setGender(patientCreateDTO.getGender());
            patientInfo.setBirthDate(patientCreateDTO.getBirthDate());
            patientInfo.setIdCard(patientCreateDTO.getIdCard());
            patientInfo.setPhone(patientCreateDTO.getPhone());
            patientInfo.setDiseaseType(patientCreateDTO.getDiseaseType());
            patientInfo.setEnrollmentInstitution(patientCreateDTO.getEnrollmentInstitution());

            StringBuilder fullAddress = new StringBuilder();
            if (patientCreateDTO.getContactAddress() != null && !patientCreateDTO.getContactAddress().trim().isEmpty()) {
                fullAddress.append(patientCreateDTO.getContactAddress());
            }
            if (patientCreateDTO.getDetailAddress() != null && !patientCreateDTO.getDetailAddress().trim().isEmpty()) {
                fullAddress.append(patientCreateDTO.getDetailAddress());
            }
            if (fullAddress.length() > 0) {
                patientInfo.setAddress(fullAddress.toString());
            }

            patientInfo.setMedicalRecordNo(generateUniqueRecordNumber());
            patientInfo.setCreatedTime(LocalDateTime.now());
            patientInfo.setUpdatedTime(LocalDateTime.now());

            int insertResult = patientInfoMapper.insert(patientInfo);
            if (insertResult <= 0) {
                throw new RuntimeException("保存患者基本信息失败");
            }

            boolean auditSaved = patientEnrollmentAuditService.saveOrUpdateStatus(patientInfo.getId(), 0, null);
            if (!auditSaved) {
                throw new RuntimeException("Failed to save enrollment audit record");
            }

            PatientDetail patientDetail = new PatientDetail();
            patientDetail.setPatientId(patientInfo.getId());
            patientDetail.setNickname(patientCreateDTO.getNickname());
            patientDetail.setIdCardType(patientCreateDTO.getIdCardType());
            patientDetail.setHeightCm(patientCreateDTO.getHeight());
            patientDetail.setEthnicity(patientCreateDTO.getEthnicity());
            patientDetail.setOccupation(patientCreateDTO.getOccupation());

            if (patientCreateDTO.getContactAddress() != null && !patientCreateDTO.getContactAddress().trim().isEmpty()) {
                patientDetail.setContactProvinceCityDistrict(patientCreateDTO.getContactAddress());
            }
            if (patientCreateDTO.getDetailAddress() != null && !patientCreateDTO.getDetailAddress().trim().isEmpty()) {
                patientDetail.setDetailAddress(patientCreateDTO.getDetailAddress());
            }
            if (patientCreateDTO.getDiagnosis() != null && !patientCreateDTO.getDiagnosis().trim().isEmpty()) {
                patientDetail.setDiagnosis(patientCreateDTO.getDiagnosis());
            }
            if (patientCreateDTO.getRemarks() != null && !patientCreateDTO.getRemarks().trim().isEmpty()) {
                patientDetail.setRemark(patientCreateDTO.getRemarks());
            }

            patientDetailMapper.insert(patientDetail);
            return patientInfo;
        });
    }
}
