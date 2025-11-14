package com.rehabilitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.common.PageResult;
import com.rehabilitation.dto.ReservationQueryDTO;
import com.rehabilitation.dto.ReservationVO;
import com.rehabilitation.entity.ClassReservationRecord;

/**
 * 课程预约记录服务接口
 */
public interface ReservationService extends IService<ClassReservationRecord> {

    /**
     * 分页查询课程预约记录列表
     */
    PageResult<ReservationVO> getReservationPage(ReservationQueryDTO query);

    /**
     * 根据ID获取预约详情
     */
    ReservationVO getReservationDetail(Integer id);

    /**
     * 核销预约
     */
    void verifyReservation(Integer id);
}
