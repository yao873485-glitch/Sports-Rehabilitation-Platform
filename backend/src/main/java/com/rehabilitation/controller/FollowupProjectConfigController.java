package com.rehabilitation.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.FollowupProjectConfigDTO;
import com.rehabilitation.dto.FollowupProjectConfigQueryDTO;
import com.rehabilitation.entity.FollowupProjectConfig;
import com.rehabilitation.service.FollowupProjectConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 随访项目配置控制器
 */
@RestController
@RequestMapping("/api/followup-project-config")
@Tag(name = "随访项目配置", description = "随访项目配置相关接口")
public class FollowupProjectConfigController {

    private static final Logger log = LoggerFactory.getLogger(FollowupProjectConfigController.class);

    @Autowired
    private FollowupProjectConfigService followupProjectConfigService;

    /**
     * 保存随访项目配置
     */
    @Operation(summary = "保存随访项目配置")
    @PostMapping
    public Result<FollowupProjectConfig> saveProjectConfig(@RequestBody FollowupProjectConfigDTO dto) {
        log.info("保存随访项目配置: {}", dto);
        try {
            FollowupProjectConfig config = followupProjectConfigService.saveProjectConfig(dto);
            return Result.success(config);
        } catch (Exception e) {
            log.error("保存随访项目配置失败", e);
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询项目配置列表
     */
    @Operation(summary = "分页查询项目配置列表")
    @GetMapping("/page")
    public Result<Page<FollowupProjectConfig>> getProjectConfigPage(FollowupProjectConfigQueryDTO queryDTO) {
        log.info("分页查询项目配置列表: {}", queryDTO);
        try {
            Page<FollowupProjectConfig> page = followupProjectConfigService.getProjectConfigPage(queryDTO);
            return Result.success(page);
        } catch (Exception e) {
            log.error("查询项目配置列表失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取项目配置详情
     */
    @Operation(summary = "根据ID获取项目配置详情")
    @GetMapping("/{id}")
    public Result<FollowupProjectConfig> getProjectConfigById(@PathVariable Long id) {
        log.info("获取项目配置详情，ID: {}", id);
        try {
            FollowupProjectConfig config = followupProjectConfigService.getById(id);
            if (config == null) {
                return Result.error("项目配置不存在");
            }
            return Result.success(config);
        } catch (Exception e) {
            log.error("获取项目配置详情失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 更新项目配置
     */
    @Operation(summary = "更新项目配置")
    @PutMapping
    public Result<FollowupProjectConfig> updateProjectConfig(@RequestBody FollowupProjectConfigDTO dto) {
        log.info("更新项目配置: {}", dto);
        try {
            FollowupProjectConfig config = followupProjectConfigService.updateProjectConfig(dto);
            return Result.success(config);
        } catch (Exception e) {
            log.error("更新项目配置失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }
}
