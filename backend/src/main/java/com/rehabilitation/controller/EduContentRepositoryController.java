package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.EduContentQueryDTO;
import com.rehabilitation.dto.EduContentVO;
import com.rehabilitation.service.EduContentRepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 教育内容资源库控制器
 */
@Tag(name = "图文素材库管理", description = "图文素材库管理接口")
@RestController
@RequestMapping("/edu-content")
@CrossOrigin
public class EduContentRepositoryController {

    @Autowired
    private EduContentRepositoryService eduContentRepositoryService;

    @Operation(summary = "分页查询图文素材")
    @GetMapping("/page")
    public Result<PageResult<EduContentVO>> getEduContentPage(EduContentQueryDTO queryDTO) {
        PageResult<EduContentVO> pageResult = eduContentRepositoryService.getEduContentPage(queryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据ID获取图文素材详情")
    @GetMapping("/{id}")
    public Result<EduContentVO> getEduContentById(@PathVariable Long id) {
        EduContentVO contentVO = eduContentRepositoryService.getEduContentById(id);
        if (contentVO == null) {
            return Result.error("内容不存在");
        }
        return Result.success(contentVO);
    }

    @Operation(summary = "根据contentId获取图文素材详情")
    @GetMapping("/by-content-id/{contentId}")
    public Result<EduContentVO> getEduContentByContentId(@PathVariable String contentId) {
        EduContentVO contentVO = eduContentRepositoryService.getEduContentByContentId(contentId);
        if (contentVO == null) {
            return Result.error("内容不存在");
        }
        return Result.success(contentVO);
    }

    @Operation(summary = "添加图文素材")
    @PostMapping
    public Result<Void> addEduContent(@RequestBody EduContentVO contentVO) {
        boolean result = eduContentRepositoryService.addEduContent(contentVO);
        if (result) {
            return Result.success();
        }
        return Result.error("添加失败");
    }

    @Operation(summary = "更新图文素材")
    @PutMapping("/{id}")
    public Result<Void> updateEduContent(@PathVariable Long id, @RequestBody EduContentVO contentVO) {
        contentVO.setId(id);
        boolean result = eduContentRepositoryService.updateEduContent(contentVO);
        if (result) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @Operation(summary = "删除图文素材")
    @DeleteMapping("/{id}")
    public Result<Void> deleteEduContent(@PathVariable Long id) {
        boolean result = eduContentRepositoryService.deleteEduContent(id);
        if (result) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
}