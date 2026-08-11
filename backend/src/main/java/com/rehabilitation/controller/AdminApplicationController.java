package com.rehabilitation.controller;

import com.rehabilitation.common.Result;
import com.rehabilitation.dto.PendingUserApplicationVO;
import com.rehabilitation.service.AdminApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员审核控制器
 */
@Tag(name = "管理员审核", description = "待审核用户管理接口")
@RestController
@RequestMapping("/admin/applications")
public class AdminApplicationController {

    @Autowired
    private AdminApplicationService adminApplicationService;

    @Operation(summary = "查询待审核用户列表")
    @GetMapping
    public Result<List<PendingUserApplicationVO>> listPendingApplications() {
        return Result.success(adminApplicationService.listPendingApplications());
    }

    @Operation(summary = "审核通过")
    @PostMapping("/{userId}/approve")
    public Result<Void> approve(@PathVariable Long userId) {
        adminApplicationService.approveUser(userId);
        return Result.success("审核通过", null);
    }

    @Operation(summary = "审核拒绝")
    @PostMapping("/{userId}/reject")
    public Result<Void> reject(@PathVariable Long userId) {
        adminApplicationService.rejectUser(userId);
        return Result.success("已拒绝该申请", null);
    }
}
