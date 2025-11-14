package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.MedicalSchemeQueryDTO;
import com.rehabilitation.dto.MedicalSchemeVO;
import com.rehabilitation.dto.SchemeCreateDTO;
import com.rehabilitation.entity.MedicalScheme;
import com.rehabilitation.entity.PatientInfo;
import com.rehabilitation.entity.PatientSchemeInfo;
import com.rehabilitation.mapper.MedicalSchemeMapper;
import com.rehabilitation.mapper.PatientInfoMapper;
import com.rehabilitation.mapper.PatientSchemeInfoMapper;
import com.rehabilitation.service.MedicalSchemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 医疗方案Service实现类
 */
@Service
@RequiredArgsConstructor
public class MedicalSchemeServiceImpl implements MedicalSchemeService {

    private final MedicalSchemeMapper medicalSchemeMapper;
    private final PatientSchemeInfoMapper patientSchemeInfoMapper;
    private final PatientInfoMapper patientInfoMapper;

    @Override
    public IPage<MedicalSchemeVO> getMedicalSchemePage(MedicalSchemeQueryDTO query) {
        Page<MedicalScheme> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<MedicalScheme> medicalSchemePage = medicalSchemeMapper.selectMedicalSchemePage(page, query);

        // 转换为VO
        IPage<MedicalSchemeVO> voPage = new Page<>(medicalSchemePage.getCurrent(), medicalSchemePage.getSize(), medicalSchemePage.getTotal());
        List<MedicalSchemeVO> voList = medicalSchemePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public MedicalSchemeVO getMedicalSchemeDetail(Long id) {
        MedicalScheme medicalScheme = medicalSchemeMapper.selectById(id);
        return convertToVO(medicalScheme);
    }

    @Override
    public boolean endMedicalScheme(Long id) {
        MedicalScheme medicalScheme = new MedicalScheme();
        medicalScheme.setId(id);
        medicalScheme.setStatus(2); // 设置为已结束
        medicalScheme.setEndTime(LocalDateTime.now());
        return medicalSchemeMapper.updateById(medicalScheme) > 0;
    }

    @Override
    public boolean checkPatientHasActiveScheme(String medicalRecordNo) {
        Integer count = medicalSchemeMapper.countActiveSchemeByMedicalRecordNo(medicalRecordNo);
        return count != null && count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createScheme(SchemeCreateDTO schemeCreateDTO) {
        // 1. 获取患者信息
        PatientInfo patientInfo = patientInfoMapper.selectById(schemeCreateDTO.getPatientId());
        if (patientInfo == null) {
            throw new RuntimeException("患者信息不存在");
        }

        // 2. 生成唯一的方案编号
        String schemeId = generateUniqueSchemeId();

        // 3. 计算结束时间（方案周期转换为月，加到当前时间上）
        LocalDateTime createTime = LocalDateTime.now();
        LocalDateTime endTime = createTime.plusMonths(schemeCreateDTO.getSchemeCycle());
        LocalDate createDate = createTime.toLocalDate();

        // 4. 创建患者方案信息（patient_scheme_info表 - 精简版）
        PatientSchemeInfo patientSchemeInfo = new PatientSchemeInfo();
        patientSchemeInfo.setPatientId(schemeCreateDTO.getPatientId());
        patientSchemeInfo.setSchemeId(schemeId);
        patientSchemeInfo.setStatus(1); // 1-执行中
        patientSchemeInfo.setCreateTime(createTime);
        patientSchemeInfo.setEndTime(endTime);

        // 5. 保存到 patient_scheme_info 表
        int insertResult1 = patientSchemeInfoMapper.insert(patientSchemeInfo);
        if (insertResult1 <= 0) {
            throw new RuntimeException("创建患者方案信息失败");
        }

        // 6. 创建医疗方案信息（medical_scheme表 - 精简版）
        MedicalScheme medicalScheme = new MedicalScheme();
        medicalScheme.setPatientId(schemeCreateDTO.getPatientId());
        medicalScheme.setSchemeId(schemeId);
        medicalScheme.setDoctorName("系统"); // TODO: 后续从登录用户信息中获取医生姓名
        medicalScheme.setStatus(1); // 1-执行中
        medicalScheme.setCreateTime(createTime);
        medicalScheme.setEndTime(endTime);
        medicalScheme.setCreateDate(createDate);

        // 7. 保存到 medical_scheme 表
        int insertResult2 = medicalSchemeMapper.insert(medicalScheme);
        if (insertResult2 <= 0) {
            throw new RuntimeException("创建医疗方案失败");
        }

        return schemeId;
    }

    /**
     * 生成唯一的方案编号
     * 格式：SC + 年月日时分秒 + 4位随机数
     * 例如：SC202410241830121234
     */
    private String generateUniqueSchemeId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        return "SC" + timestamp + String.format("%04d", randomNum);
    }

    /**
     * 实体转VO
     */
    private MedicalSchemeVO convertToVO(MedicalScheme medicalScheme) {
        if (medicalScheme == null) {
            return null;
        }

        MedicalSchemeVO vo = new MedicalSchemeVO();
        BeanUtils.copyProperties(medicalScheme, vo);

        // 从patient表获取详细信息
        PatientInfo patientInfo = patientInfoMapper.selectById(medicalScheme.getPatientId());
        if (patientInfo != null) {
            vo.setPatientName(patientInfo.getName());
            vo.setGenderDesc(patientInfo.getGender());
            vo.setArchiveNo(patientInfo.getMedicalRecordNo());
            vo.setDiseaseType(patientInfo.getDiseaseType());

            // 计算年龄
            if (patientInfo.getBirthDate() != null) {
                vo.setAge(LocalDate.now().getYear() - patientInfo.getBirthDate().getYear());
            }

            // 设置性别字段（1-男，2-女）
            vo.setGender("男".equals(patientInfo.getGender()) ? 1 : 2);
        }

        // 设置状态描述
        vo.setStatusDesc(medicalScheme.getStatus() == 1 ? "执行中" : "已结束");

        return vo;
    }
}