package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.PatientSchemeInfoQueryDTO;
import com.rehabilitation.dto.PatientSchemeInfoVO;
import com.rehabilitation.dto.PrescriptionListDTO;
import com.rehabilitation.entity.PatientInfo;
import com.rehabilitation.entity.PatientSchemeInfo;
import com.rehabilitation.mapper.PatientInfoMapper;
import com.rehabilitation.mapper.PatientSchemeInfoMapper;
import com.rehabilitation.service.PatientSchemeInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 患者方案信息Service实现类
 */
@Service
public class PatientSchemeInfoServiceImpl implements PatientSchemeInfoService {

    @Autowired
    private PatientSchemeInfoMapper patientSchemeInfoMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Override
    public IPage<PatientSchemeInfoVO> getPatientSchemeInfoPage(PatientSchemeInfoQueryDTO query) {
        Page<PatientSchemeInfo> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<PatientSchemeInfo> patientSchemeInfoPage = patientSchemeInfoMapper.selectPatientSchemeInfoPage(page, query);

        // 转换为VO
        IPage<PatientSchemeInfoVO> voPage = new Page<>(patientSchemeInfoPage.getCurrent(), patientSchemeInfoPage.getSize(), patientSchemeInfoPage.getTotal());
        List<PatientSchemeInfoVO> voList = patientSchemeInfoPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public PatientSchemeInfoVO getPatientSchemeInfoDetail(Long id) {
        PatientSchemeInfo patientSchemeInfo = patientSchemeInfoMapper.selectById(id);
        return convertToVO(patientSchemeInfo);
    }

    @Override
    public List<PrescriptionListDTO> getPatientPrescriptionList(String schemeId) {
        // 这里先返回模拟数据，实际应该从数据库查询
        List<PrescriptionListDTO> prescriptionList = new ArrayList<>();

        PrescriptionListDTO prescription1 = new PrescriptionListDTO();
        prescription1.setId(1L);
        prescription1.setExerciseName("慢走");
        prescription1.setExerciseType("有氧运动");
        prescription1.setFrequency("每日");
        prescription1.setDuration(30);
        prescription1.setIntensity("低强度");
        prescription1.setSets(1);
        prescription1.setReps(1);
        prescription1.setRestTime(0);
        prescription1.setTargetArea("全身");
        prescription1.setInstructions("保持匀速，呼吸平稳");
        prescription1.setStatus("completed");
        prescription1.setSchemeId(schemeId);
        prescription1.setCreateTime(LocalDateTime.now().minusDays(7));
        prescriptionList.add(prescription1);

        PrescriptionListDTO prescription2 = new PrescriptionListDTO();
        prescription2.setId(2L);
        prescription2.setExerciseName("深蹲");
        prescription2.setExerciseType("力量训练");
        prescription2.setFrequency("每周3次");
        prescription2.setDuration(15);
        prescription2.setIntensity("中等强度");
        prescription2.setSets(3);
        prescription2.setReps(12);
        prescription2.setRestTime(60);
        prescription2.setTargetArea("下肢");
        prescription2.setInstructions("膝盖不超过脚尖，背部挺直");
        prescription2.setStatus("in_progress");
        prescription2.setSchemeId(schemeId);
        prescription2.setCreateTime(LocalDateTime.now().minusDays(5));
        prescriptionList.add(prescription2);

        PrescriptionListDTO prescription3 = new PrescriptionListDTO();
        prescription3.setId(3L);
        prescription3.setExerciseName("拉伸运动");
        prescription3.setExerciseType("柔韧性训练");
        prescription3.setFrequency("每日");
        prescription3.setDuration(10);
        prescription3.setIntensity("低强度");
        prescription3.setSets(1);
        prescription3.setReps(1);
        prescription3.setRestTime(30);
        prescription3.setTargetArea("全身");
        prescription3.setInstructions("缓慢拉伸，避免过度用力");
        prescription3.setStatus("pending");
        prescription3.setSchemeId(schemeId);
        prescription3.setCreateTime(LocalDateTime.now().minusDays(3));
        prescriptionList.add(prescription3);

        return prescriptionList;
    }

    /**
     * 实体转VO
     */
    private PatientSchemeInfoVO convertToVO(PatientSchemeInfo patientSchemeInfo) {
        if (patientSchemeInfo == null) {
            return null;
        }

        PatientSchemeInfoVO vo = new PatientSchemeInfoVO();
        BeanUtils.copyProperties(patientSchemeInfo, vo);

        // SQL 查询已经通过 LEFT JOIN 返回了患者信息，直接使用即可，无需再次查询数据库
        vo.setPatientName(patientSchemeInfo.getPatientName());
        vo.setGenderDesc(patientSchemeInfo.getGender());
        vo.setMedicalRecordNo(patientSchemeInfo.getMedicalRecordNo());
        vo.setDiseaseType(patientSchemeInfo.getDiseaseType());
        vo.setOrgName(patientSchemeInfo.getOrgName());

        // 计算年龄
        if (patientSchemeInfo.getBirthDate() != null) {
            vo.setAge(Period.between(patientSchemeInfo.getBirthDate(), LocalDate.now()).getYears());
        }

        // 设置状态描述
        vo.setStatusDesc(patientSchemeInfo.getStatus() == 1 ? "执行中" : "已结束");

        return vo;
    }
}
