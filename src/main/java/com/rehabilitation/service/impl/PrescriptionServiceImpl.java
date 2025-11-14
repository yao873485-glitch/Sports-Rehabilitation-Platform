package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.PrescriptionQueryDTO;
import com.rehabilitation.dto.PrescriptionVO;
import com.rehabilitation.entity.PatientPrescribedExercise;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.PatientPrescribedExerciseMapper;
import com.rehabilitation.service.PrescriptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 运动处方服务实现
 */
@Service
public class PrescriptionServiceImpl 
    extends ServiceImpl<PatientPrescribedExerciseMapper, PatientPrescribedExercise> 
    implements PrescriptionService {

    @Override
    public PageResult<PrescriptionVO> getPrescriptionPage(PrescriptionQueryDTO query) {
        Page<PrescriptionVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<PrescriptionVO> result = baseMapper.selectPrescriptionPage(page, query);
        
        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public PrescriptionVO getPrescriptionDetail(Integer id) {
        PatientPrescribedExercise entity = getById(id);
        if (entity == null) {
            throw new BusinessException("运动处方不存在");
        }
        
        PrescriptionVO vo = new PrescriptionVO();
        BeanUtils.copyProperties(entity, vo);
        
        return vo;
    }

    @Override
    public void endPrescription(Integer id) {
        PatientPrescribedExercise entity = getById(id);
        if (entity == null) {
            throw new BusinessException("运动处方不存在");
        }

        if (Integer.valueOf(4).equals(entity.getPrescriptionStatus())) {
            throw new BusinessException("运动处方已结束，无需重复操作");
        }

        entity.setPrescriptionStatus(4); // 4-已结束
        updateById(entity);
    }

    @Override
    public void executePrescription(Integer id) {
        PatientPrescribedExercise entity = getById(id);
        if (entity == null) {
            throw new BusinessException("运动处方不存在");
        }

        if (Integer.valueOf(2).equals(entity.getPrescriptionStatus())) {
            throw new BusinessException("运动处方已在执行中");
        }

        if (Integer.valueOf(4).equals(entity.getPrescriptionStatus())) {
            throw new BusinessException("运动处方已结束，无法执行");
        }

        entity.setPrescriptionStatus(2); // 2-执行中
        updateById(entity);
    }
}
