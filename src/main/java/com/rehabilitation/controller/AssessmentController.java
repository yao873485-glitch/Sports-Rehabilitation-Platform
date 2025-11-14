package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.AssessmentQueryDTO;
import com.rehabilitation.dto.AssessmentVO;
import com.rehabilitation.service.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 评定管理控制器
 */
@Tag(name = "评定管理", description = "评定执行记录管理接口")
@RestController
@RequestMapping("/assessment")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    /**
     * 分页查询评定执行记录列表
     */
    @Operation(summary = "分页查询评定执行记录列表")
    @GetMapping("/list")
    public Result<PageResult<AssessmentVO>> getAssessmentPage(AssessmentQueryDTO query) {
        PageResult<AssessmentVO> result = assessmentService.getAssessmentPage(query);
        return Result.success(result);
    }

    /**
     * 获取评定方案详情
     */
    @Operation(summary = "获取评定方案详情")
    @GetMapping("/{id}")
    public Result<AssessmentVO> getAssessmentDetail(@PathVariable Integer id) {
        AssessmentVO result = assessmentService.getAssessmentDetail(id);
        return Result.success(result);
    }

    /**
     * 结束评定方案
     */
    @Operation(summary = "结束评定方案")
    @PutMapping("/{id}/end")
    public Result<Void> endAssessment(@PathVariable Integer id) {
        assessmentService.endAssessment(id);
        return Result.success();
    }

    /**
     * 执行评定方案
     */
    @Operation(summary = "执行评定方案")
    @PutMapping("/{id}/execute")
    public Result<Void> executeAssessment(@PathVariable Integer id) {
        assessmentService.executeAssessment(id);
        return Result.success();
    }
}
