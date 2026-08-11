package com.rehabilitation.controller;

import com.rehabilitation.common.PageResult;
import com.rehabilitation.common.Result;
import com.rehabilitation.dto.SharedFileQueryDTO;
import com.rehabilitation.dto.SharedFileVO;
import com.rehabilitation.service.FileShareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件共享控制器
 */
@Tag(name = "医患沟通平台", description = "文件共享管理接口")
@RestController
@RequestMapping("/file-share")
public class FileShareController {

    @Autowired
    private FileShareService fileShareService;

    /**
     * 上传文件
     */
    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file,
                                    @RequestParam Long patientId,
                                    @RequestParam String patientName) {
        String fileUrl = fileShareService.uploadFile(file, patientId, patientName);
        return Result.success("文件上传成功", fileUrl);
    }

    /**
     * 获取文件列表
     */
    @Operation(summary = "获取文件列表")
    @GetMapping("/list")
    public Result<PageResult<SharedFileVO>> getFileList(SharedFileQueryDTO query) {
        PageResult<SharedFileVO> result = fileShareService.getFileList(query);
        return Result.success(result);
    }

    /**
     * 下载文件
     */
    @Operation(summary = "下载文件")
    @GetMapping("/download/{id}")
    public Result<String> downloadFile(@PathVariable Long id) {
        String fileUrl = fileShareService.downloadFile(id);
        return Result.success("获取下载链接成功", fileUrl);
    }

    /**
     * 删除文件
     */
    @Operation(summary = "删除文件")
    @DeleteMapping("/{id}")
    public Result<Void> deleteFile(@PathVariable Long id) {
        fileShareService.deleteFile(id);
        return Result.success();
    }
}
