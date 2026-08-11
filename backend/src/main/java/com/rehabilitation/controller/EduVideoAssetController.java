package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.EduVideoQueryDTO;
import com.rehabilitation.dto.EduVideoVO;
import com.rehabilitation.service.EduVideoAssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 教育视频素材控制器
 */
@Tag(name = "视频素材库管理", description = "视频素材库管理接口")
@RestController
@RequestMapping("/video-asset")
@CrossOrigin
public class EduVideoAssetController {

    @Autowired
    private EduVideoAssetService eduVideoAssetService;

    @Operation(summary = "分页查询视频素材")
    @GetMapping("/list")
    public Result<PageResult<EduVideoVO>> getEduVideoPage(EduVideoQueryDTO queryDTO) {
        PageResult<EduVideoVO> pageResult = eduVideoAssetService.getEduVideoPage(queryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据ID获取视频素材详情(支持videoId字符串或数字ID)")
    @GetMapping("/{id}")
    public Result<EduVideoVO> getEduVideoById(@PathVariable String id) {
        EduVideoVO videoVO = null;

        // 尝试按数字ID查询
        try {
            Long numericId = Long.parseLong(id);
            videoVO = eduVideoAssetService.getEduVideoById(numericId);
        } catch (NumberFormatException e) {
            // 如果不是数字,按videoId查询
            videoVO = eduVideoAssetService.getEduVideoByVideoId(id);
        }

        if (videoVO == null) {
            return Result.error("视频不存在");
        }
        return Result.success(videoVO);
    }

    @Operation(summary = "根据videoId获取视频素材详情")
    @GetMapping("/by-video-id/{videoId}")
    public Result<EduVideoVO> getEduVideoByVideoId(@PathVariable String videoId) {
        EduVideoVO videoVO = eduVideoAssetService.getEduVideoByVideoId(videoId);
        if (videoVO == null) {
            return Result.error("视频不存在");
        }
        return Result.success(videoVO);
    }

    @Operation(summary = "添加视频素材")
    @PostMapping
    public Result<Void> addEduVideo(@RequestBody EduVideoVO videoVO) {
        boolean result = eduVideoAssetService.addEduVideo(videoVO);
        if (result) {
            return Result.success();
        }
        return Result.error("添加失败");
    }

    @Operation(summary = "更新视频素材")
    @PutMapping("/{id}")
    public Result<Void> updateEduVideo(@PathVariable Long id, @RequestBody EduVideoVO videoVO) {
        videoVO.setId(id);
        boolean result = eduVideoAssetService.updateEduVideo(videoVO);
        if (result) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @Operation(summary = "删除视频素材")
    @DeleteMapping("/{id}")
    public Result<Void> deleteEduVideo(@PathVariable Long id) {
        boolean result = eduVideoAssetService.deleteEduVideo(id);
        if (result) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
}