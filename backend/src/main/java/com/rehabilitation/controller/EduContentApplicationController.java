package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.EduContentApplicationQueryDTO;
import com.rehabilitation.service.EduContentApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * 内容应用管理控制器
 * 注意：数据现在通过联合查询从图文素材表和视频素材表获取，不再维护edu_content_application表
 */
@Tag(name = "内容应用管理", description = "内容应用管理接口")
@RestController
@CrossOrigin
public class EduContentApplicationController {

    private final EduContentApplicationService contentApplicationService;

    // 手动添加构造器以避免Lombok问题
    public EduContentApplicationController(EduContentApplicationService contentApplicationService) {
        this.contentApplicationService = contentApplicationService;
    }

    @Operation(summary = "分页查询内容应用管理列表（联合图文和视频素材表）")
    @GetMapping("/content-management/list")
    public Result getContentApplicationPage(EduContentApplicationQueryDTO queryDTO) {
        PageResult pageResult = contentApplicationService.getContentApplicationPage(queryDTO);
        return Result.success(pageResult);
    }
}
