package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.FollowupEnrollmentDTO;
import com.rehabilitation.dto.FollowupEnrollmentVO;
import com.rehabilitation.dto.FollowupQueryDTO;
import com.rehabilitation.service.FollowupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 随访管理控制器
 */
@Tag(name = "随访管理", description = "随访患者入组记录管理接口")
@RestController
@RequestMapping("/followup")
public class FollowupController {

    @Autowired
    private FollowupService followupService;

    /**
     * 分页查询随访患者列表
     */
    @Operation(summary = "分页查询随访患者列表")
    @GetMapping("/list")
    public Result<PageResult<FollowupEnrollmentVO>> getFollowupPage(FollowupQueryDTO query) {
        PageResult<FollowupEnrollmentVO> result = followupService.getFollowupPage(query);
        return Result.success(result);
    }

    /**
     * 获取随访患者详情
     */
    @Operation(summary = "获取随访患者详情")
    @GetMapping("/{id}")
    public Result<FollowupEnrollmentVO> getFollowupDetail(@PathVariable Long id) {
        FollowupEnrollmentVO result = followupService.getFollowupDetail(id);
        return Result.success(result);
    }

    /**
     * 患者入组（创建随访记录）
     */
    @Operation(summary = "患者入组")
    @PostMapping("/enroll")
    public Result<Long> enrollPatient(@RequestBody FollowupEnrollmentDTO dto) {
        System.out.println("=== Controller接收到的数据 ===");
        System.out.println("DTO对象: " + dto);
        System.out.println("patientId: " + (dto != null ? dto.getPatientId() : "DTO is null"));
        System.out.println("followupTeam: " + (dto != null ? dto.getFollowupTeam() : "DTO is null"));
        System.out.println("followupStatus: " + (dto != null ? dto.getFollowupStatus() : "DTO is null"));

        Long enrollmentId = followupService.enrollPatient(dto);
        return Result.success(enrollmentId);
    }

    /**
     * 移除项目
     */
    @Operation(summary = "移除项目")
    @PutMapping("/{id}/remove")
    public Result<Void> removeFromProject(@PathVariable Long id) {
        followupService.removeFromProject(id);
        return Result.success();
    }
}
