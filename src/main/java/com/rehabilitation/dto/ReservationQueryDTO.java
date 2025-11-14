package com.rehabilitation.dto;

import com.rehabilitation.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 课程预约查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReservationQueryDTO extends PageQuery {

    /**
     * 客户姓名搜索
     */
    private String customerName;

    /**
     * 教室名称
     */
    private String classroomName;

    /**
     * 预约状态
     */
    private String reservationStatus;

    /**
     * 预约开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    /**
     * 预约结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}
