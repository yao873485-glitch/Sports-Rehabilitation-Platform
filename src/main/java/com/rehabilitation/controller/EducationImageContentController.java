package com.rehabilitation.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.EducationImageContentDTO;
import com.rehabilitation.entity.EducationImageContent;
import com.rehabilitation.service.EducationImageContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 医教图文内容控制器
 */
@RestController
@RequestMapping("/education/image-content")
@Tag(name = "医教图文内容", description = "医教图文内容管理接口")
public class EducationImageContentController {

    private static final Logger log = LoggerFactory.getLogger(EducationImageContentController.class);

    @Autowired
    private EducationImageContentService contentService;

    /**
     * 保存图文内容
     */
    @Operation(summary = "保存图文内容")
    @PostMapping
    public Result<EducationImageContent> saveContent(@RequestBody EducationImageContentDTO dto) {
        log.info("保存图文内容: {}", dto.getContentTitle());
        try {
            EducationImageContent content = contentService.saveContent(dto);
            return Result.success(content);
        } catch (Exception e) {
            log.error("保存图文内容失败", e);
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询图文内容列表
     */
    @Operation(summary = "分页查询图文内容列表")
    @GetMapping("/page")
    public Result<Page<EducationImageContent>> getContentPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String contentTitle,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String categorySection,
            @RequestParam(required = false) String contentStatus,
            @RequestParam(required = false) String publishStartTime,
            @RequestParam(required = false) String publishEndTime) {
        log.info("查询图文内容列表，页码：{}，页大小：{}", pageNum, pageSize);
        try {
            Page<EducationImageContent> page = contentService.getContentPage(
                    pageNum, pageSize, contentTitle, author, categorySection,
                    contentStatus, publishStartTime, publishEndTime);
            return Result.success(page);
        } catch (Exception e) {
            log.error("查询图文内容列表失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取图文内容详情
     */
    @Operation(summary = "根据ID获取图文内容详情")
    @GetMapping("/{id}")
    public Result<EducationImageContent> getContentById(@PathVariable Long id) {
        log.info("获取图文内容详情，ID：{}", id);
        try {
            EducationImageContent content = contentService.getById(id);
            if (content == null) {
                return Result.error("内容不存在");
            }
            return Result.success(content);
        } catch (Exception e) {
            log.error("获取图文内容详情失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 删除图文内容
     */
    @Operation(summary = "删除图文内容")
    @DeleteMapping("/{id}")
    public Result<Void> deleteContent(@PathVariable Long id) {
        log.info("删除图文内容，ID：{}", id);
        try {
            boolean success = contentService.removeById(id);
            if (success) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除图文内容失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}
