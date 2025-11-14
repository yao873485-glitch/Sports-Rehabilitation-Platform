package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.EduContentApplicationQueryDTO;
import com.rehabilitation.dto.EduContentApplicationVO;
import com.rehabilitation.service.EduContentApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 内容应用管理控制器
 */
@Tag(name = "内容应用管理", description = "内容应用管理接口")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class EduContentApplicationController {

    private final EduContentApplicationService contentApplicationService;

    @Operation(summary = "分页查询内容应用管理列表")
    @GetMapping("/content-management/list")
    public Result getContentApplicationPage(EduContentApplicationQueryDTO queryDTO) {
        PageResult pageResult = contentApplicationService.getContentApplicationPage(queryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据ID获取内容应用配置详情")
    @GetMapping("/content-management/{id}")
    public Result getContentManagementDetail(@PathVariable Long id) {
        EduContentApplicationVO vo = contentApplicationService.getContentApplicationById(id);
        if (vo == null) {
            return Result.error("内容应用配置不存在");
        }
        return Result.success(vo);
    }

    @Operation(summary = "根据ID获取内容应用配置详情")
    @GetMapping("/content-application/{id}")
    public Result<EduContentApplicationVO> getContentApplicationById(@PathVariable Long id) {
        EduContentApplicationVO vo = contentApplicationService.getContentApplicationById(id);
        if (vo == null) {
            return Result.error("内容应用配置不存在");
        }
        return Result.success(vo);
    }

    @Operation(summary = "更新内容应用配置")
    @PutMapping("/content-application/{id}")
    public Result<Void> updateContentApplication(@PathVariable Long id, @RequestBody EduContentApplicationVO vo) {
        vo.setId(id);
        boolean result = contentApplicationService.updateContentApplication(vo);
        if (result) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @Operation(summary = "更新内容状态")
    @PutMapping("/content-management/{id}/status")
    public Result<Void> updateContentStatus(@PathVariable Long id, @RequestParam String status) {
        boolean result = contentApplicationService.updateContentStatus(id, status);
        if (result) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @Operation(summary = "删除内容应用记录")
    @DeleteMapping("/content-management/{id}")
    public Result<Void> deleteContentApplication(@PathVariable Long id) {
        boolean result = contentApplicationService.deleteContentApplication(id);
        if (result) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
}
