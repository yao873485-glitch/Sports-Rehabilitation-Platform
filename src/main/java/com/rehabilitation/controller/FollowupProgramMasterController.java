package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.FollowupProgramCreateDTO;
import com.rehabilitation.dto.FollowupProgramQueryDTO;
import com.rehabilitation.dto.FollowupProgramUpdateDTO;
import com.rehabilitation.dto.FollowupProgramVO;
import com.rehabilitation.service.FollowupProgramMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 随访项目主数据控制器
 */
@Tag(name = "随访项目管理", description = "随访项目管理接口")
@RestController
@RequestMapping("/api/followup-program")
@RequiredArgsConstructor
public class FollowupProgramMasterController {

    private final FollowupProgramMasterService followupProgramMasterService;

    @Operation(summary = "分页查询随访项目")
    @GetMapping("/page")
    public Result<PageResult<FollowupProgramVO>> getFollowupProgramPage(FollowupProgramQueryDTO queryDTO) {
        PageResult<FollowupProgramVO> pageResult = followupProgramMasterService.getFollowupProgramPage(queryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据ID获取随访项目详情")
    @GetMapping("/{id}")
    public Result<FollowupProgramVO> getFollowupProgramById(@PathVariable Long id) {
        FollowupProgramVO programVO = followupProgramMasterService.getFollowupProgramById(id);
        if (programVO == null) {
            return Result.error("项目不存在");
        }
        return Result.success(programVO);
    }

    @Operation(summary = "根据计划创建随访项目")
    @PostMapping("/create")
    public Result<Long> createProgramFromPlan(@RequestBody FollowupProgramCreateDTO dto) {
        Long programId = followupProgramMasterService.createProgramFromPlan(dto);
        return Result.success(programId);
    }

    @Operation(summary = "获取指定模板的项目数量")
    @GetMapping("/count/{templateName}")
    public Result<Integer> countByTemplate(@PathVariable String templateName) {
        int count = followupProgramMasterService.countByTemplate(templateName);
        return Result.success(count);
    }

    @Operation(summary = "更新随访项目")
    @PutMapping
    public Result<Void> updateFollowupProgram(@RequestBody FollowupProgramUpdateDTO updateDTO) {
        boolean success = followupProgramMasterService.updateFollowupProgram(updateDTO);
        if (success) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }
}