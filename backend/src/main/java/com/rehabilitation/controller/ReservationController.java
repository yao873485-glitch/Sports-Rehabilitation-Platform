package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.ReservationQueryDTO;
import com.rehabilitation.dto.ReservationVO;
import com.rehabilitation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 课程预约记录控制器
 */
@Tag(name = "排课管理", description = "课程预约记录管理接口")
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * 分页查询课程预约记录列表
     */
    @Operation(summary = "分页查询课程预约记录列表")
    @GetMapping("/list")
    public Result<PageResult<ReservationVO>> getReservationPage(ReservationQueryDTO query) {
        PageResult<ReservationVO> result = reservationService.getReservationPage(query);
        return Result.success(result);
    }

    /**
     * 获取预约详情
     */
    @Operation(summary = "获取预约详情")
    @GetMapping("/{id}")
    public Result<ReservationVO> getReservationDetail(@PathVariable Integer id) {
        ReservationVO result = reservationService.getReservationDetail(id);
        return Result.success(result);
    }

    /**
     * 核销预约
     */
    @Operation(summary = "核销预约")
    @PutMapping("/{id}/verify")
    public Result<Void> verifyReservation(@PathVariable Integer id) {
        reservationService.verifyReservation(id);
        return Result.success();
    }
}
