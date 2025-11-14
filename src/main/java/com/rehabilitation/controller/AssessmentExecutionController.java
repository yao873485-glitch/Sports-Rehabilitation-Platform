package com.rehabilitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.AssessmentExecutionQueryDTO;
import com.rehabilitation.dto.AssessmentExecutionVO;
import com.rehabilitation.service.AssessmentExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评定执行管理控制器
 */
@Tag(name = "评定执行管理", description = "评定执行管理接口")
@RestController
@RequestMapping("/api/assessment-execution")
@RequiredArgsConstructor
public class AssessmentExecutionController {

    private final AssessmentExecutionService assessmentExecutionService;

    /**
     * 分页查询评定执行列表
     */
    @Operation(summary = "分页查询评定执行列表")
    @GetMapping("/list")
    public Result<IPage<AssessmentExecutionVO>> getAssessmentExecutionPage(AssessmentExecutionQueryDTO query) {
        IPage<AssessmentExecutionVO> result = assessmentExecutionService.getAssessmentExecutionPage(query);
        return Result.success(result);
    }

    /**
     * 获取评定执行详情
     */
    @Operation(summary = "获取评定执行详情")
    @GetMapping("/{id}")
    public Result<AssessmentExecutionVO> getAssessmentExecutionById(@PathVariable Integer id) {
        AssessmentExecutionVO result = assessmentExecutionService.getAssessmentExecutionById(id);
        return Result.success(result);
    }

    /**
     * 结束评定执行
     */
    @Operation(summary = "结束评定执行")
    @PutMapping("/{id}/end")
    public Result<Void> endAssessmentExecution(@PathVariable Integer id) {
        assessmentExecutionService.endAssessmentExecution(id);
        return Result.success();
    }

    /**
     * 执行评定
     */
    @Operation(summary = "执行评定")
    @PutMapping("/{id}/execute")
    public Result<Void> executeAssessment(@PathVariable Integer id) {
        assessmentExecutionService.executeAssessment(id);
        return Result.success();
    }

    /**
     * 获取所有病种类型
     */
    @Operation(summary = "获取所有病种类型")
    @GetMapping("/disease-types")
    public Result<List<String>> getAllDiseaseTypes() {
        List<String> result = assessmentExecutionService.getAllDiseaseTypes();
        return Result.success(result);
    }

    /**
     * 获取所有机构列表
     */
    @Operation(summary = "获取所有机构列表")
    @GetMapping("/institutions")
    public Result<List<String>> getAllInstitutions() {
        List<String> result = assessmentExecutionService.getAllInstitutions();
        return Result.success(result);
    }
}