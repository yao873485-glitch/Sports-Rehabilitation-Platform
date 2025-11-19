package com.rehabilitation.controller;

import com.rehabilitation.common.Result;
import com.rehabilitation.dto.ContentApplicationConfigDTO;
import com.rehabilitation.entity.ContentApplicationConfig;
import com.rehabilitation.service.ContentApplicationConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 内容应用配置Controller
 */
@Api(tags = "内容应用配置管理")
@RestController
@RequestMapping("/api/content-config")
public class ContentApplicationConfigController {

    @Autowired
    private ContentApplicationConfigService configService;

    @ApiOperation("根据内容ID和类型获取配置")
    @GetMapping("/get")
    public Result<ContentApplicationConfig> getConfig(
            @RequestParam String contentId,
            @RequestParam String contentType) {
        ContentApplicationConfig config = configService.getByContentIdAndType(contentId, contentType);
        return Result.success(config);
    }

    @ApiOperation("保存或更新配置")
    @PostMapping("/save")
    public Result<String> saveConfig(@RequestBody ContentApplicationConfigDTO dto) {
        configService.saveOrUpdate(dto);
        return Result.success("保存成功");
    }

    @ApiOperation("删除配置")
    @DeleteMapping("/delete")
    public Result<String> deleteConfig(
            @RequestParam String contentId,
            @RequestParam String contentType) {
        configService.deleteByContentIdAndType(contentId, contentType);
        return Result.success("删除成功");
    }
}
