package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.AssessmentQueryDTO;
import com.rehabilitation.dto.AssessmentVO;
import com.rehabilitation.entity.AssessmentExecutionRecord;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.AssessmentExecutionRecordMapper;
import com.rehabilitation.service.AssessmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 评定执行记录服务实现
 */
@Service
public class AssessmentServiceImpl 
    extends ServiceImpl<AssessmentExecutionRecordMapper, AssessmentExecutionRecord> 
    implements AssessmentService {

    @Override
    public PageResult<AssessmentVO> getAssessmentPage(AssessmentQueryDTO query) {
        Page<AssessmentVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<AssessmentVO> result = baseMapper.selectAssessmentPage(page, query);
        
        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public AssessmentVO getAssessmentDetail(Integer id) {
        AssessmentExecutionRecord entity = getById(id);
        if (entity == null) {
            throw new BusinessException("评定方案不存在");
        }
        
        AssessmentVO vo = new AssessmentVO();
        BeanUtils.copyProperties(entity, vo);
        
        return vo;
    }

    @Override
    public void endAssessment(Integer id) {
        AssessmentExecutionRecord entity = getById(id);
        if (entity == null) {
            throw new BusinessException("评定方案不存在");
        }

        if (Integer.valueOf(4).equals(entity.getStatus())) {
            throw new BusinessException("评定方案已结束，无需重复操作");
        }

        entity.setStatus(4); // 4-已结束
        updateById(entity);
    }

    @Override
    public void executeAssessment(Integer id) {
        AssessmentExecutionRecord entity = getById(id);
        if (entity == null) {
            throw new BusinessException("评定方案不存在");
        }

        if (Integer.valueOf(2).equals(entity.getStatus())) {
            throw new BusinessException("评定方案已在执行中");
        }

        if (Integer.valueOf(4).equals(entity.getStatus())) {
            throw new BusinessException("评定方案已结束，无法执行");
        }

        entity.setStatus(2); // 2-执行中
        updateById(entity);
    }
}
