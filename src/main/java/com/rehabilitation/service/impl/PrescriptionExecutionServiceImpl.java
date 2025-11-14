package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.PrescriptionExecutionQueryDTO;
import com.rehabilitation.dto.PrescriptionExecutionVO;
import com.rehabilitation.entity.PatientPrescribedExercise;
import com.rehabilitation.mapper.PrescriptionExecutionMapper;
import com.rehabilitation.service.PrescriptionExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 处方执行Service实现类
 */
@Service
@RequiredArgsConstructor
public class PrescriptionExecutionServiceImpl implements PrescriptionExecutionService {

    private final PrescriptionExecutionMapper prescriptionExecutionMapper;

    @Override
    public IPage<PrescriptionExecutionVO> getPrescriptionExecutionPage(PrescriptionExecutionQueryDTO query) {
        Page<PrescriptionExecutionVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return prescriptionExecutionMapper.selectPrescriptionExecutionPage(page, query);
    }

    @Override
    public PrescriptionExecutionVO getPrescriptionExecutionById(Integer id) {
        return prescriptionExecutionMapper.selectPrescriptionExecutionById(id);
    }

    @Override
    public void endPrescriptionExecution(Integer id) {
        PatientPrescribedExercise prescription = new PatientPrescribedExercise();
        prescription.setId(id);
        prescription.setPrescriptionStatus(4); // 4-已结束
        prescription.setUpdatedTime(LocalDateTime.now());
        prescriptionExecutionMapper.updateById(prescription);
    }

    @Override
    public void executePrescription(Integer id) {
        PatientPrescribedExercise prescription = new PatientPrescribedExercise();
        prescription.setId(id);
        prescription.setPrescriptionStatus(2); // 2-执行中
        prescription.setUpdatedTime(LocalDateTime.now());
        prescriptionExecutionMapper.updateById(prescription);
    }

    @Override
    public List<String> getAllDiseaseTypes() {
        return Arrays.asList(
                "妊娠合并糖尿病",
                "肥胖症",
                "高血压",
                "糖尿病",
                "冠心病",
                "慢性阻塞性肺疾病",
                "骨关节炎",
                "颈椎病",
                "腰椎间盘突出",
                "脑卒中后遗症"
        );
    }

    @Override
    public List<String> getAllInstitutions() {
        return Arrays.asList(
                "西南医科大学附属医院",
                "江安县中医院",
                "泸州市人民医院",
                "合江县人民医院",
                "古蔺县人民医院",
                "叙永县人民医院"
        );
    }
}