package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.ReservationQueryDTO;
import com.rehabilitation.dto.ReservationVO;
import com.rehabilitation.entity.ClassReservationRecord;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.ClassReservationRecordMapper;
import com.rehabilitation.service.ReservationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 课程预约记录服务实现
 */
@Service
public class ReservationServiceImpl 
    extends ServiceImpl<ClassReservationRecordMapper, ClassReservationRecord> 
    implements ReservationService {

    @Override
    public PageResult<ReservationVO> getReservationPage(ReservationQueryDTO query) {
        Page<ReservationVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<ReservationVO> result = baseMapper.selectReservationPage(page, query);
        
        return PageResult.build(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    @Override
    public ReservationVO getReservationDetail(Integer id) {
        ClassReservationRecord entity = getById(id);
        if (entity == null) {
            throw new BusinessException("预约记录不存在");
        }
        
        ReservationVO vo = new ReservationVO();
        BeanUtils.copyProperties(entity, vo);
        
        return vo;
    }

    @Override
    public void verifyReservation(Integer id) {
        ClassReservationRecord entity = getById(id);
        if (entity == null) {
            throw new BusinessException("预约记录不存在");
        }
        
        if ("已核销".equals(entity.getReservationStatus())) {
            throw new BusinessException("预约已核销，无需重复操作");
        }
        
        if ("已逾期".equals(entity.getReservationStatus())) {
            throw new BusinessException("预约已逾期，无法核销");
        }
        
        if ("已取消".equals(entity.getReservationStatus())) {
            throw new BusinessException("预约已取消，无法核销");
        }
        
        entity.setReservationStatus("已核销");
        updateById(entity);
    }
}
