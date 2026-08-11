package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.ClassroomFacilityQueryDTO;
import com.rehabilitation.dto.ClassroomFacilityVO;
import com.rehabilitation.entity.ClassroomFacilityInfo;
import com.rehabilitation.service.ClassroomFacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教室设施管理控制器
 */
@Tag(name = "教室设施管理", description = "教室设施管理接口")
@RestController
@RequestMapping("/api/classroom-facility")
public class ClassroomFacilityController {

    @Autowired
    private ClassroomFacilityService classroomFacilityService;

    /**
     * 分页查询教室设施列表
     */
    @Operation(summary = "分页查询教室设施列表")
    @GetMapping("/list")
    public Result<PageResult<ClassroomFacilityVO>> getClassroomFacilityPage(ClassroomFacilityQueryDTO query) {
        PageResult<ClassroomFacilityVO> result = classroomFacilityService.getClassroomFacilityPage(query);
        return Result.success(result);
    }

    /**
     * 获取教室设施详情
     */
    @Operation(summary = "获取教室设施详情")
    @GetMapping("/{id}")
    public Result<ClassroomFacilityVO> getClassroomFacilityById(@PathVariable Integer id) {
        ClassroomFacilityVO result = classroomFacilityService.getClassroomFacilityById(id);
        return Result.success(result);
    }

    /**
     * 新增教室设施
     */
    @Operation(summary = "新增教室设施")
    @PostMapping
    public Result<Void> addClassroomFacility(@RequestBody ClassroomFacilityInfo classroomFacility) {
        classroomFacilityService.addClassroomFacility(classroomFacility);
        return Result.success();
    }

    /**
     * 更新教室设施
     */
    @Operation(summary = "更新教室设施")
    @PutMapping("/{id}")
    public Result<Void> updateClassroomFacility(@PathVariable Integer id, @RequestBody ClassroomFacilityInfo classroomFacility) {
        classroomFacility.setId(id);
        classroomFacilityService.updateClassroomFacility(classroomFacility);
        return Result.success();
    }

    /**
     * 删除教室设施
     */
    @Operation(summary = "删除教室设施")
    @DeleteMapping("/{id}")
    public Result<Void> deleteClassroomFacility(@PathVariable Integer id) {
        classroomFacilityService.deleteClassroomFacility(id);
        return Result.success();
    }

    /**
     * 下架教室设施
     */
    @Operation(summary = "下架教室设施")
    @PutMapping("/{id}/offline")
    public Result<Void> offlineClassroomFacility(@PathVariable Integer id) {
        classroomFacilityService.offlineClassroomFacility(id);
        return Result.success();
    }

    /**
     * 上架教室设施
     */
    @Operation(summary = "上架教室设施")
    @PutMapping("/{id}/online")
    public Result<Void> onlineClassroomFacility(@PathVariable Integer id) {
        classroomFacilityService.onlineClassroomFacility(id);
        return Result.success();
    }

    /**
     * 获取所有运动类型
     */
    @Operation(summary = "获取所有运动类型")
    @GetMapping("/exercise-types")
    public Result<List<String>> getAllExerciseTypes() {
        List<String> result = classroomFacilityService.getAllExerciseTypes();
        return Result.success(result);
    }
}