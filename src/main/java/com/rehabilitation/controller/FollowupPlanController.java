package com.rehabilitation.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.FollowupPlanDTO;
import com.rehabilitation.dto.FollowupPlanQueryDTO;
import com.rehabilitation.entity.FollowupPlan;
import com.rehabilitation.service.FollowupPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 随访计划控制器
 */
@RestController
@RequestMapping("/api/followup-plan")
@Tag(name = "随访计划", description = "随访计划相关接口")
public class FollowupPlanController {

    private static final Logger log = LoggerFactory.getLogger(FollowupPlanController.class);

    @Autowired
    private FollowupPlanService followupPlanService;

    /**
     * 保存随访计划（草稿或发布）
     */
    @Operation(summary = "保存随访计划")
    @PostMapping
    public Result<FollowupPlan> savePlan(@RequestBody FollowupPlanDTO dto) {
        log.info("保存随访计划: {}", dto);
        try {
            FollowupPlan plan = followupPlanService.savePlan(dto);
            return Result.success(plan);
        } catch (Exception e) {
            log.error("保存随访计划失败", e);
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询随访计划列表
     */
    @Operation(summary = "分页查询随访计划列表")
    @GetMapping("/page")
    public Result<Page<FollowupPlan>> getPlanPage(FollowupPlanQueryDTO queryDTO) {
        log.info("查询随访计划列表: {}", queryDTO);
        try {
            Page<FollowupPlan> page = followupPlanService.getPlanPage(queryDTO);
            return Result.success(page);
        } catch (Exception e) {
            log.error("查询随访计划列表失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取随访计划详情
     */
    @Operation(summary = "根据ID获取随访计划详情")
    @GetMapping("/{id}")
    public Result<FollowupPlan> getPlanById(@PathVariable Long id) {
        log.info("获取随访计划详情: {}", id);
        try {
            FollowupPlan plan = followupPlanService.getById(id);
            if (plan == null) {
                return Result.error("计划不存在");
            }
            return Result.success(plan);
        } catch (Exception e) {
            log.error("获取随访计划详情失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 删除随访计划
     */
    @Operation(summary = "删除随访计划")
    @DeleteMapping("/{id}")
    public Result<Void> deletePlan(@PathVariable Long id) {
        log.info("删除随访计划: {}", id);
        try {
            boolean success = followupPlanService.removeById(id);
            if (success) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除随访计划失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}
