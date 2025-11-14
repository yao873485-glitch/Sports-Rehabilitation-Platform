package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.ReservationQueryDTO;
import com.rehabilitation.dto.ReservationVO;
import com.rehabilitation.entity.ClassReservationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 课程预约记录Mapper
 */
@Mapper
public interface ClassReservationRecordMapper extends BaseMapper<ClassReservationRecord> {

    /**
     * 分页查询课程预约记录列表
     */
    IPage<ReservationVO> selectReservationPage(Page<ReservationVO> page, 
                                             @Param("query") ReservationQueryDTO query);
}
