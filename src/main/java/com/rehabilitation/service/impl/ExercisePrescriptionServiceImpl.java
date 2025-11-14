package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.dto.ExercisePrescriptionBatchOperationDTO;
import com.rehabilitation.dto.ExercisePrescriptionQueryDTO;
import com.rehabilitation.dto.ExercisePrescriptionVO;
import com.rehabilitation.entity.ExercisePrescription;
import com.rehabilitation.mapper.ExercisePrescriptionMapper;
import com.rehabilitation.service.ExercisePrescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 运动处方信息服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExercisePrescriptionServiceImpl extends ServiceImpl<ExercisePrescriptionMapper, ExercisePrescription> implements ExercisePrescriptionService {

    private final ExercisePrescriptionMapper exercisePrescriptionMapper;

    @Override
    public IPage<ExercisePrescriptionVO> getExercisePrescriptionPage(ExercisePrescriptionQueryDTO query) {
        Page<ExercisePrescription> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<ExercisePrescription> prescriptionPage = exercisePrescriptionMapper.selectExercisePrescriptionPage(page, query);

        // 转换为VO
        IPage<ExercisePrescriptionVO> voPage = new Page<>(prescriptionPage.getCurrent(), prescriptionPage.getSize(), prescriptionPage.getTotal());
        List<ExercisePrescriptionVO> voList = prescriptionPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public ExercisePrescriptionVO getExercisePrescriptionById(Long id) {
        ExercisePrescription prescription = exercisePrescriptionMapper.selectById(id);
        return convertToVO(prescription);
    }

    @Override
    public void addExercisePrescription(ExercisePrescription exercisePrescription) {
        exercisePrescription.setCreatedTime(LocalDateTime.now());
        exercisePrescriptionMapper.insert(exercisePrescription);
    }

    @Override
    public void updateExercisePrescription(ExercisePrescription exercisePrescription) {
        exercisePrescription.setLastOperationTime(LocalDateTime.now());
        exercisePrescriptionMapper.updateById(exercisePrescription);
    }

    @Override
    public List<String> getAllDiseaseTypes() {
        return exercisePrescriptionMapper.selectAllDiseaseTypes();
    }

    @Override
    @Transactional
    public void batchOperation(ExercisePrescriptionBatchOperationDTO batchOperation) {
        List<Long> ids = batchOperation.getIds();
        String operationType = batchOperation.getOperationType();

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("运动处方ID列表不能为空");
        }

        if ("上架".equals(operationType) || "下架".equals(operationType)) {
            exercisePrescriptionMapper.batchUpdateStatus(ids, operationType);
        } else {
            throw new IllegalArgumentException("不支持的批量操作类型: " + operationType);
        }
    }

    @Override
    @Transactional
    public void importExercisePrescription(List<ExercisePrescription> exercisePrescriptionList) {
        if (exercisePrescriptionList != null && !exercisePrescriptionList.isEmpty()) {
            saveBatch(exercisePrescriptionList);
        }
    }

    /**
     * 实体转VO
     */
    private ExercisePrescriptionVO convertToVO(ExercisePrescription prescription) {
        if (prescription == null) {
            return null;
        }
        ExercisePrescriptionVO vo = new ExercisePrescriptionVO();
        BeanUtils.copyProperties(prescription, vo);
        return vo;
    }
}