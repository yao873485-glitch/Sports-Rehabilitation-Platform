package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.FollowupPlanCreateDTO;
import com.rehabilitation.dto.FollowupPlanQueryDTO;
import com.rehabilitation.dto.FollowupPlanVO;
import com.rehabilitation.service.FollowupPlanRepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 随访计划仓库控制器
 */
@Tag(name = "随访计划管理", description = "随访计划管理接口")
@RestController
@RequestMapping("/api/followup-plan")
@RequiredArgsConstructor
public class FollowupPlanRepositoryController {

    private final FollowupPlanRepositoryService followupPlanRepositoryService;

    @Operation(summary = "分页查询随访计划")
    @GetMapping("/page")
    public Result<PageResult<FollowupPlanVO>> getFollowupPlanPage(FollowupPlanQueryDTO queryDTO) {
        PageResult<FollowupPlanVO> pageResult = followupPlanRepositoryService.getFollowupPlanPage(queryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据ID获取随访计划详情")
    @GetMapping("/{id}")
    public Result<FollowupPlanVO> getFollowupPlanById(@PathVariable Long id) {
        FollowupPlanVO planVO = followupPlanRepositoryService.getFollowupPlanById(id);
        if (planVO == null) {
            return Result.error("计划不存在");
        }
        return Result.success(planVO);
    }

    @Operation(summary = "根据模板创建随访计划")
    @PostMapping("/create")
    public Result<Long> createPlanFromTemplate(@RequestBody FollowupPlanCreateDTO dto) {
        Long planId = followupPlanRepositoryService.createPlanFromTemplate(dto);
        return Result.success(planId);
    }

    @Operation(summary = "删除随访计划")
    @DeleteMapping("/{id}")
    public Result<Void> deleteFollowupPlan(@PathVariable Long id) {
        boolean success = followupPlanRepositoryService.deleteFollowupPlan(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }
}